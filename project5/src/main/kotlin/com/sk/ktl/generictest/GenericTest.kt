package com.sk.ktl.generictest

fun main(s:Array<String>) {
    var names = listOf("John", "McLine", "Bomber", "Jean", "Rodrick") //Type infer from argument

    val name = names.itemAt(1) //calling of generic method
    println(name)

    var node: Node<Int> = Node(9) //Generic type class variable
    println(node.value())

    printType(names) //It runs as type is passed List<String> which is as per casting
    //printType(listOf(1)) //It will throw runtime Exception: Exception in thread "main" java.lang.ClassCastException: class java.lang.Integer cannot be cast to class java.lang.String

}

//Generic function
fun <T> List<T>.itemAt(ndx: Int): T {
    return this[ndx]
}

//Generic class
class Node <T : Number> (private val item: T) {
    fun value(): T {
        return item
    }
}

fun printType(items: List<Any>) {
    //if(items is List<String>) {} //since generic info erased at runtime, it gives compilation error: Cannot check for instance of erased type: List<String>
    val names: List<String> = items as List<String> //Direct casting will give warning: Unchecked cast: List<Any> to List<String>
    names.filter { s -> s == " "}
}