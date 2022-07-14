package com.sk.ktl.dsltest

fun main(s:Array<String>) {
    TempDsl().runDsl()
}

class Temp(var current: Float = 0.0f) {
    private var type: String = ""

    fun add(amount: Float) {
        current += amount
    }

    fun convertToC() {
        current = (current - 32) * .5556f
        type = "C"
    }

    fun convertToF() {
        current = current * .5556f + 32
        type = "F"
    }

    override fun toString(): String {
        return "$current$type"
    }
}

class TempDsl {
    fun runDsl() {
        val temp = TempBuilder().current()
            .toF().addF(10.0f)
            .toC().addC(10.0f)
            .toF().addF(10.0f).
            toC()
            .build()
        println("Final Temp ${temp}")

    }
}

open class TempBuilder(var temp: Temp = Temp()) {
    fun current(): TempBuilderImmutable {
        temp.current = 0.0f
        println("Current Temp: ${temp.current}")
        return TempBuilderImmutable(temp)
    }

    fun toF(): TempBuilderFarenheit {
        temp.convertToF()
        return TempBuilderFarenheit(temp)
    }

    fun toC(): TempBuilderCelsius {
        temp.convertToC()
        return TempBuilderCelsius(temp)
    }

    fun build(): Temp {
        return temp
    }
}

class TempBuilderImmutable(temp: Temp): TempBuilder(temp)

class TempBuilderFarenheit(temp: Temp): TempBuilder(temp) {
    fun addF(value: Float): TempBuilderFarenheit {
        temp.add(value)
        return this
    }
}

class TempBuilderCelsius(temp: Temp): TempBuilder(temp) {
    fun addC(value: Float): TempBuilderCelsius {
        temp.add(value)
        return this
    }
}