package com.sk.ktl.func

fun main(s:Array<String>) {

    var john = Person2()

    //Setting properties by using with where lambda used to set object property
    with(john) {
        id = 1
        name = "John"
        address = "New York"
        phone= "+19808980243"
    }
    john.display()

    //Setting property of object by using lambda based apply function
    john.apply {
        address = "Bostan"
        phone= "+1737497934"
    }
    john.display()

    //Process functions implementation by using anonymous way using object keyword
    john.add(10, 20, object : Process {
        override fun execute(value: Int) {
            println("Addition: $value")
        }
    })

    //Passing the function directly into function parameter - syntax 1
    john.subtract(20, 10, {n -> println("Subtraction1: $n")})

    //Passing the function directly into function parameter - syntax 2
    john.subtract(20, 10) {n -> println("Subtraction2: $n")}

    //Passing the function directly into function parameter - syntax 3 (if there is one argument then 'it' keyword can be used)
    john.subtract(22, 11, { println("Substraction3: $it") })

    //Passing the function directly into function parameter - syntax 4 (if there is one argument then 'it' keyword can be used)
    john.subtract(22, 11) { println("Substraction4: $it") }

    //Passing the function reference directly into function parameter - syntax 5
    john.subtract(34, 10, ::println)
}


interface Process {
    fun execute(value: Int)
}

class Person2 {
    var id: Int = -1
    var name: String = ""
    var address: String = ""
    var phone: String = ""

    fun display() {
        println("Id: $id")
        println("Name: $name")
        println("Address: $address")
        println("Phone: $phone")
    }

    //When function taken as class: Object Oriented Strategy Pattern implementation using interface
    fun add(a: Int, b: Int, p: Process) {
        var c = a + b
        p.execute(c)
    }

    //When function taken as function: In Kotlin Strategy Pattern implementation
    fun subtract(a: Int, b: Int, func: (value: Int) -> Unit) {
        var c = a - b
        func(c)
    }




}

