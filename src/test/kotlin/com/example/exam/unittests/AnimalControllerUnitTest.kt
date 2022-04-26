package com.example.exam.unittests

import com.example.exam.controller.AnimalController
import com.example.exam.model.AnimalEntity
import com.example.exam.model.AnimalType
import com.example.exam.service.AnimalService
import com.example.exam.service.UserService
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.contains
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.*
import java.util.Optional.empty

@WebMvcTest(AnimalController::class)
@AutoConfigureMockMvc(addFilters = false)
class AnimalControllerUnitTest {

    private val testAnimal: AnimalEntity = AnimalEntity(id = 1, animalName = "Kevin", animalType = AnimalType.RABBIT, breed = "Friend", age = 2, healthy = true)

    @TestConfiguration
    class controllerTestConfigutation{
        @Bean
        fun animalService() = mockk<AnimalService>()

        @Bean
        fun userService() = mockk<UserService>()
    }

    @Autowired
    private lateinit var animalService: AnimalService

    @Autowired
    private lateinit var mockMvc: MockMvc


    @Test
    fun shouldGetAllAnimals(){
        every { animalService.getAnimals() } answers {
            mutableListOf(
                AnimalEntity(id = 1, animalName = "Max", animalType = AnimalType.DOG, breed = "Cutie", age = 1, healthy = true),
                AnimalEntity(id = 2, animalName = "Boo", animalType = AnimalType.DOG, breed = "Cutie", age = 1, healthy = true)
            )
        }
        mockMvc.get("/api/shelter/all"){}
            .andExpect { status { isOk() } }
            .andExpect { content { jsonPath("$[:1].animalName", contains("Max")) } }
            .andExpect { content { jsonPath("$[:1].animalType", contains("DOG")) } }
    }


    @Test
    fun shouldGetAnimalById(){
        every { animalService.getAnimalsById(any()) } answers { testAnimal }
        mockMvc.get("/api/shelter/1"){}
            .andExpect { status { isOk() } }
            .andExpect { content { jsonPath("$.id", `is`(1)) } }
            .andExpect { content { jsonPath("$.animalName", `is`("Kevin")) } }
            .andExpect { content { jsonPath("$.animalType", `is`("RABBIT")) } }

        every { animalService.getAnimalsById(any()) } answers { null }
        mockMvc.get("/api/shelter/1"){}
        .andExpect { status { isBadRequest() } }
        .andExpect { content { empty<String>() } }
    }

    @Test
    fun shouldRegisterAnimal(){
        val jsonAnimalEntity = JSONObject(mapOf("animalName" to "TestName", "animalType" to "DOG", "breed" to "Beagle","age" to 2, "healthy" to true))
        every { animalService.createAnimal(any()) } answers { AnimalEntity(1, "TestName", AnimalType.DOG, "Beagle", 2, true) }
        mockMvc.post("/api/shelter/create"){
            contentType = APPLICATION_JSON
            content = jsonAnimalEntity
        }
            .andExpect { status { isOk() } }
            .andExpect { content { jsonPath("$.animalName", `is`("TestName")) } }
            .andExpect { content { jsonPath("$.animalType", `is`("DOG")) } }
    }

    @Test
    fun shouldUpdateAnimal(){
        val jsonAnimalEntity = JSONObject(mapOf("animalName" to "TestName", "animalType" to "DOG", "breed" to "Beagle","age" to 2, "healthy" to true))
        every { animalService.updateAnimal(any(), any()) } answers { firstArg() }
        mockMvc.put("/api/shelter/update/1"){
            contentType = APPLICATION_JSON
            content = jsonAnimalEntity
        }
            .andExpect { status { isOk() } }
            .andExpect { content { jsonPath("$.animalName", `is`("TestName")) } }
            .andExpect { content { jsonPath("$.animalType", `is`("DOG")) } }

        every { animalService.updateAnimal(any(), any()) } answers { null }
        mockMvc.put("/api/shelter/update/1"){
            contentType = APPLICATION_JSON
            content = jsonAnimalEntity
        }
            .andExpect { status { isBadRequest() } }
    }

    @Test
    fun shouldDeleteAnimal(){
        every { animalService.deleteAnimal(any()) } answers { true }
        mockMvc.delete("/api/shelter/delete/1"){}
            .andExpect { status { isOk() } }
            .andExpect { content { string("true") } }
        every { animalService.deleteAnimal(any()) } answers { false }
        mockMvc.delete("/api/shelter/delete/1"){}
            .andExpect { status { isBadRequest() } }
            .andExpect { content { string("false") } }

    }
}