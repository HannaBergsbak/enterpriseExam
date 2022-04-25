package com.example.exam.security.filter

import com.example.exam.BaseEndpoints
import com.example.exam.security.jwt.JwtUtil
import org.springframework.http.HttpStatus.FORBIDDEN
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthorizationFilter: OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = request.cookies?.firstOrNull{ it.name == "access_token"}?.value
        when{
            token.isNullOrEmpty() -> filterChain.doFilter(request, response)
            request.servletPath.contains("/api${BaseEndpoints.AUTHENTICATION}") -> filterChain.doFilter(request, response)
            request.servletPath.contains("/api${BaseEndpoints.USER_CREATE}") -> filterChain.doFilter(request, response)
            else -> {
                try {
                    val decodedJwt = JwtUtil.decodeToken(token)
                    val username = decodedJwt.subject
                    val authority = decodedJwt.getClaim("authorities").asList(String::class.java).map { SimpleGrantedAuthority(it) }
                    val authenticationToken = UsernamePasswordAuthenticationToken(username, null, authority)
                    SecurityContextHolder.getContext().authentication = authenticationToken
                    filterChain.doFilter(request, response)
                } catch (e: Exception){
                    logger.error("Authorization error ${e.message}")
                    val error = mapOf("error_message" to e.message)
                    response.contentType = APPLICATION_JSON_VALUE
                    response.status = FORBIDDEN.value()
                    jacksonObjectMapper().writeValue(response.outputStream, error)
                }

            }
        }
    }

}