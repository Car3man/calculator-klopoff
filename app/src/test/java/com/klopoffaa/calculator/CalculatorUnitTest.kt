package com.klopoffaa.calculator

import com.klopoffaa.calculator.core.Calculator
import com.klopoffaa.calculator.core.Operation
import org.junit.Test

import org.junit.Assert.*

class CalculatorUnitTest {
    @Test
    fun change_sign_isCorrect() {
        val calculator = Calculator()
        calculator.number(2)
        calculator.number(5)
        calculator.sign()
        assertEquals(-25.0, calculator.getCurrNumber(), 0.001)
    }

    @Test
    fun percent_isCorrect() {
        val calculator = Calculator()
        calculator.number(7)
        calculator.number(0)
        calculator.percent()
        assertEquals(0.7, calculator.getCurrNumber(), 0.001)
    }

    @Test
    fun divide_isCorrect() {
        val calculator = Calculator()
        calculator.number(3)
        calculator.delimiter()
        calculator.number(3)
        calculator.operation(Operation.DIVIDE)
        calculator.number(3)
        calculator.calculate()
        assertEquals(1.1, calculator.getCurrNumber(), 0.001)
    }

    @Test
    fun multiply_isCorrect() {
        val calculator = Calculator()
        calculator.number(2)
        calculator.number(5)
        calculator.operation(Operation.MULTIPLY)
        calculator.number(4)
        calculator.calculate()
        assertEquals(100.0, calculator.getCurrNumber(), 0.001)
    }

    @Test
    fun subtract_isCorrect() {
        val calculator = Calculator()
        calculator.number(1)
        calculator.number(0)
        calculator.number(0)
        calculator.operation(Operation.SUBTRACT)
        calculator.number(33)
        calculator.calculate()
        assertEquals(67.0, calculator.getCurrNumber(), 0.001)
    }

    @Test
    fun add_isCorrect() {
        val calculator = Calculator()
        calculator.number(3)
        calculator.number(3)
        calculator.operation(Operation.ADD)
        calculator.number(6)
        calculator.number(7)
        calculator.calculate()
        assertEquals(100.0, calculator.getCurrNumber(), 0.001)
    }
}