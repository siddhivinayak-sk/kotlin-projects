package com.sk.ktl.entropy

import org.passay.CharacterRule
import org.passay.DictionaryRule
import org.passay.EnglishCharacterData
import org.passay.LengthRule
import org.passay.PasswordData
import org.passay.PasswordValidator
import org.passay.RepeatCharacterRegexRule
import org.passay.RuleResult
import org.passay.WhitespaceRule
import org.passay.dictionary.ArrayWordList
import org.passay.dictionary.WordListDictionary

fun String.validatePasswordStrength(): RuleResult {
    val validator = PasswordValidator(
            LengthRule(16, 26),
            CharacterRule(EnglishCharacterData.LowerCase, 1),
            CharacterRule(EnglishCharacterData.UpperCase, 1),
            CharacterRule(EnglishCharacterData.Digit, 1),
            CharacterRule(EnglishCharacterData.Special, 1),
            WhitespaceRule(),
            RepeatCharacterRegexRule(3),
            DictionaryRule(WordListDictionary(ArrayWordList(arrayOf("abc")))),

    )
    return validator.validate(PasswordData(this))
}

fun RuleResult.printIt() {
    println("===============Password Validation Result================")
    println("Is valid: $isValid")
    details.forEach {
        println("Detail: ${it.errorCode}, ${it.parameters}")
    }
}

fun main(args: Array<String>) {
    "password".validatePasswordStrength().printIt()
    "mbcpx@1234567890".validatePasswordStrength().printIt()

}