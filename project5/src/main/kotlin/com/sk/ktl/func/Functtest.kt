package com.sk.ktl.func

import java.math.BigInteger

//@JvmName("FunctionTest")  //It will define the name of the class generated for this KT file
fun main(s:Array<String>) {
    val john = Person("John")

    john.display() //Simple calling
    john.display(john) //Calling with Parameter
    john.display(john::display) //Calling with func as parameter
    john.max(4, 6) //Expression function calling

    john.displayName() //calling function with optional parameter
    john.displayName("Z") //Calling optional with one parameter
    john.displayName("x", 9) //Calling optional with all parameter

    john.displayName(name = "y", age = 11) //Calling optional with parameter name

    println("Calling of Extension: " + "    my    name    is    John".replaceMultipleWhitespaces()) //Calling of Extension function

    val watson = Person("Watson")
    val megal = Person("Megal")
    val watson_and_megal = watson plus megal //Infix function calling as plus as operator
    watson_and_megal.display()
    val _watson_and_megal = watson + megal   //+ operator overloaded for Person
    _watson_and_megal.display()

    //Function call marked with tailrec
    println(fib(10000, BigInteger("1"), BigInteger("0")))

}

class Person(private var name:String) {

    //Simple member function
    fun display() {
        println("Name: $name")
    }

    //Function with argument
    fun display(p:Person) {
        p.display()
    }

    //Function taking another function as parameter
    fun display(func: () -> Unit) {
        func()
    }

    //Function with optional parameter
    //fun displayName(name: String = "No Name") {
    //    println("Name(Optional): $name")
    //}

    //Function with optional parameter with Overload feature (it will create separate methods for each optional parameter)
    @JvmOverloads
    fun displayName(name: String = "No Name", age: Int = 0) {
        println("Name(Optional): $name and Age: $age")
    }

    //Function as expression
    fun max(a:Int, b:Int) : Int = if(a > b) a else b


    //Infix function
    infix operator fun plus(other:Person) : Person {
        return Person(this.name + " & " + other.name)
    }
}

/**
 * Extension function
 */
fun String.replaceMultipleWhitespaces() : String {
    val regex = Regex("\\s+")
    return regex.replace(this, " ")
}

//Recursive function marked with TailRec
tailrec fun fib(n: Int, a: BigInteger, b: BigInteger): BigInteger {
    return if(n == 0) b else fib(n - 1, a + b, a)
}