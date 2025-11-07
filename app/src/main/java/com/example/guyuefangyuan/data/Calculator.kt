package com.example.guyuefangyuan.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.math.BigDecimal
import java.math.RoundingMode

class Calculator {
    var displayText by mutableStateOf("0")
        private set
    
    private var firstOperand = BigDecimal.ZERO
    private var secondOperand = BigDecimal.ZERO
    private var pendingOperation: Operation? = null
    private var isNewInput = true
    private var hasDecimal = false
    
    enum class Operation(val symbol: String) {
        ADD("+"),
        SUBTRACT("-"),
        MULTIPLY("×"),
        DIVIDE("÷"),
        PERCENTAGE("%")
    }
    
    fun onDigit(digit: Int) {
        if (isNewInput) {
            displayText = digit.toString()
            isNewInput = false
        } else {
            if (displayText == "0") {
                displayText = digit.toString()
            } else {
                displayText += digit.toString()
            }
        }
    }
    
    fun onDecimal() {
        if (isNewInput) {
            displayText = "0."
            isNewInput = false
            hasDecimal = true
        } else if (!hasDecimal) {
            displayText += "."
            hasDecimal = true
        }
    }
    
    fun onOperation(operation: Operation) {
        if (pendingOperation != null && !isNewInput) {
            calculate()
        }
        
        firstOperand = BigDecimal(displayText)
        pendingOperation = operation
        isNewInput = true
        hasDecimal = false
    }
    
    fun onEquals() {
        if (pendingOperation != null && !isNewInput) {
            calculate()
            pendingOperation = null
        }
    }
    
    fun onClear() {
        displayText = "0"
        firstOperand = BigDecimal.ZERO
        secondOperand = BigDecimal.ZERO
        pendingOperation = null
        isNewInput = true
        hasDecimal = false
    }
    
    fun onDelete() {
        if (!isNewInput && displayText.length > 1) {
            if (displayText.last() == '.') {
                hasDecimal = false
            }
            displayText = displayText.dropLast(1)
        } else {
            displayText = "0"
            isNewInput = true
        }
    }
    
    fun onPercentage() {
        if (!isNewInput) {
            val value = BigDecimal(displayText).divide(BigDecimal(100), 10, RoundingMode.HALF_EVEN)
            displayText = formatResult(value)
        }
    }
    
    fun onSignChange() {
        if (displayText != "0") {
            displayText = if (displayText.startsWith("-")) {
                displayText.substring(1)
            } else {
                "-$displayText"
            }
        }
    }
    
    private fun calculate() {
        secondOperand = BigDecimal(displayText)
        val result = when (pendingOperation) {
            Operation.ADD -> firstOperand.add(secondOperand)
            Operation.SUBTRACT -> firstOperand.subtract(secondOperand)
            Operation.MULTIPLY -> firstOperand.multiply(secondOperand)
            Operation.DIVIDE -> {
                if (secondOperand == BigDecimal.ZERO) {
                    BigDecimal.ZERO // 避免除以0错误，可以在UI中添加错误提示
                } else {
                    firstOperand.divide(secondOperand, 10, RoundingMode.HALF_EVEN)
                }
            }
            Operation.PERCENTAGE -> firstOperand.multiply(secondOperand).divide(BigDecimal(100), 10, RoundingMode.HALF_EVEN)
            else -> BigDecimal.ZERO
        }
        
        displayText = formatResult(result)
        firstOperand = result
        isNewInput = true
        hasDecimal = displayText.contains(".")
    }
    
    private fun formatResult(result: BigDecimal): String {
        // 移除尾部的0和不必要的小数点
        return result.stripTrailingZeros().toPlainString().let {
            if (it == "0E-10") "0" else it
        }
    }
} 