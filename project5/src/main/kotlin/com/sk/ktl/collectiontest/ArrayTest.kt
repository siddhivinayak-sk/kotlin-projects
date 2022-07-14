package com.sk.ktl.collectiontest

fun main(s:Array<String>) {

    var a1 = Array<String>(size = 2, init = { n -> "A"})
    var a2 = arrayOf<String>()
    var a3 = arrayOfNulls<String>(2)
    var a4 = intArrayOf()
    var a5 = byteArrayOf()
    var a6 = charArrayOf()
    var a7 = doubleArrayOf()
    var a8 = longArrayOf()
    var a9 = IntArray(5)
    var a10 = DoubleArray(2)

    for(s in  a1.indices) {
        println("Index: $s = ${a1[s]}")
    }
    a1[0] = "a"
    a1[1] = "b"
    a1.forEachIndexed {index, element -> println("a1[$index]=$element")}
}