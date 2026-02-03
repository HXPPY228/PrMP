package com.example.lab.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    var displayState by mutableStateOf("0")
    private var firstNumber = 0.0
    private var currentOperator: String? = null
    private var isNewOp = true

    fun onAction(action: String) {
        when {
            action in "0".."9" -> appendNumber(action)
            action == "." -> appendComma()
            action == "C" -> clear()
            action == "⌫" -> deleteLast()
            action == "+/-" -> toggleSign()
            action == "%" -> percentSign()
            action in listOf("+", "-", "×", "/") -> setOperator(action)
            action == "=" -> calculate()
        }
    }

    private fun appendNumber(num: String) {
        if (isNewOp || displayState == "0") {
            displayState = num
            isNewOp = false
        } else if (displayState.length < 9) {
            displayState += num
        }
    }

    private fun toggleSign() {
        if (displayState != "0" && displayState != "Ошибка") {
            displayState = if (displayState.startsWith("-")) {
                displayState.substring(1)
            } else {
                "-$displayState"
            }
        }
    }

    private fun percentSign(){
        if (displayState != "0" && displayState != "Ошибка") {
            val currentValue = displayState.toDoubleOrNull() ?: 0.0
            val result = currentValue / 100.0

            displayState = result.toString().let {
                if (it.endsWith(".0")) it.dropLast(2) else it
            }
            isNewOp = true
        }
    }
    private fun deleteLast() {
        if (displayState.length > 1 && displayState != "Ошибка") {
            displayState = displayState.dropLast(1)
        } else {
            displayState = "0"
            isNewOp = true
        }
    }

    private fun appendComma() {
        if (!displayState.contains(".") && displayState != "Ошибка") displayState += "."
    }

    private fun clear() {
        displayState = "0"
        firstNumber = 0.0
        currentOperator = null
        isNewOp = true
    }

    private fun setOperator(op: String) {
        firstNumber = displayState.toDoubleOrNull() ?: 0.0
        currentOperator = op
        isNewOp = true
    }

    private fun calculate() {
        val secondNumber = displayState.toDoubleOrNull() ?: 0.0
        val result: Double = when (currentOperator) {
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "×" -> firstNumber * secondNumber
            "/" -> if (secondNumber != 0.0) firstNumber / secondNumber else Double.NaN
            else -> return
        }

        displayState = if (result.isNaN()) {
            "Ошибка"
        } else {
            val formatted = result.toString()
            if (formatted.endsWith(".0")) formatted.dropLast(2) else formatted
        }
        currentOperator = null
        isNewOp = true
    }
}