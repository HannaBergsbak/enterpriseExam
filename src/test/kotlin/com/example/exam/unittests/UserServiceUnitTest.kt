package com.example.exam.unittests

import com.example.exam.dtos.LoginInfo
import com.example.exam.model.AuthorityEntity
import com.example.exam.model.UserEntity
import com.example.exam.repo.AuthorityRepo
import com.example.exam.repo.UserRepo
import com.example.exam.service.UserService
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class UserServiceUnitTest {
    private val userRepo = mockk<UserRepo>()
    private val authorityRepo = mockk<AuthorityRepo>()
    private val userService = UserService(userRepo, authorityRepo)

    private val testUserWithoutId = UserEntity(userName = "jim", userPassword = "bob")
    @Test
    fun shouldGetAllUsers(){
        val userBob = UserEntity(userName = "Bob", userPassword = "pirate")
        val userBen = UserEntity(userName = "Ben", userPassword = "mermaid")

        every { userRepo.findAll() } answers {
            mutableListOf(userBob, userBen)
        }

        val users = userService.getUsers()
        assert(users.size == 2)
        assert(users.first { it.userName == "Bob" }.userPassword == "pirate")
    }

    @Test
    fun shouldCreateUser(){
        every { userRepo.save(any()) } answers {
            firstArg()
        }

        every { authorityRepo.getByAuthorityName(any()) } answers {
            AuthorityEntity(authorityName = "USER")
        }

        val createdUser = userService.registerUser(LoginInfo("Dude", "qwerty"))
        assert(createdUser?.userName == "Dude")
    }

    @Test
    fun shouldUpdateUser(){
        every { userRepo.save(any()) } answers { firstArg() }
        every { userRepo.existsById(any()) } answers { true }

        val updatedUser = userService.updateUser(LoginInfo("TestUser", "qwerty"), 1)

        assert(updatedUser?.userName == "TestUser")
        assert(updatedUser?.id == 1.toLong())

        every { userRepo.existsById(any()) } answers { false }

        val shouldBeNull = userService.updateUser(LoginInfo("TestUser", "qwerty"), 1)
        assert(shouldBeNull == null)
    }

    @Test
    fun shouldDeleteUser(){
        every { userRepo.deleteById(any()) } answers {}
        every { userRepo.existsById(any()) } answers { true }
        assert(userService.deleteUser(1))
        every { userRepo.existsById(any()) } answers { false }
        assert(!userService.deleteUser(1))
    }

    @Test
    fun shouldGetAuthorities(){
        val auth1 = AuthorityEntity(authorityName = "Authority1")
        val auth2 = AuthorityEntity(authorityName = "Authority2")

        every { authorityRepo.findAll() } answers {
            mutableListOf(auth1, auth2)
        }

        val authorities = userService.getAuthorities()
        assert(authorities.size == 2)
        assert(authorities.first().authorityName == "Authority1")
    }

    /*
    @Test
    fun shouldAddAuthorityToUser(){
        every { authorityRepo.save(any()) } answers {
            firstArg()
        }

        val userBob = UserEntity(userName = "Bob", userPassword = "pirate")
        val authorityEntity = userService.createAuthority(AuthorityEntity(1, "Auth1"))
        val userWithAuthority = userService.grantUserAuthority("Bob", "Auth2")


    }
    */
}