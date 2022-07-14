package com.sk.ktl.generictest

fun main(s:Array<String>) {
    var meetings = listOf(BoardMeeting(), FinanaceMeeting(), BoardMeeting())

    var boardMeetingCount = meetings.typeOf<BoardMeeting>().count()
    println("Board Meeting Count: $boardMeetingCount")
}

open class Meeting {}
class BoardMeeting: Meeting() {}
class FinanaceMeeting: Meeting() {}

/**
 * The reified types maintain the type information at runtime but it is only supported in inline functions
 */
inline fun <reified T> List<*>.typeOf(): List<T> {
    val returnList = mutableListOf<T>()
    for(item in this) {
        if(item is T) { //In case type is not marked as 'reified' then compile error: Cannot check for instance of erased type: T
            returnList.add(item)
        }
    }
    return returnList
}

/**
 * Simple generic fun implementation where fun is expecting a fun reference with generic argument
 */
fun <T: Meeting> buildMeeting(meetingClass: Class<T>, action:(T) -> Unit) : T {
    var newMeeting: T = meetingClass.newInstance()
    action(newMeeting)
    return newMeeting
}

/**
 * Another fun implementation where fun is expecting a fun reference with generic
 * argument and type of class is inferred from Generic Type T and inline & reified
 * has been used to preserve generic type
 */
inline fun <reified T: Meeting> buildMeeting(action:(T) -> Unit) : T {
    var newMeeting: T = T::class.java.newInstance() //If type is not marked as reified, it does not provide class object
    action(newMeeting)
    return newMeeting
}

/**
 * Another fun implementation where function is expecting fun reference with generic
 * argument and type of class is inferred from reified type T and being passed into another method
 * as argument.
 *
 * Here, the inline lambda cannot be passed directly to another method, it must be marked as noinline
 */
inline fun <reified T: Meeting> buildMeeting2(noinline action:(T) -> Unit) : T {
    return buildMeeting (T::class.java, action) //action lambda must be marked as noinline
}