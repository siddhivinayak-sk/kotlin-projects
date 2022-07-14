package com.sk.ktl.types

fun main(s:Array<String>) {
    handlePersonEvent(PersonEvent.Awake())
    handlePersonEvent(PersonEvent.Asleep())
    handlePersonEvent(PersonEvent.Eating("Burger"))
    handlePersonEvent(Walking())
}

/**
 * Sealed classes are used restrict the inheritance.
 * If class is sealed, it can be inherited to only classes which are being compiled together. No third-party module
 * can inherit it further
 *
 * Such type of sealed classes can be used with when(*)
 */
sealed class PersonEvent {
    class Awake: PersonEvent()
    class Asleep: PersonEvent()
    class Eating(val food: String) : PersonEvent()
}

class Walking: PersonEvent()

fun handlePersonEvent(event: PersonEvent) {
    when(event) {
        is PersonEvent.Awake -> println("Awake")
        is PersonEvent.Asleep -> println("Asleep")
        is PersonEvent.Eating -> println("Eating ${event.food}")
        is Walking -> println("Walking")
    }
}
