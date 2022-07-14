package com.sk.ktl.basic

import java.time.chrono.IsoChronology

fun main(s:Array<String>) {
    //var m1: Meeting = null //This will give compiler error 'Null cannot be value of Not-Null type'

    var m1: Meeting? = null  //Making variable nullable
    var m2: Meeting = Meeting()

    //m2 = m1 //This will give compiler error 'Type mismatch'

    m2 = m1 ?: Meeting() //This is use of Elvis operator if m1 is not null assign it else create new meeting and assign

    //closeMeeting(m1) //Compiler error 'Type mismatch'
    closeMeeting(m2)


    closeMeeting2(m1) //Now, it can be called as method is accepting nullable parameter
    closeMeeting2(m2)

    closeMeeting3(m1)
    closeMeeting3(m2)

    m2.save(object: ISaveable {
        override fun save() {
            TODO("Not yet implemented")
        }
    })
}

fun closeMeeting(m: Meeting): Boolean? {
    return if (m.canClose) m.close()
    else false
}

//Making parameter nullable and use of Safe-Call operator
fun closeMeeting2(m: Meeting?): Boolean? {
    return if (m?.canClose == true) m?.close()
    else false
}

//Making parameter nullable and use of Not-Null Assertion
fun closeMeeting3(m: Meeting?): Boolean? {
    return if (m!!.canClose) m.close()
    else false
}


class Meeting {
    val canClose: Boolean = false
    lateinit var address: MyAddress

    fun close(): Boolean {
        return true
    }

    fun save(o: Any) {
        val saveable = o as? ISaveable //User of Safe-Cast
        saveable?.save()
    }

    fun init(address: MyAddress) {
        this.address = address
    }
}

interface ISaveable {
    fun save()
}

class MyAddress {}