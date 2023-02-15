package com.sk.project7.service

import com.sk.project7.config.security.AuthenticationException
import com.sk.project7.config.security.support.PBKDF2Encoder
import com.sk.project7.repos.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.Base64
import java.util.Date
import java.util.UUID

@Service
class CustomUserDetailsService: UserDetailsService {

    @Value("\${jwt.secret}")
    private val secret: String = ""

    @Value("\${jwt.expiration}")
    private val defaultExpirationTimeInSecondsConf: String? = ""

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var encoder: PBKDF2Encoder

    override fun loadUserByUsername(username: String?): User {
        var user =  userRepository.findByName(username)
                .orElseThrow { UsernameNotFoundException("User not found with username: $username") }
                return User(user.name, user.password, true, true, true, true,
                            listOf(SimpleGrantedAuthority(user.role))
                )
    }

    fun authenticate(username: String, password: String?): Mono<TokenInfo> {
        return loadUserByUsername(username)
                .toMono()
                .flatMap { user ->
                    if (!user.isEnabled)
                        return@flatMap Mono.error(AuthenticationException("Account disabled.: USER_ACCOUNT_DISABLED"))
                    if (encoder.encode(password) != user.password)
                        return@flatMap Mono.error(AuthenticationException("Invalid user password!: INVALID_USER_PASSWORD")
                        )
                    Mono.just(generateAccessToken(user))
                }
                .switchIfEmpty(Mono.error(AuthenticationException("Invalid user, $username is not registered.: INVALID_USERNAME")))
    }

    fun createUser(username: String, password: String, role: String): Mono<com.sk.project7.entity.User> {
        return Mono.just(com.sk.project7.entity.User())
                .map {
                    it.name = username
                    it.password = encoder.encode(password)
                    it.role = role
                    it
                }
                .map { userRepository.save(it) }
    }

    fun generateAccessToken(user: User): TokenInfo {
        val claims = mutableMapOf("role" to user.authorities.random().authority)
        return doGenerateToken(claims, user.username, user.username)
    }

    private fun doGenerateToken(claims: Map<String, Any>, issuer: String, subject: String): TokenInfo {
        val expirationTimeInMilliseconds = defaultExpirationTimeInSecondsConf!!.toLong() * 1000
        val expirationDate = Date(Date().time + expirationTimeInMilliseconds)
        return doGenerateToken(expirationDate, claims, issuer, subject)
    }

    private fun doGenerateToken(expirationDate: Date, claims: Map<String, Any>, issuer: String, subject: String): TokenInfo {
        val createdDate = Date()
        val token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.toByteArray()))
                .compact()
        return TokenInfo(0L, token, createdDate, expirationDate)
    }
}

data class TokenInfo(val userId: Long, val token: String, val issuedAt: Date, val expiresAt: Date)