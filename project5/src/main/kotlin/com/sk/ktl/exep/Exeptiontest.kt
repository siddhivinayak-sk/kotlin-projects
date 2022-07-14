package com.sk.ktl.exep

fun main(s:Array<String>) {

    var input = "1"

    //simple try and catch
    try {
        val number: Int = Integer.parseInt(input)
        println("Number is: $number")
    } catch(e:NumberFormatException) {
        println("Invalid number")
    }

    //try as expression
    var isNumber = try {
        Integer.parseInt(input)
        true
    } catch(e:NumberFormatException) {
        false
    }
    println(isNumber)


}


class Person(name: String) {}