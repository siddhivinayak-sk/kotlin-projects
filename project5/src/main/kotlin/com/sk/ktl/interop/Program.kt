package com.sk.ktl.interop

fun main(s:Array<String>) {
    var user: User = User("John")

    var count = 0
    //Compiler will complain if lambda is pullout seprately: Required ((User!) -> Unit)! but Found () -> Unit
    //Need to add SAM constructor here, just define the interface name along with lambda
    //var createdLambda = { println("User with name ${it.name} created ${++count} times") }

    var createdLambda =  Created { println("User with name ${it.name} created ${++count} times") }
    user.create (createdLambda)
    user.create { println("User with name ${it.name} created ${++count} times") }


    var name: String? = null
    var name2: String = ""
    //name2 = name //It will throw 'Type mismatch' compiler error

}