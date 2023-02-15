package com.sk.project7.config.security

class AuthenticationException(message: String): Exception(message) {
    constructor(): this("Authentication Exception")
}