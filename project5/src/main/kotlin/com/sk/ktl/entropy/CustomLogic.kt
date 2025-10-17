package com.sk.ktl.entropy


import kotlin.math.log2

fun isHex(s: String): Boolean {
    // Checks if all characters are hex digits and length is even
    return s.matches(Regex("^[0-9a-fA-F]+$")) && s.length % 2 == 0
}

fun isBase64(s: String): Boolean {
    // Checks if string matches base64 pattern (simple version)
    return s.matches(Regex("^[A-Za-z0-9+/]+={0,2}$")) && s.length % 4 == 0
}

fun hasDigit(s: String): Boolean = s.any { it.isDigit() }
fun hasLower(s: String): Boolean = s.any { it.isLowerCase() }
fun hasUpper(s: String): Boolean = s.any { it.isUpperCase() }
fun hasSpecial(s: String): Boolean = s.any { !it.isLetterOrDigit() }

fun calculateEntropyBits(s: String): Double {
    val L = s.length
    val N = when {
        isHex(s) -> 16
        isBase64(s) -> 64
        else -> {
            var n = 0
            if (hasDigit(s)) n += 10
            if (hasLower(s)) n += 26
            if (hasUpper(s)) n += 26
            if (hasSpecial(s)) n += 32 // "safe"
            n
        }
    }
    return if (N > 0) L * log2(N.toDouble()) else 0.0
}

fun main(args: Array<String>) {
    val testStrings = listOf(
            "abc123",
            "ABCdef!@#",
            "a1B2c3D4$%",
            "4d5e6f7g8h9i0j",
            "P@ssw0rd!",
            "c#be%F5TInv2C6_[",
            "48656c6c6f576f726c64", // Hex for "HelloWorld"
            "SGVsbG9Xb3JsZA==",     // Base64 for "HelloWorld"
            "short",
            "L0ng3rP@ssw0rd2024!",
            "W5WNNGH5WR5/OnaXdYW2OhL1ad2A5vcCi55nwYikkWY="
    )

    for (s in testStrings) {
        val entropy = calculateEntropyBits(s)
        println("String: '$s' | Entropy: ${"%.2f".format(entropy)} bits")
    }
}
