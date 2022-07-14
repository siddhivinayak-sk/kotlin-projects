package com.sk.ktl.func

/**
 * High Order Function = A function that takes another function as argument
 */

fun main(s:Array<String>) {
    performAction(action1)

    val ints = listOf(1, 2, 3, 4, 5)
    val i = first(ints, {i -> i == 3})

    println(i)
}

//Normal function
fun action() { println("Hello World!")}
fun cal(x: Int, y: Int): Int {return x * y}

//Anonymous Function assigned to variable
var action1: () -> Unit = {println("Hello World!")}
var calc1: (x: Int, y: Int) -> Int = {x, y -> x * y}

//Anonymous Function assigned to variable with inferred type
var action2 = {println("Hello World!")}
var calc2 = {x:Int, y:Int -> x * y}

//High Order function - It can be mark as inline
fun performAction(perform: () -> Unit) {
    perform()
}

//High Order function directly using lambda function so it can be marked inline by using inline keyword
inline fun <T> first(items: List<T>, predicate: (t: T) -> Boolean) : T {
    for(item in items) {
        if(predicate(item)) return item
    }
    throw Exception()
}