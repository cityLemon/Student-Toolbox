package com.example.guyuefangyuan.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.guyuefangyuan.components.CalculatorButton
import com.example.guyuefangyuan.components.CalculatorButtonType
import com.example.guyuefangyuan.components.CalculatorDisplay
import com.example.guyuefangyuan.components.StandardTopAppBar
import com.example.guyuefangyuan.data.Calculator

@Composable
fun CalculatorScreen(
    onNavigateBack: () -> Unit
) {
    val calculator = remember { Calculator() }
    val snackbarHostState = remember { SnackbarHostState() }
    
    Scaffold(
        topBar = {
            StandardTopAppBar(
                title = "计算器",
                onNavigateBack = onNavigateBack
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 显示屏
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.BottomEnd
            ) {
                CalculatorDisplay(
                    text = calculator.displayText,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // 按钮区域
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .padding(8.dp)
            ) {
                // 第一行：清除、删除、百分比、除法
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalculatorButton(
                        symbol = "C",
                        type = CalculatorButtonType.FUNCTION,
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onClear() }
                    )
                    CalculatorButton(
                        symbol = "⌫",
                        type = CalculatorButtonType.FUNCTION,
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onDelete() }
                    )
                    CalculatorButton(
                        symbol = "%",
                        type = CalculatorButtonType.FUNCTION,
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onPercentage() }
                    )
                    CalculatorButton(
                        symbol = "÷",
                        type = CalculatorButtonType.OPERATION,
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onOperation(Calculator.Operation.DIVIDE) }
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 第二行：7、8、9、乘法
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalculatorButton(
                        symbol = "7",
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onDigit(7) }
                    )
                    CalculatorButton(
                        symbol = "8",
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onDigit(8) }
                    )
                    CalculatorButton(
                        symbol = "9",
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onDigit(9) }
                    )
                    CalculatorButton(
                        symbol = "×",
                        type = CalculatorButtonType.OPERATION,
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onOperation(Calculator.Operation.MULTIPLY) }
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 第三行：4、5、6、减法
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalculatorButton(
                        symbol = "4",
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onDigit(4) }
                    )
                    CalculatorButton(
                        symbol = "5",
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onDigit(5) }
                    )
                    CalculatorButton(
                        symbol = "6",
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onDigit(6) }
                    )
                    CalculatorButton(
                        symbol = "-",
                        type = CalculatorButtonType.OPERATION,
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onOperation(Calculator.Operation.SUBTRACT) }
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 第四行：1、2、3、加法
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalculatorButton(
                        symbol = "1",
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onDigit(1) }
                    )
                    CalculatorButton(
                        symbol = "2",
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onDigit(2) }
                    )
                    CalculatorButton(
                        symbol = "3",
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onDigit(3) }
                    )
                    CalculatorButton(
                        symbol = "+",
                        type = CalculatorButtonType.OPERATION,
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onOperation(Calculator.Operation.ADD) }
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 第五行：正负号、0、小数点、等于
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalculatorButton(
                        symbol = "±",
                        type = CalculatorButtonType.FUNCTION,
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onSignChange() }
                    )
                    CalculatorButton(
                        symbol = "0",
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onDigit(0) }
                    )
                    CalculatorButton(
                        symbol = ".",
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onDecimal() }
                    )
                    CalculatorButton(
                        symbol = "=",
                        type = CalculatorButtonType.EQUALS,
                        modifier = Modifier.weight(1f),
                        onClick = { calculator.onEquals() }
                    )
                }
            }
        }
    }
} 