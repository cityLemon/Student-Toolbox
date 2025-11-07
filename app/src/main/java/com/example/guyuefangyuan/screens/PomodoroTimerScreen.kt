package com.example.guyuefangyuan.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.guyuefangyuan.data.*
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * 番茄钟屏幕
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroTimerScreen(
    onNavigateToSettings: () -> Unit = {},
    onNavigateToStatistics: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val repository = remember { PomodoroRepository(context) }
    val timer = remember { PomodoroTimer(context, repository) }
    
    val pomodoroState by timer.currentState.collectAsStateWithLifecycle()
    val settings by repository.settings.collectAsStateWithLifecycle()
    val statistics by repository.statistics.collectAsStateWithLifecycle()
    
    val scope = rememberCoroutineScope()
    
    // 清理资源
    DisposableEffect(Unit) {
        onDispose {
            timer.cleanup()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("番茄钟") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToStatistics) {
                        Icon(Icons.Default.BarChart, contentDescription = "统计")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "设置")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 阶段选择器
            PhaseSelector(
                currentPhase = pomodoroState.phase,
                onPhaseSelected = { phase ->
                    timer.switchToPhase(phase)
                }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 计时器圆环
            TimerCircle(
                phase = pomodoroState.phase,
                remainingTime = pomodoroState.remainingTime,
                totalTime = when (pomodoroState.phase) {
                    PomodoroPhase.WORK -> pomodoroState.workDuration
                    PomodoroPhase.SHORT_BREAK -> pomodoroState.shortBreakDuration
                    PomodoroPhase.LONG_BREAK -> pomodoroState.longBreakDuration
                },
                isRunning = pomodoroState.isRunning
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 时间显示
            TimeDisplay(
                remainingTime = pomodoroState.remainingTime,
                phase = pomodoroState.phase
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 控制按钮
            ControlButtons(
                isRunning = pomodoroState.isRunning,
                onToggle = { timer.toggleTimer() },
                onReset = { timer.resetTimer() },
                onSkip = { timer.skipPhase() }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 统计信息
            StatisticsInfo(
                completedPomodoros = pomodoroState.completedPomodoros,
                todayCompleted = statistics.todayPomodoros,
                weeklyCompleted = statistics.todayPomodoros // 暂时使用今日数据代替本周数据
            )
        }
    }
}

/**
 * 阶段选择器
 */
@Composable
fun PhaseSelector(
    currentPhase: PomodoroPhase,
    onPhaseSelected: (PomodoroPhase) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        PhaseButton(
            text = "工作",
            isSelected = currentPhase == PomodoroPhase.WORK,
            color = Color(0xFFE57373),
            onClick = { onPhaseSelected(PomodoroPhase.WORK) }
        )
        
        PhaseButton(
            text = "短休息",
            isSelected = currentPhase == PomodoroPhase.SHORT_BREAK,
            color = Color(0xFF81C784),
            onClick = { onPhaseSelected(PomodoroPhase.SHORT_BREAK) }
        )
        
        PhaseButton(
            text = "长休息",
            isSelected = currentPhase == PomodoroPhase.LONG_BREAK,
            color = Color(0xFF64B5F6),
            onClick = { onPhaseSelected(PomodoroPhase.LONG_BREAK) }
        )
    }
}

/**
 * 阶段按钮
 */
@Composable
fun PhaseButton(
    text: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) color else Color.Transparent,
            contentColor = if (isSelected) Color.White else color
        ),
        border = if (!isSelected) BorderStroke(1.dp, color) else null,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text(text)
    }
}

/**
 * 计时器圆环
 */
@Composable
fun TimerCircle(
    phase: PomodoroPhase,
    remainingTime: Long,
    totalTime: Long,
    isRunning: Boolean
) {
    val progress = if (totalTime > 0) (totalTime - remainingTime).toFloat() / totalTime else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000),
        label = "progress"
    )
    
    val phaseColor = when (phase) {
        PomodoroPhase.WORK -> Color(0xFFE57373)
        PomodoroPhase.SHORT_BREAK -> Color(0xFF81C784)
        PomodoroPhase.LONG_BREAK -> Color(0xFF64B5F6)
    }
    
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(250.dp)
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            // 背景圆环
            drawCircle(
                color = Color.LightGray.copy(alpha = 0.3f),
                radius = size.width / 2 - 10.dp.toPx(),
                style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
            )
            
            // 进度圆环
            drawArc(
                color = phaseColor,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round),
                size = Size(size.width - 20.dp.toPx(), size.height - 20.dp.toPx()),
                topLeft = Offset(10.dp.toPx(), 10.dp.toPx())
            )
            
            // 如果正在运行，绘制刻度
            if (isRunning) {
                drawTickMarks(phaseColor, animatedProgress)
            }
        }
        
        // 中心图标
        Icon(
            imageVector = when (phase) {
                PomodoroPhase.WORK -> Icons.Default.Work
                PomodoroPhase.SHORT_BREAK -> Icons.Default.Coffee
                PomodoroPhase.LONG_BREAK -> Icons.Default.Bedtime
            },
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = phaseColor
        )
    }
}

/**
 * 绘制刻度线
 */
private fun DrawScope.drawTickMarks(color: Color, progress: Float) {
    val radius = size.width / 2 - 30.dp.toPx()
    val tickCount = 60
    val tickLength = 10.dp.toPx()
    
    for (i in 0 until tickCount) {
        val angle = (i * 360f / tickCount - 90f) * (PI / 180f)
        val startRadius = radius - tickLength / 2
        val endRadius = radius + tickLength / 2
        
        val startX = center.x + startRadius * cos(angle).toFloat()
        val startY = center.y + startRadius * sin(angle).toFloat()
        val endX = center.x + endRadius * cos(angle).toFloat()
        val endY = center.y + endRadius * sin(angle).toFloat()
        
        // 只绘制已完成的刻度
        if (i <= (tickCount * progress)) {
            drawLine(
                color = color,
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 2.dp.toPx()
            )
        }
    }
}

/**
 * 时间显示
 */
@Composable
fun TimeDisplay(
    remainingTime: Long,
    phase: PomodoroPhase
) {
    val minutes = (remainingTime / 60000).toInt()
    val seconds = ((remainingTime % 60000) / 1000).toInt()
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = String.format("%02d:%02d", minutes, seconds),
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Text(
            text = when (phase) {
                PomodoroPhase.WORK -> "专注时间"
                PomodoroPhase.SHORT_BREAK -> "短休息"
                PomodoroPhase.LONG_BREAK -> "长休息"
            },
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

/**
 * 控制按钮
 */
@Composable
fun ControlButtons(
    isRunning: Boolean,
    onToggle: () -> Unit,
    onReset: () -> Unit,
    onSkip: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 重置按钮
        IconButton(
            onClick = onReset,
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "重置",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // 开始/暂停按钮
        FloatingActionButton(
            onClick = onToggle,
            modifier = Modifier.size(80.dp),
            containerColor = if (isRunning) Color(0xFFFF9800) else Color(0xFF4CAF50)
        ) {
            Icon(
                imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isRunning) "暂停" else "开始",
                modifier = Modifier.size(40.dp),
                tint = Color.White
            )
        }
        
        // 跳过按钮
        IconButton(
            onClick = onSkip,
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "跳过",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 统计信息
 */
@Composable
fun StatisticsInfo(
    completedPomodoros: Int,
    todayCompleted: Int,
    weeklyCompleted: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatisticItem(
                value = completedPomodoros.toString(),
                label = "总计"
            )
            
            StatisticItem(
                value = todayCompleted.toString(),
                label = "今日"
            )
            
            StatisticItem(
                value = weeklyCompleted.toString(),
                label = "本周"
            )
        }
    }
}

/**
 * 统计项
 */
@Composable
fun StatisticItem(
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}