package com.sk.project7.config.security.support

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

@Component
class PBKDF2Encoder: PasswordEncoder {

    @Value("\${jwt.password.encoder.secret}")
    var secret: String = ""

    @Value("\${jwt.password.encoder.iteration}")
    var iteration: Int = 0

    @Value("\${jwt.password.encoder.keylength}")
    var keylength: Int = 0

    override fun encode(rawPassword: CharSequence?): String {
        try {
            var result = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA512")
                    .generateSecret(PBEKeySpec(rawPassword.toString().toCharArray(), secret.toByteArray(), iteration, keylength))
                    .encoded
            return Base64.getEncoder().encodeToString(result)
        } catch (ex: NoSuchAlgorithmException) {
            throw RuntimeException(ex)
        } catch (ex: InvalidKeySpecException) {
            throw RuntimeException(ex)
        }
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        return encode(rawPassword) == encodedPassword
    }
}