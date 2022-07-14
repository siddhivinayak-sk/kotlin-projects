package com.sk.ktl.looping

import java.util.TreeMap


fun main(s:Array<String>) {
    var str1: String = "abc"
    var str2: String = String("abc".toCharArray())

    //Simple if
    if(str1 == str2) {
        println("Both Strings are same")
    }

    //If as expression
    var str3 = if(str1 == str2) "Both Strings are same" else "Both Strings are different"
    println(str3)

    //case
    var str4 = "A"
    var str5 = "B"
    when(str4) {
        str5 -> println("B")
        "C" -> println("C")
        else -> println("Not matched")
    }

    //do while loop
    var i: Int = 0
    do {
        println("Good Morning")
        i++
    } while (i != 5)

    //while loop
    while(i > 0) {
        println("Hello")
        i--;
    }

    //For loop
    var range = 1..5
    var charrange = 'a'..'z'
    for(i in range) {
        println("Index: $i")
    }

    //for with step
    for(i in range step 2) {
        println("Step Index: $i")
    }

    //backward range
    for(i in 10 downTo 1 step 2) {
        println("DownStepIndex: $i")
    }

    //Until
    for(i in 1 until 5) {
        println("UntilIndex: $i")
    }

    //List looping
    var numbers = listOf(1, 2, 3, 4, 5)
    for(i in numbers) {
        println("ListIndex: $i")
    }

    //Iterate with ordinal index
    for((index, element) in numbers.withIndex()) {
        println("Number: $element at $index")
    }

    //Map
    var ages = TreeMap<String, Int>()
    ages["John"] = 45
    ages["McJean"] = 41
    ages["Peter"] = 34
    for((name, age) in ages) {
        println("Name: $name and Age: $age")
    }

    for (x in charrange) {
        println("Char: $x")
    }

}






class Person2(var name:String) {}