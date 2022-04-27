package com.example.exam.controller

import com.example.exam.BaseEndpoints
import com.example.exam.dtos.LoginInfo
import com.example.exam.model.AuthorityEntity
import com.example.exam.model.UserEntity
import com.example.exam.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/api")
class AuthController(@Autowired private val userService: UserService) {

    //USERS
    @GetMapping("/user/all")
    fun getUsers(): ResponseEntity<List<UserEntity>>{
        return ResponseEntity.ok().body(userService.getUsers())
    }

    @PostMapping(BaseEndpoints.USER_CREATE)
    fun registerUser(@RequestBody loginInfo: LoginInfo): ResponseEntity<UserEntity>{
        val createdUser = userService.registerUser(loginInfo)
        val uri = URI.create(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user").toUriString())
        return ResponseEntity.created(uri).body(createdUser)
    }

    @PutMapping("/user/update/{userId}")
    fun updateUser(@PathVariable("userId") userId: Long, @RequestBody user: LoginInfo): ResponseEntity<UserEntity>{

        userService.updateUser(user, userId)?.let {
            return ResponseEntity.ok(it)
        }
        return ResponseEntity.badRequest().build()
    }

    @DeleteMapping("/user/delete/{userId}")
    fun deleteUser(@PathVariable("userId") userId: Long): ResponseEntity<Boolean> {
        if (userService.deleteUser(userId)){
            return ResponseEntity.ok(true)
        }
        return ResponseEntity.badRequest().body(false)
    }

    //AUTHORITIES
    @GetMapping("${BaseEndpoints.USER_AUTHORITY}/all")
    fun getAuthorities(): ResponseEntity<List<AuthorityEntity>>{
        return ResponseEntity.ok().body(userService.getAuthorities())
    }

    @PostMapping("${BaseEndpoints.USER_AUTHORITY}/create")
    fun createAuthority(@RequestBody authority: AuthorityEntity): ResponseEntity<AuthorityEntity>{
        val authorityEntity = userService.createAuthority(authority)
        val uri = URI.create(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("${BaseEndpoints.USER_AUTHORITY}/create").toUriString()
        )
        return ResponseEntity.created(uri).body(authorityEntity)
    }

    /*
    @PostMapping("${BaseEndpoints.USER_AUTHORITY}/addToUser")
    fun addAuthorityToUser(@RequestBody authorityToUser: AuthorityToUser): ResponseEntity<Any>{
        userService.grantUserAuthority(authorityToUser.username, authorityToUser.authorityName)
        return ResponseEntity.ok().build()
    }*/
}
/*
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found")
class UserNotFound: RuntimeException()
*/


