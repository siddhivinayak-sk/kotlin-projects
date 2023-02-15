package com.sk.project7.config.security.support

import com.sk.project7.config.security.AuthenticationException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import reactor.core.publisher.Mono
import java.util.Base64
import java.util.Date

class JwtVerifyHandler(val secret: String) {

    fun check(token: String): Mono<VerificationResult> {
        return Mono.just<VerificationResult?>(verify(token))
                .onErrorResume { e -> Mono.error { AuthenticationException(e.message ?: "") } }
    }

    private fun verify(token: String): VerificationResult {
        var claims = getAllClaimsFromToken(token)
        var expiry = claims.expiration
        if(expiry.before(Date())) {
            throw RuntimeException("Token expired")
        } else {
            return VerificationResult(claims, token)
        }
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.toByteArray()))
                .parseClaimsJws(token)
                .body
    }

    class VerificationResult {
        lateinit var claims: Claims
        lateinit var token: String

        constructor(claims: Claims, token: String) {
            this.claims = claims
            this.token = token
        }
    }
}