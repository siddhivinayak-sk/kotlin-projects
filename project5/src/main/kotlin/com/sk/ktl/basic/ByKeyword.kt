package com.sk.ktl.basic

import kotlin.reflect.KProperty

/**
 * This 'by' keyword can be used for delegation:
 *  - Delegation for property
 *    There is implicit getter for val and getter & setter for var keywords. Additionally, there could be a provider for
 *    these getter and setter methods in Kotlin's Delegate class.
 *    'by' keyword delegates provider to function specified by object name after by keyword for getter and setter.
 *
 *  - Delegation for class
 *    Suppose there is an interface which has some methods implemented by some class. Now, another class requires to
 *    implement same interface and need to implement functions with same definition implemented in previous class, the
 *    implementation can be delegated.
 *
 *  - Delegation for parameter
 *
 */

val number1: Int get() = random()
val number2: Int = random()
val e1 = Employee()

fun main(args: Array<String>) {
    println("N1: $number1")
    println("N2: $number2")
    //println(e1.name)
    e1.name = "XZ"
    println(e1.name)
}

fun random(): Int = Math.random().times(100).toInt()


class Employee {
    var myval = ""
    var name by Delegate() //Delegate for property using 'by'
}

class Delegate {
    operator fun getValue(ref: Any, property: KProperty<*>): Any {
        println("GetValue: REF$ref    PROP$property")
        return (ref as Employee).myval
    }

    operator fun setValue(ref: Any, property: KProperty<*>, any: Any) {
        println("SetValue: REF$ref    PROP$property     ANY$any")
        (ref as Employee).myval = any.toString()
    }
}

interface I {
    val value: String
    fun m1()
}

class A : I {
    override val value: String = "A"

    override fun m1() {
        println("Class A")
    }
}

class B(a: A): I by a { //Class B implements Interface I but delegates all public methods from Class A
    fun m2() {
        this.m1()
    }
}

object MTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val b: B = B(A())
        println(b.m2())
    }
}

class User(mapA: Map<String, Any?>, mapB: MutableMap<String, Any?>) {
    val name: String by mapA
    val age: Int by mapA
    val address: String by mapB
    val id: Long by mapB
}

object M2Test {
    @JvmStatic
    fun main(args: Array<String>) {
        val user: User = User(mapOf("name" to "John", "age" to 30), mutableMapOf("address" to "city, location", "id" to 101L))
        println("Name: ${user.name}")
        println("Age: ${user.age}")
        println("Address: ${user.address}")
        println("Id: ${user.id}")
    }
}