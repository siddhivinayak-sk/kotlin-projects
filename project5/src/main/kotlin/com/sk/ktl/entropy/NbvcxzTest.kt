package com.sk.ktl.entropy

import me.gosimple.nbvcxz.Nbvcxz
import me.gosimple.nbvcxz.resources.Configuration
import me.gosimple.nbvcxz.resources.ConfigurationBuilder
import me.gosimple.nbvcxz.resources.DictionaryBuilder
import java.util.Locale
import me.gosimple.nbvcxz.scoring.Result

val dictionaryList = ConfigurationBuilder.getDefaultDictionaries()
        .also {
            val directory = DictionaryBuilder().setDictionaryName("exclude").setExclusion(true).addWord("xyz", 0).createDictionary()
            it.add(directory)
        }
val configuration: Configuration = ConfigurationBuilder()
        .setLocale(Locale.forLanguageTag("en"))
        .setMinimumEntropy(30.0)
        .setDictionaries(dictionaryList)
        .createConfiguration()
val nbvcxz = Nbvcxz(configuration)

fun estimatePasswordStrength(password: String): Result = nbvcxz.estimate(password)

fun Result.messageWhenEntropyNotMet(): String? {
    return if (!isMinimumEntropyMet) {
        feedback.let {
            val messages = StringBuilder()
            it.warning?.let { warning -> messages.append("Warn: $warning").append(". \n") }
            it.suggestion.forEach { msg -> messages.append(msg).append(". \n") }
            messages.toString()
        }
    } else {
        null
    }
}

fun Result.printIt() {
    //println("===============Password: $password================")
    println("Password $password, Entropy: ${entropy}, Score: ${basicScore}, isEntropyMet: ${isMinimumEntropyMet}")
    //messageWhenEntropyNotMet()?.let { println(it) }
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
    testStrings.forEach {
        estimatePasswordStrength(it).printIt()
    }
}