package com.sk.project7.config.security.support

import com.sun.security.auth.UserPrincipal
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import reactor.core.publisher.Mono
import java.util.stream.Collectors

class CurrentUserAuthenticationBearer {
    companion object {
        fun create(verificationResult: JwtVerifyHandler.VerificationResult): Mono<Authentication> {
            val claims = verificationResult.claims
            val subject = claims.subject
            val role = claims.get("role", String::class.java)
            val authority = SimpleGrantedAuthority("ROLE_$role")
            val principal = UserPrincipal(subject)
            return Mono.justOrEmpty(UsernamePasswordAuthenticationToken(principal, null, listOf(authority)))
        }
    }
}