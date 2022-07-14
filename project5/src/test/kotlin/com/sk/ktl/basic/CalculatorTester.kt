package com.sk.ktl.basic

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class CalculatorTester {

    @Test
    fun `1 + 1 = 2`() {
        val calculator = CalculatorTest()
        assertEquals(2, calculator.add(1, 1))
    }

    @ParameterizedTest(name = "{0} + {1} = {3}")
    @CsvSource("0,1,1", "1,2,3", "49,51,100", "1,100,101")
    fun add(first: Int, second: Int, result: Int) {
        val calculator = CalculatorTest()
        assertEquals(result, calculator.add(first, second), "$first + $second should equal $result")
    }


}