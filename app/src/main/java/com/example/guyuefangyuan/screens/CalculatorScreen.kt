package com.example.guyuefangyuan.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guyuefangyuan.components.CalculatorButton
import com.example.guyuefangyuan.components.CalculatorButtonType
import com.example.guyuefangyuan.components.CalculatorDisplay
import com.example.guyuefangyuan.components.CalculatorHistory
import com.example.guyuefangyuan.components.StandardTopAppBar
import com.example.guyuefangyuan.data.Calculator
import com.example.guyuefangyuan.data.CalculationRecord
import kotlin.math.PI
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    onNavigateBack: () -> Unit
) {
    val calculator = remember { Calculator() }
    val snackbarHostState = remember { SnackbarHostState() }
    val clipboardManager = LocalClipboardManager.current
    
    // UI状态
    var showHistory by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = if (calculator.isScientificModeEnabled()) "科学计算器" else "计算器",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showMenu = !showMenu }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "更多选项"
                        )
                    }
                    
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(if (calculator.isScientificModeEnabled()) "标准模式" else "科学模式") },
                            onClick = { 
                                calculator.toggleScientificMode()
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.SwapHoriz,
                                    contentDescription = null
                                )
                            }
                        )
                        
                        DropdownMenuItem(
                            text = { Text("历史记录") },
                            onClick = { 
                                showHistory = !showHistory
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            if (!showHistory) {
                FloatingActionButton(
                    onClick = { 
                        val text = calculator.copyCurrentDisplay()
                        clipboardManager.setText(AnnotatedString(text))
                        // 可以添加一个提示消息
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentPaste,
                        contentDescription = "复制结果"
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                )
        ) {
            if (showHistory) {
                CalculatorHistory(
                    history = calculator.history,
                    onHistoryItemClick = { record ->
                        calculator.restoreFromHistory(record)
                        showHistory = false
                    },
                    onCopyResult = { result ->
                        clipboardManager.setText(AnnotatedString(result))
                        // 可以添加一个提示消息
                    },
                    onClearHistory = {
                        calculator.clearHistory()
                        // 可以添加一个提示消息
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                CalculatorContent(
                    calculator = calculator,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "计算器预览",
    device = "spec:width=360dp,height=640dp"
)
@Composable
fun CalculatorScreenPreview() {
    MaterialTheme {
        CalculatorScreen(
            onNavigateBack = { /* 预览中不执行导航 */ }
        )
    }
}

@Composable
fun CalculatorContent(
    calculator: Calculator,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
                hasError = calculator.hasError,
                errorMessage = calculator.errorMessage,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        // 按钮区域
        if (calculator.isScientificModeEnabled()) {
            ScientificCalculatorButtons(
                calculator = calculator,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .padding(8.dp)
            )
        } else {
            StandardCalculatorButtons(
                calculator = calculator,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun StandardCalculatorButtons(
    calculator: Calculator,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        // 第一行：清除、删除、百分比、除法
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                symbol = "C",
                type = CalculatorButtonType.FUNCTION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onClear() }
            )
            CalculatorButton(
                symbol = "π",
                type = CalculatorButtonType.SCIENTIFIC,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { 
                    // 将π值作为字符串输入，避免类型不匹配
                    calculator.onDecimal()
                    calculator.onDigit(3)
                    calculator.onDigit(1)
                    calculator.onDigit(4)
                    calculator.onDigit(1)
                    calculator.onDigit(5)
                    calculator.onDigit(9)
                }
            )
            CalculatorButton(
                symbol = "e",
                type = CalculatorButtonType.SCIENTIFIC,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onOperation(Calculator.Operation.NATURAL_LOG) }
            )
            CalculatorButton(
                symbol = "⌫",
                type = CalculatorButtonType.FUNCTION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDelete() }
            )
            CalculatorButton(
                symbol = "÷",
                type = CalculatorButtonType.OPERATION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
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
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDigit(7) }
            )
            CalculatorButton(
                symbol = "8",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDigit(8) }
            )
            CalculatorButton(
                symbol = "9",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDigit(9) }
            )
            CalculatorButton(
                symbol = "×",
                type = CalculatorButtonType.OPERATION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
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
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDigit(4) }
            )
            CalculatorButton(
                symbol = "5",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDigit(5) }
            )
            CalculatorButton(
                symbol = "6",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDigit(6) }
            )
            CalculatorButton(
                symbol = "-",
                type = CalculatorButtonType.OPERATION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
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
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDigit(1) }
            )
            CalculatorButton(
                symbol = "2",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDigit(2) }
            )
            CalculatorButton(
                symbol = "3",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDigit(3) }
            )
            CalculatorButton(
                symbol = "+",
                type = CalculatorButtonType.OPERATION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
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
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onSignChange() }
            )
            CalculatorButton(
                symbol = "0",
                modifier = Modifier
                    .weight(2f)
                    .aspectRatio(2f),
                onClick = { calculator.onDigit(0) }
            )
            CalculatorButton(
                symbol = ".",
                type = CalculatorButtonType.FUNCTION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDecimal() }
            )
            CalculatorButton(
                symbol = "=",
                type = CalculatorButtonType.EQUALS,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onEquals() }
            )
        }
    }
}

@Composable
fun ScientificCalculatorButtons(
    calculator: Calculator,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        // 科学计算器第一行：sin、cos、tan、清除、删除
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                symbol = "sin",
                type = CalculatorButtonType.SCIENTIFIC,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onOperation(Calculator.Operation.SINE) }
            )
            CalculatorButton(
                symbol = "cos",
                type = CalculatorButtonType.SCIENTIFIC,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onOperation(Calculator.Operation.COSINE) }
            )
            CalculatorButton(
                symbol = "tan",
                type = CalculatorButtonType.SCIENTIFIC,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onOperation(Calculator.Operation.TANGENT) }
            )
            CalculatorButton(
                symbol = "log",
                type = CalculatorButtonType.SCIENTIFIC,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onOperation(Calculator.Operation.LOGARITHM) }
            )
            CalculatorButton(
                symbol = "⌫",
                type = CalculatorButtonType.FUNCTION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDelete() }
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 科学计算器第二行：x²、√、x^y、π、%
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                symbol = "x²",
                type = CalculatorButtonType.SCIENTIFIC,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onOperation(Calculator.Operation.POWER) }
            )
            CalculatorButton(
                symbol = "√",
                type = CalculatorButtonType.SCIENTIFIC,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onOperation(Calculator.Operation.SQUARE_ROOT) }
            )
            CalculatorButton(
                symbol = "x^y",
                type = CalculatorButtonType.SCIENTIFIC,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onOperation(Calculator.Operation.POWER) }
            )
            CalculatorButton(
                symbol = "C",
                type = CalculatorButtonType.FUNCTION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onClear() }
            )
            CalculatorButton(
                symbol = "%",
                type = CalculatorButtonType.FUNCTION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onPercentage() }
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 科学计算器第三行：7、8、9、÷、(
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                symbol = "7",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDigit(7) }
            )
            CalculatorButton(
                symbol = "8",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDigit(8) }
            )
            CalculatorButton(
                symbol = "9",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDigit(9) }
            )
            CalculatorButton(
                symbol = "÷",
                type = CalculatorButtonType.OPERATION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onOperation(Calculator.Operation.DIVIDE) }
            )
            CalculatorButton(
                symbol = "(",
                type = CalculatorButtonType.FUNCTION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { /* TODO: 括号功能 */ }
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 科学计算器第四行：4、5、6、×、)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                symbol = "4",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDigit(4) }
            )
            CalculatorButton(
                symbol = "5",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDigit(5) }
            )
            CalculatorButton(
                symbol = "6",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDigit(6) }
            )
            CalculatorButton(
                symbol = "×",
                type = CalculatorButtonType.OPERATION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onOperation(Calculator.Operation.MULTIPLY) }
            )
            CalculatorButton(
                symbol = ")",
                type = CalculatorButtonType.FUNCTION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { /* TODO: 括号功能 */ }
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 科学计算器第五行：1、2、3、-、±
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                symbol = "1",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDigit(1) }
            )
            CalculatorButton(
                symbol = "2",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDigit(2) }
            )
            CalculatorButton(
                symbol = "3",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDigit(3) }
            )
            CalculatorButton(
                symbol = "-",
                type = CalculatorButtonType.OPERATION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onOperation(Calculator.Operation.SUBTRACT) }
            )
            CalculatorButton(
                symbol = "±",
                type = CalculatorButtonType.FUNCTION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onSignChange() }
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 科学计算器第六行：0、.、+、=
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                symbol = "0",
                modifier = Modifier
                    .weight(2f)
                    .aspectRatio(2f),
                onClick = { calculator.onDigit(0) }
            )
            CalculatorButton(
                symbol = ".",
                type = CalculatorButtonType.FUNCTION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onDecimal() }
            )
            CalculatorButton(
                symbol = "+",
                type = CalculatorButtonType.OPERATION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onOperation(Calculator.Operation.ADD) }
            )
            CalculatorButton(
                symbol = "=",
                type = CalculatorButtonType.EQUALS,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { calculator.onEquals() }
            )
        }
    }
}