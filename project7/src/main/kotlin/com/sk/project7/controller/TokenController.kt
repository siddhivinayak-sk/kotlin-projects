package com.sk.project7.controller

import com.sk.project7.entity.User
import com.sk.project7.service.CustomUserDetailsService
import com.sk.project7.service.TokenInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/authenticate")
class TokenController {

    @Autowired
    lateinit var userDetailsService: CustomUserDetailsService

    @GetMapping
    fun token(@RequestParam username: String, @RequestParam password: String): Mono<TokenInfo> {
        return userDetailsService.authenticate(username, password)
    }

    @GetMapping("/create")
    fun create(@RequestParam username: String, @RequestParam password: String): Mono<User> {
        return userDetailsService.createUser(username, password, "ROLE_USER")
    }
}