package com.sk.ktl.fortest.demo2

interface DateProvider {
    val dayOfMonth: Int
    val monthValue: Int
    val year: Int
}

class SystemLocalDate(val date: java.time.LocalDate) : DateProvider {
    override val monthValue: Int
        get() = date.monthValue
    override val year: Int
        get() = date.year
    override val dayOfMonth: Int
        get() = date.dayOfMonth

}