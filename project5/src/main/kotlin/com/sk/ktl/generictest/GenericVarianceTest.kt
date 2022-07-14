package com.sk.ktl.generictest

import javax.print.attribute.standard.Destination

/**
 * out = same class and subclass accepted = extends
 * in = same class and superclass accepted = super
 *
 * Kotlin supports:
 * - Declaration site variance where in/out is defined at generic type definition with class
 */

open class MyMeeting {
    fun attend() {println("Meeting attending")}
}
class MyBoardMeeting: MyMeeting() {}
class MyFinanceMeeting: MyMeeting() {}

/**
 * Example of Declaration site variance: Means 'out' has been defined into
 * class declaration itself which defines what type of variance is tolerable
 */
class AllMeeting<out T: MyMeeting>(private val meetings: List<MyMeeting>) {
    val count: Int get() = meetings.count()
    operator fun get(i: Int) = meetings[i]
}

fun attendAllMeetings(meetings: AllMeeting<MyMeeting>) {
    for(i in 0 until meetings.count) {
        meetings[i].attend()
    }
}

/**
 * Call site variance: variance defined at the place where argument is passed
 */
fun <T> copyData(source: MutableList<out T>, destination: MutableList<T>) {
    for(item in source) {
        destination.add(item)
    }
}

fun main(s: Array<String>) {
    var finanaceMeetings = mutableListOf(MyFinanceMeeting())
    var meetings: AllMeeting<MyFinanceMeeting> = AllMeeting(finanaceMeetings)
    attendAllMeetings(meetings)
}

