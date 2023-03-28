package com.klopoffaa.calculator.core

import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToLong

class Calculator {
    private var operandA: Long = 0
    private var operandAFloatingPoint: Long = 0
    private var operandB: Long = 0
    private var operandBFloatingPoint: Long = 0
    private var isCurrOperandA: Boolean = true
    private var forceDelimiter: Boolean = false
    private var currOperation: Operation = Operation.DIVIDE
    private var stateUpdateListener: () -> Unit = {}

    fun setStateUpdateListener(listener: () -> Unit) {
        stateUpdateListener = listener
    }

    fun number(number: Long) {
        val currOperand = getCurrOperand()
        setCurrOperand(addNumberToEnd(currOperand, number))

        stateUpdateListener()
    }

    fun clear() {
        setCurrOperandWithFloatingPoint(0.0)

        if (!isCurrOperandA) {
            isCurrOperandA = true
        }
        forceDelimiter = false

        stateUpdateListener()
    }

    fun sign() {
        setCurrOperandWithFloatingPoint(-getCurrOperandWithFloatingPoint())

        stateUpdateListener()
    }

    fun percent() {
        setCurrOperandWithFloatingPoint(getCurrOperandWithFloatingPoint() / 100)

        stateUpdateListener()
    }

    fun operation(operation: Operation) {
        if (!isCurrOperandA) {
            calculate()
        }

        isCurrOperandA = !isCurrOperandA
        currOperation = operation
        forceDelimiter = false

        stateUpdateListener()
    }

    fun delimiter() {
        forceDelimiter = true

        stateUpdateListener()
    }

    fun calculate() {
        if (!isCurrOperandA) {
            val operandAWithFloatingPoint = composeNumber(operandA, operandAFloatingPoint)
            val operandBWithFloatingPoint = composeNumber(operandB, operandBFloatingPoint)

            val decomposedOperandA = decomposeNumber(when (currOperation) {
                Operation.DIVIDE -> operandAWithFloatingPoint / operandBWithFloatingPoint
                Operation.MULTIPLY -> operandAWithFloatingPoint * operandBWithFloatingPoint
                Operation.SUBTRACT -> operandAWithFloatingPoint - operandBWithFloatingPoint
                Operation.ADD -> operandAWithFloatingPoint + operandBWithFloatingPoint
            })

            operandA = decomposedOperandA.first
            operandAFloatingPoint = decomposedOperandA.second

            operandB = 0
            operandBFloatingPoint = 0

            isCurrOperandA = true
            forceDelimiter = false
        }

        stateUpdateListener()
    }

    fun isCurrNumberIsFirstOperand(): Boolean {
        return isCurrOperandA
    }

    fun hasCurrNumberDelimiter(): Boolean {
        return isCurrentOperandFloatingPoint()
    }

    fun getCurrNumber(): Double {
        return getCurrOperandWithFloatingPoint()
    }

    private fun addNumberToEnd(source: Long, number: Long): Long {
        return if (getNumberDigits(source) > 0) {
            source * 10 + number
        } else {
            if (source == 0L) number else source * 10 + number
        }
    }

    private fun getCurrOperand(): Long {
        return if (isCurrentOperandFloatingPoint()) {
            getCurrOperandFloatingPoint()
        } else {
            getCurrOperandInteger()
        }
    }

    private fun setCurrOperand(value: Long) {
        if (isCurrentOperandFloatingPoint()) {
            setCurrOperandFloatingPoint(value)
        } else {
            setCurrOperandInteger(value)
        }
    }

    private fun getCurrOperandInteger(): Long {
        return if (isCurrOperandA) {
            operandA
        } else {
            operandB
        }
    }

    private fun setCurrOperandInteger(value: Long) {
        if (isCurrOperandA) {
            operandA = value
        } else {
            operandB = value
        }
    }

    private fun getCurrOperandFloatingPoint(): Long {
        return if (isCurrOperandA) {
            operandAFloatingPoint
        } else {
            operandBFloatingPoint
        }
    }

    private fun setCurrOperandFloatingPoint(value: Long) {
        if (isCurrOperandA) {
            operandAFloatingPoint = value
        } else {
            operandBFloatingPoint = value
        }
    }

    private fun getCurrOperandWithFloatingPoint(): Double {
        return if (isCurrOperandA) {
            composeNumber(operandA, operandAFloatingPoint)
        } else {
            composeNumber(operandB, operandBFloatingPoint)
        }
    }

    private fun setCurrOperandWithFloatingPoint(value: Double) {
        val number = decomposeNumber(value)

        if (isCurrOperandA) {
            operandA = number.first
            operandAFloatingPoint = number.second
        } else {
            operandB = number.first
            operandBFloatingPoint = number.second
        }
    }

    private fun composeNumber(integerPart: Long, floatingPointPart: Long): Double {
        return if (floatingPointPart == 0L) {
            integerPart.toDouble()
        } else {
            val floatingPointSize = getNumberDigits(floatingPointPart)
            integerPart + floatingPointPart / 10.0.pow(floatingPointSize)
        }
    }

    private fun decomposeNumber(number: Double): Pair<Long, Long> {
        if (number == 0.0) {
            return Pair(0L, 0L)
        }

        val integerPart = number.toLong()
        val floatingPointPart = number - integerPart

        if (floatingPointPart == 0.0) {
            return Pair(integerPart, 0L)
        }

        val floatingZeroes = log10(1.0 / (floatingPointPart % 1.0)).toInt()

        return if (floatingZeroes > 0) {
            Pair(integerPart, (floatingPointPart * 10.0.pow(floatingZeroes)).roundToLong())
        } else {
            Pair(integerPart, (floatingPointPart * 10.0).roundToLong())
        }
    }

    private fun isCurrentOperandFloatingPoint(): Boolean {
        if (forceDelimiter) {
            return true
        }
        val currOperandFloatingPoint = if (isCurrOperandA) operandAFloatingPoint else operandBFloatingPoint
        return currOperandFloatingPoint > 0
    }

    private fun getNumberDigits(number: Number): Int {
        return log10(number.toDouble()).toInt() + 1
    }
}