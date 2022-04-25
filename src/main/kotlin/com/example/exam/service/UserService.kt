package com.example.exam.service

import com.example.exam.dtos.NewUserInfo
import com.example.exam.model.AuthorityEntity
import com.example.exam.model.UserEntity
import com.example.exam.repo.AuthorityRepo
import com.example.exam.repo.UserRepo
import com.example.exam.security.filter.LoginInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(@Autowired private val userRepo: UserRepo, @Autowired private val authorityRepo: AuthorityRepo): UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        username?.let { it ->
            val user = userRepo.findByUserName(it)
            return User(user?.userName, user?.userPassword, user?.authorities?.map { SimpleGrantedAuthority(it.authorityName) })
        }
        throw Exception("NO GOOD")
    }

    fun getUsers(): List<UserEntity>{
        return userRepo.findAll()
    }

    fun registerUser(newUserInfo: NewUserInfo): UserEntity{
        val newUser = UserEntity(userName = newUserInfo.name, userPassword = BCryptPasswordEncoder().encode(newUserInfo.password))
        newUser.authorities.add(getAuthority("USER"))
        return userRepo.save(newUser)
    }

    fun updateUser(user: LoginInfo){
        val oldUser = userRepo.findByUserName(user.username)
        userRepo.save(UserEntity(id = oldUser!!.id, userName = user.username, userPassword = user.password))
    }

    fun createAuthority(authority: AuthorityEntity): AuthorityEntity? {
        val newAuth = AuthorityEntity(authorityName = authority.authorityName)
        return authorityRepo.save(newAuth)
    }

    fun getAuthorities(): List<AuthorityEntity>{
        return authorityRepo.findAll()
    }

    fun grantUserAuthority(username: String?, authorityName: String?){
        val user = username?. let { userRepo.findByUserName(it) }
        val authority = authorityName?. let { authorityRepo.getByAuthorityName(it) }
        user?.let { u ->
            authority?.let { a -> u.authorities.add(a) }
        }
    }
    fun getAuthority(name: String): AuthorityEntity {
        return authorityRepo.getByAuthorityName(name)
    }



}