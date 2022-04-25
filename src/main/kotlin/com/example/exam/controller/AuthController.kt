package com.example.exam.controller

import com.example.exam.BaseEndpoints
import com.example.exam.dtos.AuthorityToUser
import com.example.exam.dtos.NewUserInfo
import com.example.exam.model.AuthorityEntity
import com.example.exam.model.UserEntity
import com.example.exam.security.filter.LoginInfo
import com.example.exam.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/api")
class AuthController(@Autowired private val userService: UserService) {

    @PostMapping(BaseEndpoints.USER_CREATE)
    fun registerUser(@RequestBody newUserInfo: NewUserInfo): ResponseEntity<UserEntity>{
        val createdUser = userService.registerUser(newUserInfo)
        val uri = URI.create(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user").toUriString())
        return ResponseEntity.created(uri).body(createdUser)
    }

    @GetMapping("/user/all")
    fun getUsers(): ResponseEntity<List<UserEntity>>{
        return ResponseEntity.ok().body(userService.getUsers())
    }

    @PostMapping("/user/{userId}")
    fun updateUser(@PathVariable("userId") userId: Long, @RequestBody user: LoginInfo){
        return userService.updateUser(user)
    }

    @DeleteMapping("/user/{userId}")
    fun deleteUser(@PathVariable("userId") userId: Long){@
    }

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

    @PostMapping("${BaseEndpoints.USER_AUTHORITY}/addToUser")
    fun addAuthorityToUser(@RequestBody authorityToUser: AuthorityToUser): ResponseEntity<Any>{
        userService.grantUserAuthority(authorityToUser.username, authorityToUser.authorityName)
        return ResponseEntity.ok().build()
    }

}



