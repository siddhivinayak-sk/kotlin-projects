package com.sk.ktl.nimbus

/**
env: test
jwe:
  encryption:
    key: <some key AES256>
  private:
    key: <some private key>
  public:
    key: <some public key>
---
*/

const val configFile = ""
const val sourceFile = ""
const val environment = "test"
const val generateToken = true
val kid = null
val source = readSource(sourceFile)

fun main(args: Array<String>) {
    if(generateToken) {
        createToken(configFile, source, environment, kid)
    } else {
        tokenInfo(configFile, source, source.contains(".."), environment)
    }
    publicKeyFromPrivateKey(configFile, environment)
}
