package com.example.guyuefangyuan.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 番茄钟阶段枚举
 */
enum class PomodoroPhase {
    WORK,       // 工作时间
    SHORT_BREAK, // 短休息
    LONG_BREAK   // 长休息
}

/**
 * 番茄钟状态
 */
data class PomodoroState(
    val phase: PomodoroPhase = PomodoroPhase.WORK,
    val remainingTime: Long = 25 * 60 * 1000L, // 默认25分钟，单位毫秒
    val isRunning: Boolean = false,
    val completedPomodoros: Int = 0, // 完成的番茄钟数量
    val currentCycle: Int = 1, // 当前周期（每4个番茄钟为一个周期）
    val workDuration: Long = 25 * 60 * 1000L, // 工作时长，默认25分钟
    val shortBreakDuration: Long = 5 * 60 * 1000L, // 短休息时长，默认5分钟
    val longBreakDuration: Long = 15 * 60 * 1000L, // 长休息时长，默认15分钟
    val soundEnabled: Boolean = true, // 是否启用声音提醒
    val vibrationEnabled: Boolean = true // 是否启用震动提醒
)

/**
 * 番茄钟设置
 */
data class PomodoroSettings(
    val workDuration: Long = 25 * 60 * 1000L, // 工作时长，默认25分钟
    val shortBreakDuration: Long = 5 * 60 * 1000L, // 短休息时长，默认5分钟
    val longBreakDuration: Long = 15 * 60 * 1000L, // 长休息时长，默认15分钟
    val longBreakInterval: Int = 4, // 长休息间隔，默认每4个番茄钟后长休息
    val soundEnabled: Boolean = true, // 是否启用声音提醒
    val vibrationEnabled: Boolean = true, // 是否启用震动提醒
    val autoStartBreak: Boolean = false, // 是否自动开始休息
    val autoStartWork: Boolean = false // 是否自动开始工作
)

/**
 * 番茄钟统计数据
 */
data class PomodoroStatistics(
    val todayPomodoros: Int = 0, // 今日完成的番茄钟数量
    val totalPomodoros: Int = 0, // 总共完成的番茄钟数量
    val todayWorkTime: Long = 0L, // 今日工作时间（毫秒）
    val totalWorkTime: Long = 0L, // 总工作时间（毫秒）
    val currentStreak: Int = 0, // 当前连续完成天数
    val longestStreak: Int = 0 // 最长连续完成天数
)

/**
 * 每日统计数据
 */
data class DailyStatistic(
    val date: String, // 日期，格式为 yyyy-MM-dd
    val pomodoros: Int, // 当日完成的番茄钟数量
    val workTime: Long // 当日工作时间（毫秒）
)