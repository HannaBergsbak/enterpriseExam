package com.example.exam.unittests

import com.example.exam.controller.AuthController
import com.example.exam.model.AuthorityEntity
import com.example.exam.model.UserEntity
import com.example.exam.service.UserService
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.contains
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.*

@WebMvcTest(AuthController::class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerUnitTest {

    @TestConfiguration
    class ControllerTestConfiguration{
        @Bean
        fun userService() = mockk<UserService>()
    }

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var mockMvc: MockMvc


    //USERS
    @Test
    fun shouldGetAllUsers(){
        every { userService.getUsers() } answers {
            mutableListOf(
                UserEntity(id = 1, userName = "testUser1", userPassword = "pirate"),
                UserEntity(id = 2, userName = "testUser2", userPassword = "pirate")
            )
        }
        mockMvc.get("/api/user/all"){}
            .andExpect { status { isOk() } }
            .andExpect { content { jsonPath("$[:1].userName", contains("testUser1")) } }
    }

    @Test
    fun shouldRegisterUser(){
        val jsonUserEntity = JSONObject(mapOf("username" to "TestName", "password" to "pirate"))
        every { userService.registerUser(any()) } answers { UserEntity(1, "TestName", "pirate") }
        mockMvc.post("/api/user/create"){
            contentType = APPLICATION_JSON
            content = jsonUserEntity
        }
            .andExpect { status { isCreated() } }
            .andExpect { content { jsonPath("$.userName", Matchers.`is`("TestName")) } }
            .andExpect { content { jsonPath("$.userPassword", Matchers.`is`("pirate")) } }
    }

    @Test
    fun shouldUpdateUser(){
        val jsonUserEntity = JSONObject(mapOf("username" to "TestName", "password" to "pirate"))
        every { userService.updateUser(any(), any()) } answers { UserEntity(userName = "TestName", userPassword = "pirate") }
        mockMvc.put("/api/user/update/1"){
            contentType = APPLICATION_JSON
            content = jsonUserEntity
        }
            .andExpect { status { isOk() } }
            .andExpect { content { jsonPath("$.userName", Matchers.`is`("TestName")) } }
            .andExpect { content { jsonPath("$.userPassword", Matchers.`is`("pirate")) } }

        every { userService.updateUser(any(), any()) } answers { null }
        mockMvc.put("/api/user/update/1"){
            contentType = APPLICATION_JSON
            content = jsonUserEntity
        }
            .andExpect { status { isBadRequest() } }
    }

    @Test
    fun shouldDeleteUser(){
        every { userService.deleteUser(any()) } answers { true }
        mockMvc.delete("/api/user/delete/1"){}
            .andExpect { status { isOk() } }
            .andExpect { content { string("true") } }
        every { userService.deleteUser(any()) } answers { false }
        mockMvc.delete("/api/user/delete/1"){}
            .andExpect { status { isBadRequest() } }
            .andExpect { content { string("false") } }


    }

    //AUTHORITIES
    @Test
    fun shouldGetAuthorities(){
        every { userService.getAuthorities() } answers {
            mutableListOf(
                AuthorityEntity(id = 1, authorityName = "auth1"),
                AuthorityEntity(id = 2, authorityName = "auth2")
            )
        }
        mockMvc.get("/api/user/authority/all"){}
            .andExpect { status { isOk() } }
            .andExpect { content { jsonPath("$[0].authorityName", `is`("auth1")) } }
            .andExpect { content { jsonPath("$[1].authorityName", `is`("auth2")) } }
    }

    @Test
    fun shouldCreateAuthority(){
        val jsonAuthEntity = JSONObject(mapOf("authorityName" to "auth1"))
        every { userService.createAuthority(any()) } answers { AuthorityEntity(1, "auth1") }
        mockMvc.post("/api/user/authority/create"){
            contentType = APPLICATION_JSON
            content = jsonAuthEntity
        }
            .andExpect { status { isCreated() } }
            .andExpect { content { jsonPath("$.authorityName", Matchers.`is`("auth1")) } }
    }

}