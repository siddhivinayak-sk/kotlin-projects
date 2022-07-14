package com.sk.ktl.maptest

fun main(s:Array<String>) {

    val maptest: MutableMap<String, String> = mutableMapOf()
    val regex: Regex = Regex("\\{(.*?)\\}")

    println(maptest)
    println(maptest.toString().matches(regex))

    maptest["A1"] = "V1"
    println(maptest)
    println(maptest.toString().matches(regex))

    maptest["A2"] = "V2"
    println(maptest)
    println(maptest.toString().matches(regex))




}