package com.example.exam.integrationtests

import com.example.exam.model.AnimalEntity
import com.example.exam.model.AnimalType
import com.example.exam.service.AnimalService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc(addFilters = true)
class AnimalServiceIntegrationTests {

    @Autowired
    lateinit var animalService: AnimalService

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun shouldCreateAnimal(){
        animalService.createAnimal(AnimalEntity(1, "Bob", AnimalType.DOG, "Good boy", 3, true))
        val createdAnimal = animalService.getAnimalsById(1)
        assert(createdAnimal?.animalName == "Bob")
        assert(createdAnimal?.animalType == AnimalType.DOG)
        assert(createdAnimal?.breed == "Good boy")
        assert(createdAnimal?.age == 3)
        assert(createdAnimal?.healthy == true)
    }
}