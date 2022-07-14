package com.sk.ktl.types

fun main(s:Array<String>) {

    var aMeeting: Meeting = Meeting(name = "A Meeting", location = "London")
    var bMeeting: Meeting = Meeting("A Meeting", "London")
    println("Are equal: ${aMeeting == bMeeting}")

    var another = aMeeting.copy(location = "New York")
    another.display()

}

/**
 * Data classes are specialized claesses which are used for data storage for collection use or data transfer
 * use. It internally provides hashCode, equals and toString implementation.
 *
 * It provides copy method which is used for cloning purpose. It also provides overloaded operators to enrich
 * feature for data purpose.
 */
data class Meeting (val name:String, val location: String) {
    fun display() {
        println("Name: $name    Location: $location")
    }
}


