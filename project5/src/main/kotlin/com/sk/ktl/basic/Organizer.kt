package com.sk.ktl.basic


fun main(s:Array<String>) {
    val m = MyMeeting()
    //m.addTitle("Title")
    //m.addTitle(null)   //Since java method parameter annotated with @NotNull therefore null cannot be assigned

    val title:String? = m.meetingTitle() //since return type in Java marked with @Nullable, variable must be nullable

    println("Title: $title")
}