package com.sk.ktl.tika

import org.apache.tika.Tika

fun main(args: Array<String>) {

    val tika = Tika()
    tika.detect("www.google.com/abc.jar").also(::println)
}