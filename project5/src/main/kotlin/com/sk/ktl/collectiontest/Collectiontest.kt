package com.sk.ktl.collectiontest

fun main(s:Array<String>) {
    val ints = listOf<Int>(1, 2, 3, 4, 5)
    val smallInts = ints.filter { it < 4 }
    for(i in smallInts) println(i)

    val squares = ints.map { it * it }
    for(i in squares) println(i)

    ints.filter { it < 4 }.map { it * it }.forEach(::println)

    var persons = listOf<Person>(Person(1, "John", "New York", 40)
    ,Person(2, "McDonald", "New York", 45)
    ,Person(3, "Mike", "Bostan", 20)
    ,Person(4, "Bolivia", "New Jersey", 18)
    ,Person(5, "McKenzi", "Columbia", 10)
    ,Person(6, "Urokamia", "Washington", 22)
    ,Person(7, "Chelsia", "Bostan", 76)
    ,Person(8, "Don", "New Yrk", 81)
    ,Person(9, "Brad", "New Jersey", 35)
    ,Person(10, "Peter", "Columbia", 81))

    persons.filter { it.age > 60 }.map{it.name}.forEach(::println)

    println("All Age > 80: ${persons.all { it.age > 80 }}")
    println("Any Age > 80: ${persons.any { it.age > 80 }}")
    println("Count: ${persons.count()}")
    println("Count Age > 50: ${persons.count {it.age > 50}}")
    println("Find Age > 40: ${persons.find { it.age >40 }}")


    var works = persons.flatMap { it.works }.count()
    println("Work Count: $works")
    println("Unique Work Count: ${persons.flatMap { it.works }.distinct().count()}")


    // Use of Sequence for lazy operation on collection
    println(persons.asSequence().filter { it.address.startsWith("N") }.count())
}

data class Person(var id:Int, var name: String, var address: String, var age: Int) {
    var works = listOf<Work>(Work.SLEEP, Work.PLAY, Work.SING, Work.WALK)
}

enum class Work {
    SLEEP, PLAY, SING, WALK, EAT, RUN, TYPE
}