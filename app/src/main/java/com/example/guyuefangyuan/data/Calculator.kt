package com.example.guyuefangyuan.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.*

// 计算记录数据类
data class CalculationRecord(
    val expression: String,
    val result: String,
    val timestamp: Long = System.currentTimeMillis()
)

class Calculator {
    var displayText by mutableStateOf("0")
        private set
    
    private var firstOperand = BigDecimal.ZERO
    private var secondOperand = BigDecimal.ZERO
    private var pendingOperation: Operation? = null
    private var isNewInput = true
    private var hasDecimal = false
    private var isScientificMode = false
    
    // 历史记录
    var history by mutableStateOf<List<CalculationRecord>>(emptyList())
        private set
    
    // 错误状态
    var hasError by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set
    
    enum class Operation(val symbol: String) {
        ADD("+"),
        SUBTRACT("-"),
        MULTIPLY("×"),
        DIVIDE("÷"),
        PERCENTAGE("%"),
        POWER("^"),
        SQUARE_ROOT("√"),
        SINE("sin"),
        COSINE("cos"),
        TANGENT("tan"),
        LOGARITHM("log"),
        NATURAL_LOG("ln"),
        FACTORIAL("!")
    }
    
    fun onDigit(digit: Int) {
        clearError()
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
        clearError()
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
        clearError()
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
            val expression = "$firstOperand ${pendingOperation?.symbol} $displayText"
            calculate()
            val result = displayText
            
            // 添加到历史记录
            history = history + CalculationRecord(expression, result)
            
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
        clearError()
    }
    
    fun onDelete() {
        clearError()
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
        clearError()
        if (!isNewInput) {
            val value = BigDecimal(displayText).divide(BigDecimal(100), 10, RoundingMode.HALF_EVEN)
            displayText = formatResult(value)
        }
    }
    
    fun onSignChange() {
        clearError()
        if (displayText != "0") {
            displayText = if (displayText.startsWith("-")) {
                displayText.substring(1)
            } else {
                "-$displayText"
            }
        }
    }
    
    private fun calculate() {
        try {
            secondOperand = BigDecimal(displayText)
            val result = when (pendingOperation) {
                Operation.ADD -> firstOperand.add(secondOperand)
                Operation.SUBTRACT -> firstOperand.subtract(secondOperand)
                Operation.MULTIPLY -> firstOperand.multiply(secondOperand)
                Operation.DIVIDE -> {
                    if (secondOperand == BigDecimal.ZERO) {
                        hasError = true
                        errorMessage = "不能除以零"
                        BigDecimal.ZERO
                    } else {
                        firstOperand.divide(secondOperand, 10, RoundingMode.HALF_EVEN)
                    }
                }
                Operation.PERCENTAGE -> firstOperand.multiply(secondOperand).divide(BigDecimal(100), 10, RoundingMode.HALF_EVEN)
                Operation.POWER -> {
                    val base = firstOperand.toDouble()
                    val exponent = secondOperand.toDouble()
                    if (base < 0 && exponent % 1 != 0.0) {
                        hasError = true
                        errorMessage = "负数的非整数次幂"
                        BigDecimal.ZERO
                    } else {
                        BigDecimal(base.pow(exponent))
                    }
                }
                Operation.SQUARE_ROOT -> {
                    val value = firstOperand.toDouble()
                    if (value < 0) {
                        hasError = true
                        errorMessage = "负数的平方根"
                        BigDecimal.ZERO
                    } else {
                        BigDecimal(sqrt(value))
                    }
                }
                Operation.SINE -> {
                    val radians = firstOperand.toDouble() * PI / 180
                    BigDecimal(sin(radians))
                }
                Operation.COSINE -> {
                    val radians = firstOperand.toDouble() * PI / 180
                    BigDecimal(cos(radians))
                }
                Operation.TANGENT -> {
                    val radians = firstOperand.toDouble() * PI / 180
                    val value = tan(radians)
                    if (value.isInfinite()) {
                        hasError = true
                        errorMessage = "正切值未定义"
                        BigDecimal.ZERO
                    } else {
                        BigDecimal(value)
                    }
                }
                Operation.LOGARITHM -> {
                    val value = firstOperand.toDouble()
                    if (value <= 0) {
                        hasError = true
                        errorMessage = "对数的真数必须大于0"
                        BigDecimal.ZERO
                    } else {
                        BigDecimal(log10(value))
                    }
                }
                Operation.NATURAL_LOG -> {
                    val value = firstOperand.toDouble()
                    if (value <= 0) {
                        hasError = true
                        errorMessage = "自然对数的真数必须大于0"
                        BigDecimal.ZERO
                    } else {
                        BigDecimal(ln(value))
                    }
                }
                Operation.FACTORIAL -> {
                    val value = firstOperand.toInt()
                    if (value < 0 || value > 20) {
                        hasError = true
                        errorMessage = "阶乘值超出范围(0-20)"
                        BigDecimal.ZERO
                    } else {
                        var result = 1.0
                        for (i in 1..value) {
                            result *= i
                        }
                        BigDecimal(result)
                    }
                }
                else -> BigDecimal.ZERO
            }
            
            displayText = formatResult(result)
            firstOperand = result
            isNewInput = true
            hasDecimal = displayText.contains(".")
        } catch (e: Exception) {
            hasError = true
            errorMessage = "计算错误"
            displayText = "0"
        }
    }
    
    private fun formatResult(result: BigDecimal): String {
        // 移除尾部的0和不必要的小数点
        return result.stripTrailingZeros().toPlainString().let {
            if (it == "0E-10") "0" else it
        }
    }
    
    // 清除错误状态
    private fun clearError() {
        if (hasError) {
            hasError = false
            errorMessage = ""
        }
    }
    
    // 切换科学计算器模式
    fun toggleScientificMode(): Boolean {
        isScientificMode = !isScientificMode
        return isScientificMode
    }
    
    // 获取科学计算器模式状态
    fun isScientificModeEnabled(): Boolean = isScientificMode
    
    // 清除历史记录
    fun clearHistory() {
        history = emptyList()
    }
    
    // 从历史记录中恢复计算
    fun restoreFromHistory(record: CalculationRecord) {
        displayText = record.result
        firstOperand = BigDecimal.ZERO
        secondOperand = BigDecimal.ZERO
        pendingOperation = null
        isNewInput = true
        hasDecimal = displayText.contains(".")
        clearError()
    }
    
    // 复制当前显示的值
    fun copyCurrentDisplay(): String = displayText
}