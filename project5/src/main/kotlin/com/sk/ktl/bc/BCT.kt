package com.sk.ktl.bc

import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x500.X500NameStyle
import org.bouncycastle.asn1.x500.style.BCStyle
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import java.io.StringReader
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder
import java.security.Security
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.ContentVerifierProvider
import java.security.cert.X509Certificate
import java.util.Date

val BC_PROVIDER = "BC"

val x509CertRoot = """
-----BEGIN CERTIFICATE-----
MIIFVzCCAz+gAwIBAgINAgPlk28xsBNJiGuiFzANBgkqhkiG9w0BAQwFADBHMQsw
CQYDVQQGEwJVUzEiMCAGA1UEChMZR29vZ2xlIFRydXN0IFNlcnZpY2VzIExMQzEU
MBIGA1UEAxMLR1RTIFJvb3QgUjEwHhcNMTYwNjIyMDAwMDAwWhcNMzYwNjIyMDAw
MDAwWjBHMQswCQYDVQQGEwJVUzEiMCAGA1UEChMZR29vZ2xlIFRydXN0IFNlcnZp
Y2VzIExMQzEUMBIGA1UEAxMLR1RTIFJvb3QgUjEwggIiMA0GCSqGSIb3DQEBAQUA
A4ICDwAwggIKAoICAQC2EQKLHuOhd5s73L+UPreVp0A8of2C+X0yBoJx9vaMf/vo
27xqLpeXo4xL+Sv2sfnOhB2x+cWX3u+58qPpvBKJXqeqUqv4IyfLpLGcY9vXmX7w
Cl7raKb0xlpHDU0QM+NOsROjyBhsS+z8CZDfnWQpJSMHobTSPS5g4M/SCYe7zUjw
TcLCeoiKu7rPWRnWr4+wB7CeMfGCwcDfLqZtbBkOtdh+JhpFAz2weaSUKK0Pfybl
qAj+lug8aJRT7oM6iCsVlgmy4HqMLnXWnOunVmSPlk9orj2XwoSPwLxAwAtcvfaH
szVsrBhQf4TgTM2S0yDpM7xSma8ytSmzJSq0SPly4cpk9+aCEI3oncKKiPo4Zor8
Y/kB+Xj9e1x3+naH+uzfsQ55lVe0vSbv1gHR6xYKu44LtcXFilWr06zqkUspzBmk
MiVOKvFlRNACzqrOSbTqn3yDsEB750Orp2yjj32JgfpMpf/VjsPOS+C12LOORc92
wO1AK/1TD7Cn1TsNsYqiA94xrcx36m97PtbfkSIS5r762DL8EGMUUXLeXdYWk70p
aDPvOmbsB4om3xPXV2V4J95eSRQAogB/mqghtqmxlbCluQ0WEdrHbEg8QOB+DVrN
VjzRlwW5y0vtOUucxD/SVRNuJLDWcfr0wbrM7Rv1/oFB2ACYPTrIrnqYNxgFlQID
AQABo0IwQDAOBgNVHQ8BAf8EBAMCAYYwDwYDVR0TAQH/BAUwAwEB/zAdBgNVHQ4E
FgQU5K8rJnEaK0gnhS9SZizv8IkTcT4wDQYJKoZIhvcNAQEMBQADggIBAJ+qQibb
C5u+/x6Wki4+omVKapi6Ist9wTrYggoGxval3sBOh2Z5ofmmWJyq+bXmYOfg6LEe
QkEzCzc9zolwFcq1JKjPa7XSQCGYzyI0zzvFIoTgxQ6KfF2I5DUkzps+GlQebtuy
h6f88/qBVRRiClmpIgUxPoLW7ttXNLwzldMXG+gnoot7TiYaelpkttGsN/H9oPM4
7HLwEXWdyzRSjeZ2axfG34arJ45JK3VmgRAhpuo+9K4l/3wV3s6MJT/KYnAK9y8J
ZgfIPxz88NtFMN9iiMG1D53Dn0reWVlHxYciNuaCp+0KueIHoI17eko8cdLiA6Ef
MgfdG+RCzgwARWGAtQsgWSl4vflVy2PFPEz0tv/bal8xa5meLMFrUKTX5hgUvYU/
Z6tGn6D/Qqc6f1zLXbBwHSs09dR2CQzreExZBfMzQsNhFRAbd03OIozUhfJFfbdT
6u9AWpQKXCBfTkBdYiJ23//OYb2MI3jSNwLgjt7RETeJ9r/tSQdirpLsQBqvFAnZ
0E6yove+7u7Y/9waLd64NnHi/Hm3lCXRSHNboTXns5lndcEZOitHTtNCjv0xyBZm
2tIMPNuzjsmhDYAPexZ3FL//2wmUspO8IFgV6dtxQ/PeEMMA3KgqlbbC1j+Qa3bb
bP6MvPJwNQzcmRk13NfIRmPVNnGuV/u3gm3c
-----END CERTIFICATE-----
"""

val x509CertParent = """
-----BEGIN CERTIFICATE-----
MIIFCzCCAvOgAwIBAgIQf/AFoHxM3tEArZ1mpRB7mDANBgkqhkiG9w0BAQsFADBH
MQswCQYDVQQGEwJVUzEiMCAGA1UEChMZR29vZ2xlIFRydXN0IFNlcnZpY2VzIExM
QzEUMBIGA1UEAxMLR1RTIFJvb3QgUjEwHhcNMjMxMjEzMDkwMDAwWhcNMjkwMjIw
MTQwMDAwWjA7MQswCQYDVQQGEwJVUzEeMBwGA1UEChMVR29vZ2xlIFRydXN0IFNl
cnZpY2VzMQwwCgYDVQQDEwNXUjIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEK
AoIBAQCp/5x/RR5wqFOfytnlDd5GV1d9vI+aWqxG8YSau5HbyfsvAfuSCQAWXqAc
+MGr+XgvSszYhaLYWTwO0xj7sfUkDSbutltkdnwUxy96zqhMt/TZCPzfhyM1IKji
aeKMTj+xWfpgoh6zySBTGYLKNlNtYE3pAJH8do1cCA8Kwtzxc2vFE24KT3rC8gIc
LrRjg9ox9i11MLL7q8Ju26nADrn5Z9TDJVd06wW06Y613ijNzHoU5HEDy01hLmFX
xRmpC5iEGuh5KdmyjS//V2pm4M6rlagplmNwEmceOuHbsCFx13ye/aoXbv4r+zgX
FNFmp6+atXDMyGOBOozAKql2N87jAgMBAAGjgf4wgfswDgYDVR0PAQH/BAQDAgGG
MB0GA1UdJQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjASBgNVHRMBAf8ECDAGAQH/
AgEAMB0GA1UdDgQWBBTeGx7teRXUPjckwyG77DQ5bUKyMDAfBgNVHSMEGDAWgBTk
rysmcRorSCeFL1JmLO/wiRNxPjA0BggrBgEFBQcBAQQoMCYwJAYIKwYBBQUHMAKG
GGh0dHA6Ly9pLnBraS5nb29nL3IxLmNydDArBgNVHR8EJDAiMCCgHqAchhpodHRw
Oi8vYy5wa2kuZ29vZy9yL3IxLmNybDATBgNVHSAEDDAKMAgGBmeBDAECATANBgkq
hkiG9w0BAQsFAAOCAgEARXWL5R87RBOWGqtY8TXJbz3S0DNKhjO6V1FP7sQ02hYS
TL8Tnw3UVOlIecAwPJQl8hr0ujKUtjNyC4XuCRElNJThb0Lbgpt7fyqaqf9/qdLe
SiDLs/sDA7j4BwXaWZIvGEaYzq9yviQmsR4ATb0IrZNBRAq7x9UBhb+TV+PfdBJT
DhEl05vc3ssnbrPCuTNiOcLgNeFbpwkuGcuRKnZc8d/KI4RApW//mkHgte8y0YWu
ryUJ8GLFbsLIbjL9uNrizkqRSvOFVU6xddZIMy9vhNkSXJ/UcZhjJY1pXAprffJB
vei7j+Qi151lRehMCofa6WBmiA4fx+FOVsV2/7R6V2nyAiIJJkEd2nSi5SnzxJrl
Xdaqev3htytmOPvoKWa676ATL/hzfvDaQBEcXd2Ppvy+275W+DKcH0FBbX62xevG
iza3F4ydzxl6NJ8hk8R+dDXSqv1MbRT1ybB5W0k8878XSOjvmiYTDIfyc9acxVJr
Y/cykHipa+te1pOhv7wYPYtZ9orGBV5SGOJm4NrB3K1aJar0RfzxC3ikr7Dyc6Qw
qDTBU39CluVIQeuQRgwG3MuSxl7zRERDRilGoKb8uY45JzmxWuKxrfwT/478JuHU
/oTxUFqOl2stKnn7QGTq8z29W+GgBLCXSBxC9epaHM0myFH/FJlniXJfHeytWt0=
-----END CERTIFICATE-----
"""

val x509Cert = """
-----BEGIN CERTIFICATE-----
MIIEVjCCAz6gAwIBAgIQOabDZFkE/bIJLObcyjD3eTANBgkqhkiG9w0BAQsFADA7
MQswCQYDVQQGEwJVUzEeMBwGA1UEChMVR29vZ2xlIFRydXN0IFNlcnZpY2VzMQww
CgYDVQQDEwNXUjIwHhcNMjUxMjI5MTk1MzA5WhcNMjYwMzIzMTk1MzA4WjAZMRcw
FQYDVQQDEw53d3cuZ29vZ2xlLmNvbTBZMBMGByqGSM49AgEGCCqGSM49AwEHA0IA
BD1Mm4lMWn6uXf8pORYeXOLa3WahFXKA2E6dTf7KNoEgM+IavXky5UxpnOQ1K3XO
xgcyhkqIV5sq3QRydJXuIgijggJBMIICPTAOBgNVHQ8BAf8EBAMCB4AwEwYDVR0l
BAwwCgYIKwYBBQUHAwEwDAYDVR0TAQH/BAIwADAdBgNVHQ4EFgQUgkwN9hT9ELc/
fwXT4ldWS3+XahEwHwYDVR0jBBgwFoAU3hse7XkV1D43JMMhu+w0OW1CsjAwWAYI
KwYBBQUHAQEETDBKMCEGCCsGAQUFBzABhhVodHRwOi8vby5wa2kuZ29vZy93cjIw
JQYIKwYBBQUHMAKGGWh0dHA6Ly9pLnBraS5nb29nL3dyMi5jcnQwGQYDVR0RBBIw
EIIOd3d3Lmdvb2dsZS5jb20wEwYDVR0gBAwwCjAIBgZngQwBAgEwNgYDVR0fBC8w
LTAroCmgJ4YlaHR0cDovL2MucGtpLmdvb2cvd3IyLzc1cjRaeUEzdkEwLmNybDCC
AQQGCisGAQQB1nkCBAIEgfUEgfIA8AB3AJaXZL9VWJet90OHaDcIQnfp8DrV9qTz
Nm5GpD8PyqnGAAABm2vi5V0AAAQDAEgwRgIhAIgS9MWtBGWFgCHPNJp8ilo8wrr+
Qe+4rjrd/5XLUkP6AiEAz2fXyr2V4CxMWuKe/FZf2zIxg9AQJd0Cwan/6I8WoOIA
dQDRbqmlaAd+ZjWgPzel3bwDpTxBEhTUiBj16TGzI8uVBAAAAZtr4uYFAAAEAwBG
MEQCIB7NhbeDfjBWi27gm8eG2pPrBFTZiTTbvzt/WGMU1+c9AiAhqCXcYeWY9JLU
J1glvmVeib0cUheegrrtZEYFuKq4UzANBgkqhkiG9w0BAQsFAAOCAQEANpaUxWzI
cw9IczBXtb3yD64ry9EN0i37zcgxyrxNnNvpiYI5mtXice0e/TamjKiEWdm5dyah
WmU+4+KnufbgtftqvysCPOYcPNEIEFWufgAYMH1XW38sRzx+VSthzndG86oAd6Jf
IR42ASFEgH3qfQdbGL01J/2ZOn3r68q9LXekywqBV2Q7waENcxS5DnUZZuhoX5s3
UIzqHk7zjPref8LhqbUjdu/cbPSc3yp7QpuCfHjGwqIW9+71nIUeQftlvOrVqtGr
DMwQMponHKxhKlVTlE/f4eyb0NPl6e1mfj6T+/lFqflTtBP7vMYMKNWeQDMBWjKQ
QMpp4JA5T9dLjQ==
-----END CERTIFICATE-----
""".trimIndent()

val certRootCanonical = "CN=GTS Root R1, O=Google Trust Services LLC, C=US"
val certRootPrintable = "CN=GTS Root R1, O=Google Trust Services LLC, C=US"
val certParentCanonical = "CN=WR2,O=Google Trust Services,C=US"
val certParentPrintable = "CN=WR2, O=Google Trust Services, C=US"
val certCanonical = "CN=www.google.com"
val certPrintable = "CN=www.google.com"

fun String.pemToX509CertHolder(): Any {
    val reader = StringReader(this)
    val pemParser = PEMParser(reader)
    return pemParser.readObject()
}

fun X509CertificateHolder.toX509Certificate(): X509Certificate = JcaX509CertificateConverter().getCertificate(this)

fun X509CertificateHolder.subjectAsString() = subject.toString()

fun String.sdnToX500Name(style: X500NameStyle = BCStyle.INSTANCE) = X500Name(style, this)

fun isSignedBy(child: X509CertificateHolder, issuer: X509CertificateHolder): Boolean {
    val publicKey = issuer.toX509Certificate().publicKey
    val verifier: ContentVerifierProvider = JcaContentVerifierProviderBuilder().setProvider(BC_PROVIDER).build(publicKey)
    return child.isSignatureValid(verifier)
}

fun isSignedBy2(child: X509CertificateHolder, issuer: X509CertificateHolder): Boolean {
    val verifierProvider = JcaContentVerifierProviderBuilder().build(issuer.subjectPublicKeyInfo)
    return child.isSignatureValid(verifierProvider)
}

fun isValidOn(cert: X509CertificateHolder, now: Date = Date()): Boolean {
    return cert.isValidOn(now)
}

fun isSelfSinged(cert: X509CertificateHolder): Boolean {
    return cert.issuer == cert.subject
}

fun main(args: Array<String>) {
    Security.addProvider(BouncyCastleProvider())

    val x509CertRootHolder = x509CertRoot.pemToX509CertHolder() as X509CertificateHolder
    val x509CertParentHolder = x509CertParent.pemToX509CertHolder() as X509CertificateHolder
    val x509CertHolder = x509Cert.pemToX509CertHolder() as X509CertificateHolder

    val subjectFromCert = x509CertHolder.subjectAsString()
    println("Subject from Cert: $subjectFromCert")

    println("X500Name matches subject from cert (same): ${certRootPrintable.sdnToX500Name() == x509CertRootHolder.subject}")
    println("X500Name matches subject from cert (same): ${certRootCanonical.sdnToX500Name() == x509CertParentHolder.subject}")
    println("X500Name matches subject from cert (same): ${certParentPrintable.sdnToX500Name() == x509CertHolder.subject}")
    println("X500Name matches subject from cert (same): ${certParentCanonical.sdnToX500Name() == x509CertRootHolder.subject}")
    println("X500Name matches subject from cert (same): ${certPrintable.sdnToX500Name() == x509CertParentHolder.subject}")
    println("X500Name matches subject from cert (same): ${certCanonical.sdnToX500Name() == x509CertHolder.subject}")

    println("Is cert self-signed - false: ${isSelfSinged(x509CertHolder)}")
    println("Is cert self-signed - true: ${isSelfSinged(x509CertRootHolder)}")

    println("Is cert signed by ca (false): ${isSignedBy(x509CertHolder, x509CertRootHolder)}")
    println("Is cert signed by ca (true): ${isSignedBy(x509CertHolder, x509CertParentHolder)}")
    println("Is cert signed by2 ca (true): ${isSignedBy2(x509CertHolder, x509CertParentHolder)}")

    println("Is Valid (today): ${isValidOn(x509CertHolder)}")
}
