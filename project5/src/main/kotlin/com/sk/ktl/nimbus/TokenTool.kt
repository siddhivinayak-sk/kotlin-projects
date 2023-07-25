package com.sk.ktl.nimbus

const val configFile = ""
const val environment = ""
const val generateToken = true
const val source = ""

fun main(args: Array<String>) {
    if(generateToken) {
        createToken(configFile, source, environment)
    } else {
        tokenInfo(configFile, source, source.contains(".."), environment)
    }
}
