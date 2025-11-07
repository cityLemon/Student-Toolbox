package com.example.guyuefangyuan.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.guyuefangyuan.data.PomodoroRepository
import com.example.guyuefangyuan.data.PomodoroStatistics
import java.text.SimpleDateFormat
import java.util.*

/**
 * 番茄钟统计界面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroStatisticsScreen(
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val repository = remember { PomodoroRepository(context) }
    
    val statistics by repository.statistics.collectAsStateWithLifecycle()
    val dailyStats by repository.dailyStats.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("番茄钟统计") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 总体统计卡片
            OverallStatisticsCard(statistics = statistics)
            
            // 今日统计卡片
            TodayStatisticsCard(statistics = statistics)
            
            // 本周统计卡片
            WeeklyStatisticsCard(statistics = statistics)
            
            // 每日统计图表
            DailyStatisticsChart(dailyStats = dailyStats)
            
            // 重置统计数据按钮
            Button(
                onClick = {
                    repository.resetStatistics()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("重置统计数据")
            }
        }
    }
}

/**
 * 总体统计卡片
 */
@Composable
fun OverallStatisticsCard(
    statistics: PomodoroStatistics
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "总体统计",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatisticItem(
                    value = statistics.totalPomodoros.toString(),
                    label = "总完成数",
                    color = MaterialTheme.colorScheme.primary
                )
                
                StatisticItem(
                    value = formatDuration(statistics.totalWorkTime),
                    label = "总专注时间",
                    color = MaterialTheme.colorScheme.secondary
                )
                
                StatisticItem(
                    value = calculateAverageFocusTime(statistics).toString(),
                    label = "平均专注时间(分钟)",
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

/**
 * 今日统计卡片
 */
@Composable
fun TodayStatisticsCard(
    statistics: PomodoroStatistics
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "今日统计",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatisticItem(
                    value = statistics.todayPomodoros.toString(),
                    label = "今日完成",
                    color = Color(0xFFFF9800)
                )
                
                StatisticItem(
                    value = formatDuration(statistics.todayWorkTime),
                    label = "今日专注时间",
                    color = Color(0xFF03A9F4)
                )
                
                StatisticItem(
                    value = calculateDailyGoalProgress(statistics).toString() + "%",
                    label = "每日目标进度",
                    color = Color(0xFFFF9800)
                )
            }
        }
    }
}

/**
 * 本周统计卡片
 */
@Composable
fun WeeklyStatisticsCard(
    statistics: PomodoroStatistics
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "本周统计",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatisticItem(
                    value = statistics.todayPomodoros.toString(), // 暂时使用今日数据代替本周数据
                    label = "本周完成",
                    color = Color(0xFF9C27B0)
                )
                
                StatisticItem(
                    value = formatDuration(statistics.todayWorkTime), // 暂时使用今日数据代替本周数据
                    label = "本周专注时间",
                    color = Color(0xFF00BCD4)
                )
                
                StatisticItem(
                    value = calculateWeeklyAverage(statistics).toString(),
                    label = "日均完成",
                    color = Color(0xFF607D8B)
                )
            }
        }
    }
}

/**
 * 每日统计图表
 */
@Composable
fun DailyStatisticsChart(
    dailyStats: List<com.example.guyuefangyuan.data.DailyStatistic>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "最近7天统计",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 简单的柱状图
            if (dailyStats.isNotEmpty()) {
                val maxValue = dailyStats.maxOfOrNull { it.pomodoros } ?: 1
                
                Column {
                    dailyStats.forEach { dailyStat ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = dailyStat.date,
                                modifier = Modifier.width(80.dp),
                                fontSize = 12.sp
                            )
                            
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(24.dp)
                                    .padding(horizontal = 8.dp)
                            ) {
                                // 背景条
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth()
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = MaterialTheme.shapes.small
                                        )
                                )
                                
                                // 进度条
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(dailyStat.pomodoros.toFloat() / maxValue)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = MaterialTheme.shapes.small
                                        )
                                )
                            }
                            
                            Text(
                                text = dailyStat.pomodoros.toString(),
                                modifier = Modifier.width(30.dp),
                                fontSize = 12.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.End
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            } else {
                Text(
                    text = "暂无数据",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 统计项组件
 */
@Composable
fun StatisticItem(
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 格式化时长
 */
private fun formatDuration(milliseconds: Long): String {
    val hours = milliseconds / (1000 * 60 * 60)
    val minutes = (milliseconds % (1000 * 60 * 60)) / (1000 * 60)
    
    return if (hours > 0) {
        "${hours}小时${minutes}分钟"
    } else {
        "${minutes}分钟"
    }
}

/**
 * 计算平均专注时间（分钟）
 */
private fun calculateAverageFocusTime(statistics: PomodoroStatistics): Int {
    return if (statistics.totalPomodoros > 0) {
        (statistics.totalWorkTime / (1000 * 60) / statistics.totalPomodoros).toInt()
    } else {
        0
    }
}

/**
 * 计算每日目标进度（假设每日目标是8个番茄钟）
 */
private fun calculateDailyGoalProgress(statistics: PomodoroStatistics): Int {
    val dailyGoal = 8
    return (statistics.todayPomodoros.toFloat() / dailyGoal * 100).toInt().coerceAtMost(100)
}

/**
 * 计算本周日均完成数
 */
private fun calculateWeeklyAverage(statistics: PomodoroStatistics): Int {
    // 暂时使用今日数据作为本周平均值的估算
    return statistics.todayPomodoros
}