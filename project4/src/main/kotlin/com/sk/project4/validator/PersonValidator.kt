package com.sk.project4.validator

import org.springframework.validation.Errors
import org.springframework.validation.Validator

class PersonValidator: Validator {
    override fun supports(clazz: Class<*>): Boolean {
        return false
    }

    override fun validate(target: Any, errors: Errors) {

    }
}