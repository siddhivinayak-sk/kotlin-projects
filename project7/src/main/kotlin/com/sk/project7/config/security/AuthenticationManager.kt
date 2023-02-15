package com.sk.project7.config.security

import com.sk.project7.entity.User
import com.sk.project7.service.CustomUserDetailsService
import com.sun.security.auth.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Component
class AuthenticationManager: ReactiveAuthenticationManager {

    @Autowired
    lateinit var userDetailService: CustomUserDetailsService

    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        var user = authentication?.principal as UserPrincipal
        return Mono.just(userDetailService.loadUserByUsername(user.name))
                .filter { it.isEnabled }
                .switchIfEmpty { Mono.error { AuthenticationException("User account is disabled.") } }
                .map { authentication }
    }
}