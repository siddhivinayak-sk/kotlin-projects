package com.sk.ktl.basic


/**
 * A function may be outsize of any class
 * Such functions created in a class created by Kotlin by its filename as public static functions
 *
 * Basics:
 * - Class
 * - variables, var, val, nullable, lateinit, not-nullable
 * - function, calling, function as parameter, if, if expression
 * - String equals with = sign
 * - Templates use in String
 *
 */
fun main(args: Array<String>) {
    println("Hello")

    /**
     * Variables can be created with val and var keywords
     * val - value can't be changed, used for constants
     * var - value can be changed
     */
    val random1: Int = 44332
    var random2: Int = 44332
    //random1 = 10 //compiler complain value can't be changed
    random2 = 10

    println("Random1 Value: " + random1) //String normal concatanation
    println("Random2 Value $random2") //String value by using template


    /**
     * Variable access and template example. A template can be used
     * to access variable within String by using $ sign
     */
    var john = Person() //New keyword is not required to create objects
    val mark = Person()

    john.name = "John"  //Variable access and assignment

    println("var John Name: ${john.name}")   //variable access using template
    println("var John Area: ${john.area}")   //Will be printing null when variable's value is null

    john.area = "Sector 37" //Value assigned to nullable variable of John
    println("var John Area: ${john.area}")   //Now, it will print value of area

    //variables marked as lateinit in Person must be initialized before calling may function from Person
    john.address = "myaddress"
    john.display()

    //Passing function as another function parameter
    john.functionPassAsParameter(john::display)

    //String comparison with == sign
    val str1: String = "abc"
    val str2: String = String("abc".toCharArray())
    if(str1 == str2) {
        println("Both strings are same")
    }

    //If as expression
    val str3 = if(str1 == str2) "same" else "different"
    println(str3)

    //Null handling with nullable variable
    var victor: Person? = null
    victor?.display() //Since variable is marked as nullable means if null is assigned, do not make call of Display method



    /**
     * Creating Address using constructor
      */
    var delhiAddress: Address
    val noidaAddress: Address = Address("Noida")



}

class Person {
    var name: String = "" //Variable must be initialize or mark latevinit
    var height: Int = 0
    lateinit var address: String //makred with lateinit
    var area: String? = null //Variable must not be null or marked for null by using ? sign

    fun display() {
        println("Display() Method:")
        println("Name: $name")
        println("Height: $height")
        println("Address: $address")
        println("Area: $area")
    }

    /**
     * Function taking argument
     */
    fun display(p: Person) {
        println("Display(p:Person) Method:")
        println("Name: ${p.name}")
        println("Height: ${p.height}")
        println("Address: ${p.address}")
        println("Area: ${p.area}")
    }


    /**
     * This function takes an argument 'function' who takes parameter as person
     */
    fun functionPassAsParameter(func: (p:Person) -> Unit) {
        func(this)  //Current Person object passed as parameter for the function
    }
}

/*
Class with constructor does not require to define class member variable explicitly
 */
class Address(var city: String) {}
