package com.example.exam.security

import com.example.exam.BaseEndpoints
import com.example.exam.security.filter.CustomAuthenticationFilter
import com.example.exam.security.filter.CustomAuthorizationFilter
import com.example.exam.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@Configuration
class SecurityConfig(@Autowired private val userService: UserService, @Autowired private val passwordEncoder: BCryptPasswordEncoder): WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder)
    }

    override fun configure(http: HttpSecurity) {
        val authenticationFilter = CustomAuthenticationFilter(authenticationManagerBean())
        authenticationFilter.setFilterProcessesUrl("/api${BaseEndpoints.AUTHENTICATION}")
        http.csrf().disable()
        http.sessionManagement().disable()
        http.authorizeHttpRequests()
            .antMatchers("${BaseEndpoints.AUTHENTICATION}").permitAll()
            .antMatchers("${BaseEndpoints.USER_CREATE}").permitAll()
            .antMatchers("${BaseEndpoints.USER_AUTHORITY}/**").hasAuthority("ADMIN")
            .antMatchers("/api/user/**").hasAnyAuthority("USER", "ADMIN")
            .antMatchers(HttpMethod.GET, "api/shelter/**").hasAnyAuthority("USER", "ADMIN")
            .antMatchers("/api/shelter/**").hasAuthority("ADMIN")
            .and().formLogin()
        http.authorizeRequests().anyRequest().authenticated()
        http.addFilter(authenticationFilter)
        http.addFilterBefore(CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }


}