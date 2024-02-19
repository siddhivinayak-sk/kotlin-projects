package com.sk.task.util

import kotlin.math.pow
import kotlin.math.sqrt

fun stdDev(numbers: Array<Int>): Double {
    return stdDev(numbers.map { it.toDouble() }.toTypedArray())
}

fun stdDev(numbers: Array<Float>): Double {
    return stdDev(numbers.map { it.toDouble() }.toTypedArray())
}

fun stdDev(numbers: Array<Long>): Double {
    return stdDev(numbers.map { it.toDouble() }.toTypedArray())
}

fun stdDev(numbers: Array<Double>): Double {
    //https://www.khanacademy.org/math/statistics-probability/summarizing-quantitative-data/variance-standard-deviation-population/a/calculating-standard-deviation-step-by-step
    val mean = mean(numbers)
    val variance = numbers.sumOf { (it - mean).pow(2.0) } / numbers.size
    return sqrt(variance)
}

fun mean(numbers: Array<Double>): Double {
    return numbers.sum() / numbers.size
}

fun mean(numbers: Array<Int>): Double {
    return (numbers.sum() / numbers.size).toDouble()
}
