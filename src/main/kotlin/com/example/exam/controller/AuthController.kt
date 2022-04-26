package com.example.exam.controller

import com.example.exam.BaseEndpoints
import com.example.exam.dtos.AuthorityToUser
import com.example.exam.dtos.NewUserInfo
import com.example.exam.model.AuthorityEntity
import com.example.exam.model.UserEntity
import com.example.exam.security.filter.LoginInfo
import com.example.exam.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.security.InvalidParameterException

@RestController
@RequestMapping("/api")
class AuthController(@Autowired private val userService: UserService) {

    //USERS
    @GetMapping("/user/all")
    fun getUsers(): ResponseEntity<List<UserEntity>>{
        return ResponseEntity.ok().body(userService.getUsers())
    }

    @PostMapping(BaseEndpoints.USER_CREATE)
    fun registerUser(@RequestBody newUserInfo: NewUserInfo): ResponseEntity<UserEntity>{
        val createdUser = userService.registerUser(newUserInfo)
        val uri = URI.create(
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user").toUriString())
        return ResponseEntity.created(uri).body(createdUser)
    }

    @PutMapping("/user/update/{userId}")
    fun updateUser(@PathVariable("userId") userId: Long, @RequestBody user: LoginInfo?): LoginInfo{
        when {
            userId == null -> throw InvalidParameterException()
            user == null -> throw InvalidParameterException()
            else -> {
                userService.updateUser(user, userId)?.let {
                    return user
                }
            }
        }
        throw UserNotFound()
    }

    @DeleteMapping("/user/delete/{userId}")
    fun deleteUser(@PathVariable("userId") userId: Long){
        when{
            userId == null -> throw InvalidParameterException()
            else -> {
                userService.deleteUser(userId)
            }
        }
        throw UserNotFound()
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

    @PostMapping("${BaseEndpoints.USER_AUTHORITY}/addToUser")
    fun addAuthorityToUser(@RequestBody authorityToUser: AuthorityToUser): ResponseEntity<Any>{
        userService.grantUserAuthority(authorityToUser.username, authorityToUser.authorityName)
        return ResponseEntity.ok().build()
    }
}

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found")
class UserNotFound: RuntimeException()



