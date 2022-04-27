package com.example.exam.integrationtests

import com.example.exam.dtos.NewUserInfo
import com.example.exam.service.UserService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc(addFilters = true)
class UserServiceIntegrationTests(@Autowired private val userService: UserService) {

    @Test
    fun shouldGetUsers(){
        val result = userService.getUsers()
        assert(result.size == 1)
    }

    @Test
    fun registerAndFindUser(){
        val newUserInfo = NewUserInfo("Jim", "jim123")
        val createdUser = userService.registerUser(newUserInfo)
        assert(createdUser?.userName == "Jim")
        val foundUser = userService.loadUserByUsername("Jim")
        assert(createdUser?.userName == foundUser.username)
    }


}