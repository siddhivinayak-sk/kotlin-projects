package com.sk.project4.handler

import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.context.request.WebRequest
import kotlin.reflect.KClass
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class GlobalHandler {

    @ExceptionHandler(value = arrayOf<KClass<out Throwable>>(Exception::class))
    fun genericCustomException(request: WebRequest, ex: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity.badRequest().body(ErrorResponse("400", "Error"))
    }

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        dateFormat.isLenient = false
        binder.registerCustomEditor(Date::class.java, CustomDateEditor(dateFormat, false));
    }


//    @ModelAttribute
//    fun addAccount(@RequestParam number: String) {
//        val accountMono: Mono<Account> = accountRepository.findAccount(number)
//        model.addAttribute("account", accountMono);
//    }

    public class ErrorResponse {
        lateinit var code: String
        lateinit var message: String

        constructor():super() {
            code = ""
            message = ""
        }

        constructor(code: String, message: String):super() {
            this.code = code;
            this.message = message;
        }
    }


}