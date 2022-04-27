package com.example.exam.integrationtests

import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc(addFilters = true)
class SecurityIntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun shouldLogIn(){
        mockMvc.post("/api/authentication"){
            contentType = APPLICATION_JSON
            content = JSONObject(mapOf("userName" to "Name", "password" to "pirate"))
        }
            .andExpect { cookie { exists("access_token") } }
    }

    @Test
    fun shouldRedirectToLogin(){
        mockMvc.get("api/user/all"){}
            .andExpect { status { is3xxRedirection() } }
    }
}