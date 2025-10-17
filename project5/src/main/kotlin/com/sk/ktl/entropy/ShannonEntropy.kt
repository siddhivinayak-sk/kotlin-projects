package com.sk.ktl.entropy

import java.security.SecureRandom
import java.util.Base64
import kotlin.math.log2

class ShannonEntropy {

    fun generateRandomByteArrayOfGivenLength(length: Int): String {
        val random = SecureRandom()
        val bytes = ByteArray(length)
        random.nextBytes(bytes)
        return Base64.getEncoder().encodeToString(bytes)
    }

    fun generateRandomUpperCharsOfGivenLength(length: Int): String {
        val chars = ('A'..'Z').toList()
        val random = SecureRandom()
        return (1..length)
                .map { chars[random.nextInt(chars.size)] }
                .joinToString("")
    }

    fun generateRandomLowerCharsOfGivenLength(length: Int): String {
        val chars = ('a'..'z').toList()
        val random = SecureRandom()
        return (1..length)
                .map { chars[random.nextInt(chars.size)] }
                .joinToString("")
    }

    fun generateRandomNumericCharsOfGivenLength(length: Int): String {
        val chars = ('0'..'9').toList()
        val random = SecureRandom()
        return (1..length)
                .map { chars[random.nextInt(chars.size)] }
                .joinToString("")
    }

    fun generateRandomLowerAndUpperCharsOfGivenLength(length: Int): String {
        val chars = ('A'..'Z').toList() + ('a'..'z').toList()
        val random = SecureRandom()
        return (1..length)
                .map { chars[random.nextInt(chars.size)] }
                .joinToString("")
    }

    fun generateRandomLowerUpperAndNumericCharsOfGivenLength(length: Int): String {
        val chars = ('A'..'Z').toList() + ('a'..'z').toList() + ('0'..'9').toList()
        val random = SecureRandom()
        return (1..length)
                .map { chars[random.nextInt(chars.size)] }
                .joinToString("")
    }

    fun generateRandomLowerUpperNumAndKeyboardAndSpecialCharsOfGivenLength(length: Int): String {
        val chars = ('A'..'Z').toList() + ('a'..'z').toList() + ('0'..'9').toList() + listOf(
                '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+')
        val random = SecureRandom()
        return (1..length)
                .map { chars[random.nextInt(chars.size)] }
                .joinToString("")
    }

    fun shannonEntropy(input: String?): Double {
        if (input.isNullOrEmpty()) return 0.0
        return input.groupingBy { it }.eachCount().values
                .filter { it > 0 }.sumOf { count ->
                    val p = count / input.length.toDouble()
                    -p * log2(p)
                }
    }

    fun String.bitLength() = toByteArray(Charsets.UTF_8).size * 8

    fun bits(input: String) = input.bitLength()

    fun evaluateShannonEntropy(times: Int, length: Int, randomStringGenerator: (Int) -> String) {
        val entropyList = mutableListOf<Double>()
        for (i in 1..times) {
            val randomString = randomStringGenerator(length)
            val entropy = shannonEntropy(randomString)
            entropyList.add(entropy)
            println("Random String: $randomString, BitLength :${randomString.bitLength()}, Shannon Entropy: $entropy")
        }
        println("Random Length: $length chars (${length * 8}+ bits) + $randomStringGenerator")
        println("Number of Random Evaluated: ${entropyList.count()}")
        println("Average entropy: ${entropyList.average()}")
        println("Min entropy: ${entropyList.minOrNull()}")
        println("Max entropy: ${entropyList.maxOrNull()}")
        println("---------------------------------------------------------------------------------------------")
    }
}



fun main() {
    val se = ShannonEntropy()

//    se.evaluateShannonEntropy(1000, 16, se::generateRandomByteArrayOfGivenLength)
//    se.evaluateShannonEntropy(1000, 16, se::generateRandomUpperCharsOfGivenLength)
//    se.evaluateShannonEntropy(1000, 16, se::generateRandomLowerCharsOfGivenLength)
//    se.evaluateShannonEntropy(1000, 16, se::generateRandomNumericCharsOfGivenLength)  // 128 bits
//    se.evaluateShannonEntropy(1000, 16, se::generateRandomLowerAndUpperCharsOfGivenLength)  // 128 bits
//    se.evaluateShannonEntropy(1000, 16, se::generateRandomLowerUpperAndNumericCharsOfGivenLength)  // 128 bits
//    se.evaluateShannonEntropy(1000, 16, se::generateRandomLowerUpperNumAndKeyboardAndSpecialCharsOfGivenLength)  // 128 bits
    //se.evaluateShannonEntropy(1000, 32, se::generateRandomUpperCharsOfGivenLength)
    val text ="Q.kb,<w+=#}hYyT<,rje)v\$S:n]SbcpSecret@321"
    println("Entropy of $text : ${se.shannonEntropy(text)} and bit length: ${se.bits(text)}")
}
