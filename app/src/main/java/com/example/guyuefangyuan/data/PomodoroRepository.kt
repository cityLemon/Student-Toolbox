package com.example.guyuefangyuan.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

/**
 * 番茄钟仓库类，管理番茄钟状态和数据持久化
 */
class PomodoroRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    // 番茄钟状态
    private val _pomodoroState = MutableStateFlow(
        PomodoroState(
            workDuration = prefs.getLong(KEY_WORK_DURATION, 25 * 60 * 1000L),
            shortBreakDuration = prefs.getLong(KEY_SHORT_BREAK_DURATION, 5 * 60 * 1000L),
            longBreakDuration = prefs.getLong(KEY_LONG_BREAK_DURATION, 15 * 60 * 1000L),
            soundEnabled = prefs.getBoolean(KEY_SOUND_ENABLED, true),
            vibrationEnabled = prefs.getBoolean(KEY_VIBRATION_ENABLED, true)
        )
    )
    val pomodoroState: StateFlow<PomodoroState> = _pomodoroState.asStateFlow()
    
    // 番茄钟设置
    private val _settings = MutableStateFlow(
        PomodoroSettings(
            workDuration = prefs.getLong(KEY_WORK_DURATION, 25 * 60 * 1000L),
            shortBreakDuration = prefs.getLong(KEY_SHORT_BREAK_DURATION, 5 * 60 * 1000L),
            longBreakDuration = prefs.getLong(KEY_LONG_BREAK_DURATION, 15 * 60 * 1000L),
            longBreakInterval = prefs.getInt(KEY_LONG_BREAK_INTERVAL, 4),
            soundEnabled = prefs.getBoolean(KEY_SOUND_ENABLED, true),
            vibrationEnabled = prefs.getBoolean(KEY_VIBRATION_ENABLED, true),
            autoStartBreak = prefs.getBoolean(KEY_AUTO_START_BREAK, false),
            autoStartWork = prefs.getBoolean(KEY_AUTO_START_WORK, false)
        )
    )
    val settings: StateFlow<PomodoroSettings> = _settings.asStateFlow()
    
    // 番茄钟统计
    private val _statistics = MutableStateFlow(
        PomodoroStatistics(
            todayPomodoros = prefs.getInt(KEY_TODAY_POMODOROS, 0),
            totalPomodoros = prefs.getInt(KEY_TOTAL_POMODOROS, 0),
            todayWorkTime = prefs.getLong(KEY_TODAY_WORK_TIME, 0L),
            totalWorkTime = prefs.getLong(KEY_TOTAL_WORK_TIME, 0L),
            currentStreak = prefs.getInt(KEY_CURRENT_STREAK, 0),
            longestStreak = prefs.getInt(KEY_LONGEST_STREAK, 0)
        )
    )
    val statistics: StateFlow<PomodoroStatistics> = _statistics.asStateFlow()
    
    // 每日统计数据
    private val _dailyStats = MutableStateFlow<List<DailyStatistic>>(emptyList())
    val dailyStats: StateFlow<List<DailyStatistic>> = _dailyStats.asStateFlow()
    
    init {
        // 初始化每日统计数据
        loadDailyStats()
    }
    
    // 更新番茄钟状态
    fun updatePomodoroState(newState: PomodoroState) {
        _pomodoroState.value = newState
        
        // 保存关键状态到SharedPreferences
        prefs.edit {
            putLong(KEY_WORK_DURATION, newState.workDuration)
            putLong(KEY_SHORT_BREAK_DURATION, newState.shortBreakDuration)
            putLong(KEY_LONG_BREAK_DURATION, newState.longBreakDuration)
            putBoolean(KEY_SOUND_ENABLED, newState.soundEnabled)
            putBoolean(KEY_VIBRATION_ENABLED, newState.vibrationEnabled)
        }
    }
    
    // 更新设置
    fun updateSettings(newSettings: PomodoroSettings) {
        _settings.value = newSettings
        
        // 保存设置到SharedPreferences
        prefs.edit {
            putLong(KEY_WORK_DURATION, newSettings.workDuration)
            putLong(KEY_SHORT_BREAK_DURATION, newSettings.shortBreakDuration)
            putLong(KEY_LONG_BREAK_DURATION, newSettings.longBreakDuration)
            putInt(KEY_LONG_BREAK_INTERVAL, newSettings.longBreakInterval)
            putBoolean(KEY_SOUND_ENABLED, newSettings.soundEnabled)
            putBoolean(KEY_VIBRATION_ENABLED, newSettings.vibrationEnabled)
            putBoolean(KEY_AUTO_START_BREAK, newSettings.autoStartBreak)
            putBoolean(KEY_AUTO_START_WORK, newSettings.autoStartWork)
        }
        
        // 同时更新番茄钟状态中的设置
        val currentState = _pomodoroState.value
        updatePomodoroState(
            currentState.copy(
                workDuration = newSettings.workDuration,
                shortBreakDuration = newSettings.shortBreakDuration,
                longBreakDuration = newSettings.longBreakDuration,
                soundEnabled = newSettings.soundEnabled,
                vibrationEnabled = newSettings.vibrationEnabled
            )
        )
    }
    
    // 完成一个番茄钟
    fun completePomodoro() {
        val currentState = _pomodoroState.value
        val currentStats = _statistics.value
        val currentSettings = _settings.value
        
        // 更新番茄钟状态
        val newCompletedPomodoros = currentState.completedPomodoros + 1
        val newCycle = if (newCompletedPomodoros % currentSettings.longBreakInterval == 0) {
            currentState.currentCycle + 1
        } else {
            currentState.currentCycle
        }
        
        _pomodoroState.value = currentState.copy(
            completedPomodoros = newCompletedPomodoros,
            currentCycle = newCycle
        )
        
        // 更新统计数据
        val today = getTodayString()
        val lastActiveDate = prefs.getString(KEY_LAST_ACTIVE_DATE, "")
        
        val newCurrentStreak = if (lastActiveDate == today) {
            currentStats.currentStreak
        } else if (isYesterday(lastActiveDate)) {
            currentStats.currentStreak + 1
        } else {
            1
        }
        
        val newLongestStreak = maxOf(currentStats.longestStreak, newCurrentStreak)
        
        val newTodayPomodoros = if (lastActiveDate == today) {
            currentStats.todayPomodoros + 1
        } else {
            1
        }
        
        val newTodayWorkTime = if (lastActiveDate == today) {
            currentStats.todayWorkTime + currentState.workDuration
        } else {
            currentState.workDuration
        }
        
        val newStats = currentStats.copy(
            todayPomodoros = newTodayPomodoros,
            totalPomodoros = currentStats.totalPomodoros + 1,
            todayWorkTime = newTodayWorkTime,
            totalWorkTime = currentStats.totalWorkTime + currentState.workDuration,
            currentStreak = newCurrentStreak,
            longestStreak = newLongestStreak
        )
        
        _statistics.value = newStats
        
        // 更新每日统计数据
        updateDailyStats(today, newTodayPomodoros, newTodayWorkTime)
        
        // 保存统计数据
        prefs.edit {
            putString(KEY_LAST_ACTIVE_DATE, today)
            putInt(KEY_TODAY_POMODOROS, newStats.todayPomodoros)
            putInt(KEY_TOTAL_POMODOROS, newStats.totalPomodoros)
            putLong(KEY_TODAY_WORK_TIME, newStats.todayWorkTime)
            putLong(KEY_TOTAL_WORK_TIME, newStats.totalWorkTime)
            putInt(KEY_CURRENT_STREAK, newStats.currentStreak)
            putInt(KEY_LONGEST_STREAK, newStats.longestStreak)
        }
    }
    
    // 重置统计数据
    fun resetStatistics() {
        val newStats = PomodoroStatistics()
        _statistics.value = newStats
        
        // 重置每日统计数据
        _dailyStats.value = emptyList()
        
        prefs.edit {
            remove(KEY_TODAY_POMODOROS)
            remove(KEY_TOTAL_POMODOROS)
            remove(KEY_TODAY_WORK_TIME)
            remove(KEY_TOTAL_WORK_TIME)
            remove(KEY_CURRENT_STREAK)
            remove(KEY_LONGEST_STREAK)
            remove(KEY_LAST_ACTIVE_DATE)
        }
    }
    
    // 重置设置
    fun resetSettings() {
        val defaultSettings = PomodoroSettings()
        _settings.value = defaultSettings
        
        prefs.edit {
            putLong(KEY_WORK_DURATION, defaultSettings.workDuration)
            putLong(KEY_SHORT_BREAK_DURATION, defaultSettings.shortBreakDuration)
            putLong(KEY_LONG_BREAK_DURATION, defaultSettings.longBreakDuration)
            putInt(KEY_LONG_BREAK_INTERVAL, defaultSettings.longBreakInterval)
            putBoolean(KEY_SOUND_ENABLED, defaultSettings.soundEnabled)
            putBoolean(KEY_VIBRATION_ENABLED, defaultSettings.vibrationEnabled)
            putBoolean(KEY_AUTO_START_BREAK, defaultSettings.autoStartBreak)
            putBoolean(KEY_AUTO_START_WORK, defaultSettings.autoStartWork)
        }
    }
    
    // 加载每日统计数据
    private fun loadDailyStats() {
        // 这里可以扩展为从SharedPreferences或数据库加载历史数据
        // 目前先创建一些示例数据
        val today = getTodayString()
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        
        val stats = mutableListOf<DailyStatistic>()
        
        // 添加今日数据
        stats.add(
            DailyStatistic(
                date = today,
                pomodoros = _statistics.value.todayPomodoros,
                workTime = _statistics.value.todayWorkTime
            )
        )
        
        // 添加过去6天的示例数据
        for (i in 1..6) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            val date = dateFormat.format(calendar.time)
            
            // 生成一些随机示例数据
            stats.add(
                DailyStatistic(
                    date = date,
                    pomodoros = (0..8).random(),
                    workTime = (0..8).random() * 25 * 60 * 1000L
                )
            )
        }
        
        _dailyStats.value = stats
    }
    
    // 更新每日统计数据
    private fun updateDailyStats(date: String, pomodoros: Int, workTime: Long) {
        val currentStats = _dailyStats.value.toMutableList()
        
        // 查找是否已有该日期的数据
        val existingIndex = currentStats.indexOfFirst { it.date == date }
        
        if (existingIndex >= 0) {
            // 更新现有数据
            currentStats[existingIndex] = currentStats[existingIndex].copy(
                pomodoros = pomodoros,
                workTime = workTime
            )
        } else {
            // 添加新数据
            currentStats.add(DailyStatistic(date, pomodoros, workTime))
            // 按日期排序
            currentStats.sortByDescending { it.date }
        }
        
        _dailyStats.value = currentStats
    }
    
    // 获取今天的日期字符串
    private fun getTodayString(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
    
    // 判断给定日期字符串是否是昨天
    private fun isYesterday(dateString: String?): Boolean {
        if (dateString.isNullOrEmpty()) return false
        
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(dateString) ?: return false
        
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        
        val tomorrow = dateFormat.format(calendar.time)
        return tomorrow == getTodayString()
    }
    
    companion object {
        private const val PREFS_NAME = "pomodoro_prefs"
        
        // 设置键
        private const val KEY_WORK_DURATION = "work_duration"
        private const val KEY_SHORT_BREAK_DURATION = "short_break_duration"
        private const val KEY_LONG_BREAK_DURATION = "long_break_duration"
        private const val KEY_LONG_BREAK_INTERVAL = "long_break_interval"
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_VIBRATION_ENABLED = "vibration_enabled"
        private const val KEY_AUTO_START_BREAK = "auto_start_break"
        private const val KEY_AUTO_START_WORK = "auto_start_work"
        
        // 统计键
        private const val KEY_TODAY_POMODOROS = "today_pomodoros"
        private const val KEY_TOTAL_POMODOROS = "total_pomodoros"
        private const val KEY_TODAY_WORK_TIME = "today_work_time"
        private const val KEY_TOTAL_WORK_TIME = "total_work_time"
        private const val KEY_CURRENT_STREAK = "current_streak"
        private const val KEY_LONGEST_STREAK = "longest_streak"
        private const val KEY_LAST_ACTIVE_DATE = "last_active_date"
    }
}