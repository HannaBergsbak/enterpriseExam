package com.example.exam.integrationtests

import com.example.exam.service.UserService
import org.hamcrest.Matchers
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc(addFilters = false)
class UserServiceIntegrationTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userService: UserService

    @Test
    fun registerAndFindUser(){
        val jsonUserEntity = JSONObject(mapOf("username" to "TestName", "password" to "pirate"))
        mockMvc.post("/api/user/create"){
            contentType = APPLICATION_JSON
            content = jsonUserEntity
        }
            .andExpect { status { isCreated() } }
            .andExpect { content { jsonPath("$.userName", Matchers.`is`("TestName")) } }

        val storedUser = userService.loadUserByUsername("TestName")
        assert(storedUser.username == "TestName")
        assert(storedUser.password != "pirate")
    }
}