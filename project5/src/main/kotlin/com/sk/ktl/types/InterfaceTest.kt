package com.sk.ktl.types

fun main(s:Array<String>) {
    var ab = ABImpl()
    ab.myAction()
    ab.actionA()
    ab.actionB()
}

interface A {
    fun actionA()
    fun myAction() = run { println("myaction of A") }
}

interface B {
    fun actionB()
    fun myAction() = run { println("myaction of B") }
}

class ABImpl: A, B {
    override fun actionA() {
        println("Action A")
    }

    override fun actionB() {
        println("Action B")
    }

    override fun myAction() {
        println("Default Action")
        super<A>.myAction();
    }
}