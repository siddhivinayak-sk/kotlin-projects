package com.sk.ktl.nimbus

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.jose.CompressionAlgorithm.DEF
import com.nimbusds.jose.EncryptionMethod
import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWEAlgorithm
import com.nimbusds.jose.JWEHeader
import com.nimbusds.jose.JWEObject
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.DirectDecrypter
import com.nimbusds.jose.crypto.DirectEncrypter
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.proc.JWSVerificationKeySelector
import com.nimbusds.jose.proc.SimpleSecurityContext
import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import com.nimbusds.jwt.proc.DefaultJWTProcessor
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import java.io.FileInputStream
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.interfaces.RSAPrivateCrtKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPublicKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.spec.SecretKeySpec

const val ALG = "RSA"
val ENC_METHOD: EncryptionMethod = EncryptionMethod.A128CBC_HS256
val JWS_ALG: JWSAlgorithm = JWSAlgorithm.RS256
val BASE64_DECODER: Base64.Decoder = Base64.getDecoder()
val objectMapper = ObjectMapper()

fun createToken(configFile: String, tokenData: String, environment: String, kid: String?): String {
    val config = parseYaml(configFile, environment)
    val claims = createClaims(tokenData)
    val jwtToken = serializedSignedJwt(config, claims, kid)
    println("Jwt Token: ")
    println(jwtToken)
    println("")
    val encryptedToken = jweEncrypt(config, jwtToken, kid)
    println("JWE Token: ")
    println(encryptedToken)
    println("")
    tokenInfo(configFile, encryptedToken!!, true, environment)
    return jwtToken
}

fun tokenInfo(configFile: String, token: String, isJwe: Boolean, environment: String) {
    val config = parseYaml(configFile, environment)
    println("Environment: ${config.env}")
    val jwtToken = if(isJwe)  jweDecrypt(config, token) else token
    jwtToken.run {
        val jwt = jwtParse(config, jwtToken)
        println("Is Valid: ${jwt.valid} ${if (!jwt.valid) jwt.validationFailMessage else ""}")
        println("Headers: ")
        println(jwt.headers)
        println("ClaimSet: ")
        println(jwt.jwtClaimsSet)
    }
}

fun parseYaml(file: String, environment: String): ConfigDto {
    val options = LoaderOptions()
    val yaml = Yaml(Constructor(ConfigDto::class.java, options))
    val allConfigs = yaml.loadAll(FileInputStream(file))
    val first = allConfigs.first() as ConfigDto
    return allConfigs.map { it as ConfigDto }.filter { it.env == environment }.getOrElse(0) { first }
}

fun keyFactory():KeyFactory = KeyFactory.getInstance(ALG)

fun rsaPublicKey(configDto: ConfigDto, keyFactory: KeyFactory): RSAPublicKey {
    val base64 = base64Decoder(configDto.jwe?.public?.key!!)
    val spec = X509EncodedKeySpec(base64)
    return keyFactory.generatePublic(spec) as RSAPublicKey
}

fun rsaPrivateKey(configDto: ConfigDto, keyFactory: KeyFactory): PrivateKey =
        rsaPrivateKey(configDto.jwe?.private?.key!!, keyFactory)

fun rsaPrivateKey(privateKey: String, keyFactory: KeyFactory): PrivateKey =
        keyFactory.generatePrivate(PKCS8EncodedKeySpec(base64Decoder(privateKey)))

fun rsaPKey(publicKey: RSAPublicKey): RSAKey = RSAKey.Builder(publicKey).build()

fun jwkSource(rsaPKey: RSAKey) = ImmutableJWKSet<SimpleSecurityContext>(JWKSet(rsaPKey))

fun jwsVerificationKeySelector(jwkSource:  ImmutableJWKSet<SimpleSecurityContext>) = JWSVerificationKeySelector(JWS_ALG, jwkSource)

fun jwtProcessor(jwsKeySelectorObj:  JWSVerificationKeySelector<SimpleSecurityContext>) =  DefaultJWTProcessor<SimpleSecurityContext>()
        .apply { jwsKeySelector = jwsKeySelectorObj }


fun signedJWT(claims: Map<String, Any?>, rsaPrivateKey: PrivateKey, keyId: String?): SignedJWT {
    val header = JWSHeader.Builder(JWSAlgorithm.RS256)
            .type(JOSEObjectType.JWT)
            .apply { keyId?.let { keyID(keyId) } }
            .build()
    val payload = JWTClaimsSet.Builder()
            .apply { claims.filterValues { it != null }
                    .forEach { claim(it.key, it.value) } }
            .build()
    return SignedJWT(header, payload).apply { sign(RSASSASigner(rsaPrivateKey)) }
}

fun serializedSignedJwt(configDto: ConfigDto, claims: Map<String, Any?>, kid: String?): String {
    val keyFactory = keyFactory()
    val rsaPrivateKey = rsaPrivateKey(configDto, keyFactory)
    val signedJWT = signedJWT(claims, rsaPrivateKey, kid)
    return signedJWT.serialize()
}

fun verifyTokenSignature(configDto: ConfigDto, token: String): JWTClaimsSet {
    val keyFactory = keyFactory()
    val rsaPublicKey = rsaPublicKey(configDto, keyFactory)
    val rsaPKey = rsaPKey(rsaPublicKey)
    val jwkSource = jwkSource(rsaPKey)
    val jwsVerificationKeySelector = jwsVerificationKeySelector(jwkSource)
    val jwtProcessor = jwtProcessor(jwsVerificationKeySelector)
    return jwtProcessor.process(token, SimpleSecurityContext())
}

fun jwtParse(configDto: ConfigDto, token: String): Jwt {
    val jwt = JWTParser.parse(token)
    val headers = jwt.header.toJSONObject()
    var errorMessage: String? = null
    val valid = runCatching { verifyTokenSignature(configDto, token) }.onFailure {
        errorMessage = it.message
    }.isSuccess
    return Jwt(jwt, headers, jwt.jwtClaimsSet, valid, errorMessage)
}

fun keySpec(configDto: ConfigDto) = SecretKeySpec(base64Decoder(configDto.jwe?.encryption?.key!!), ENC_METHOD.name)

fun decrypter(keySpec: SecretKeySpec) = DirectDecrypter(keySpec)

fun encrypter(keySpec: SecretKeySpec) = DirectEncrypter(keySpec)

fun jweEncrypt(configDto: ConfigDto, plainJson: String, kid: String?): String? {
    val keySpec = keySpec(configDto)
    val encrypter = encrypter(keySpec)
    val jweHeader = JWEHeader.Builder(JWEAlgorithm.DIR, ENC_METHOD).apply {
            if (kid != null) {
                keyID(kid)
            }
            if (configDto.jwe!!.compression) {
                compressionAlgorithm(DEF)
            }
        }
        .build()
    return JWEObject(jweHeader, Payload(JWSObject.parse(plainJson)))
            .apply { encrypt(encrypter) }
            .serialize()
}

fun jweDecrypt(configDto: ConfigDto, encryptedToken: String): String {
    val keySpec = keySpec(configDto)
    val decrypter = decrypter(keySpec)
    return JWEObject.parse(encryptedToken).apply { decrypt(decrypter) }.payload.toString()
}

fun createClaims(claimsString: String): Map<String, Any> =
        objectMapper.readValue(claimsString, object: TypeReference<Map<String, Any>>() {})

fun base64Decoder(text: String): ByteArray {
    return try {
        BASE64_DECODER.decode(text)
    } catch(ex: Exception) {
        com.nimbusds.jose.util.Base64(text).decode()
    }
}

open class Dto
class EncryptionDto: Dto() { var key: String? = null }
class PrivateDto: Dto() { var key: String? = null }
class PublicDto: Dto() {var key: String? = null }
class JweDto: Dto() {
    var encryption: EncryptionDto? = null
    var private: PrivateDto? = null
    var public: PublicDto? = null
    var compression: Boolean = false
}
class ConfigDto: Dto() {
    var jwe: JweDto? = null
    var env: String? = null
}
data class Jwt(val jwt: JWT, val headers: Map<String, Any>, val jwtClaimsSet: JWTClaimsSet, val valid: Boolean, val validationFailMessage: String? = null)

fun publicKeyFromPrivateKey(configFile: String, environment: String) {
    val config = parseYaml(configFile, environment)
    val privateKey: String = config.jwe?.private?.key!!
    val keyFactory = keyFactory()
    val simplePrivateKey = rsaPrivateKey(privateKey, keyFactory)
    val rsaPrivateKey: RSAPrivateCrtKey = simplePrivateKey as RSAPrivateCrtKey

    val publicKeySpec = RSAPublicKeySpec(rsaPrivateKey.modulus, rsaPrivateKey.publicExponent)
    val myPublicKey = keyFactory.generatePublic(publicKeySpec)
    println("Public Key: " + Base64.getEncoder().encodeToString(myPublicKey.encoded))
}

fun readSource(file: String): String {
    return FileInputStream(file).use { inputStream ->
        inputStream.bufferedReader().use { it.readText() }
    }
}