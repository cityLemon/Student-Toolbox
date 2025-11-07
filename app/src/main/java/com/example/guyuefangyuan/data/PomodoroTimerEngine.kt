package com.example.guyuefangyuan.data

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 番茄钟计时器类，处理计时逻辑和通知
 */
class PomodoroTimer(
    private val context: Context,
    private val repository: PomodoroRepository
) {
    private val scope = CoroutineScope(Dispatchers.Main)
    private var timerJob: Job? = null
    
    // 当前状态
    private val _currentState = MutableStateFlow(repository.pomodoroState.value)
    val currentState: StateFlow<PomodoroState> = _currentState.asStateFlow()
    
    // 初始化时同步状态
    init {
        // 监听仓库状态变化
        scope.launch {
            repository.pomodoroState.collect { state ->
                _currentState.value = state
            }
        }
    }
    
    /**
     * 开始或暂停计时器
     */
    fun toggleTimer() {
        val state = _currentState.value
        
        if (state.isRunning) {
            // 暂停计时器
            pauseTimer()
        } else {
            // 开始计时器
            startTimer()
        }
    }
    
    /**
     * 开始计时器
     */
    private fun startTimer() {
        val state = _currentState.value
        
        // 如果已经有一个计时器在运行，先取消它
        timerJob?.cancel()
        
        // 更新状态为运行中
        val newState = state.copy(isRunning = true)
        _currentState.value = newState
        repository.updatePomodoroState(newState)
        
        // 启动计时器
        timerJob = scope.launch {
            while (true) {
                delay(1000) // 每秒更新一次
                
                val currentState = _currentState.value
                if (currentState.remainingTime <= 1000) {
                    // 时间到了，切换到下一阶段
                    handlePhaseComplete()
                    break
                } else {
                    // 更新剩余时间
                    val updatedState = currentState.copy(
                        remainingTime = currentState.remainingTime - 1000
                    )
                    _currentState.value = updatedState
                    repository.updatePomodoroState(updatedState)
                }
            }
        }
    }
    
    /**
     * 暂停计时器
     */
    private fun pauseTimer() {
        timerJob?.cancel()
        
        val state = _currentState.value
        val newState = state.copy(isRunning = false)
        _currentState.value = newState
        repository.updatePomodoroState(newState)
    }
    
    /**
     * 重置计时器
     */
    fun resetTimer() {
        timerJob?.cancel()
        
        val state = _currentState.value
        val duration = when (state.phase) {
            PomodoroPhase.WORK -> state.workDuration
            PomodoroPhase.SHORT_BREAK -> state.shortBreakDuration
            PomodoroPhase.LONG_BREAK -> state.longBreakDuration
        }
        
        val newState = state.copy(
            remainingTime = duration,
            isRunning = false
        )
        _currentState.value = newState
        repository.updatePomodoroState(newState)
    }
    
    /**
     * 跳过当前阶段
     */
    fun skipPhase() {
        timerJob?.cancel()
        handlePhaseComplete()
    }
    
    /**
     * 切换到下一阶段
     */
    private fun handlePhaseComplete() {
        val state = _currentState.value
        val settings = repository.settings.value
        
        // 播放通知
        playNotification(state)
        
        // 根据当前阶段决定下一阶段
        val nextPhase = when (state.phase) {
            PomodoroPhase.WORK -> {
                // 完成一个番茄钟
                repository.completePomodoro()
                
                // 判断是短休息还是长休息
                if (state.completedPomodoros % settings.longBreakInterval == 0) {
                    PomodoroPhase.LONG_BREAK
                } else {
                    PomodoroPhase.SHORT_BREAK
                }
            }
            PomodoroPhase.SHORT_BREAK, PomodoroPhase.LONG_BREAK -> {
                // 休息结束，回到工作
                PomodoroPhase.WORK
            }
        }
        
        // 设置下一阶段的时长
        val nextDuration = when (nextPhase) {
            PomodoroPhase.WORK -> state.workDuration
            PomodoroPhase.SHORT_BREAK -> state.shortBreakDuration
            PomodoroPhase.LONG_BREAK -> state.longBreakDuration
        }
        
        // 更新状态
        val newState = state.copy(
            phase = nextPhase,
            remainingTime = nextDuration,
            isRunning = false
        )
        _currentState.value = newState
        repository.updatePomodoroState(newState)
        
        // 如果设置了自动开始，则自动开始下一阶段
        if ((state.phase == PomodoroPhase.WORK && settings.autoStartBreak) ||
            (state.phase != PomodoroPhase.WORK && settings.autoStartWork)) {
            startTimer()
        }
    }
    
    /**
     * 切换到指定阶段
     */
    fun switchToPhase(phase: PomodoroPhase) {
        timerJob?.cancel()
        
        val state = _currentState.value
        val duration = when (phase) {
            PomodoroPhase.WORK -> state.workDuration
            PomodoroPhase.SHORT_BREAK -> state.shortBreakDuration
            PomodoroPhase.LONG_BREAK -> state.longBreakDuration
        }
        
        val newState = state.copy(
            phase = phase,
            remainingTime = duration,
            isRunning = false
        )
        _currentState.value = newState
        repository.updatePomodoroState(newState)
    }
    
    /**
     * 播放通知（声音和/或震动）
     */
    private fun playNotification(state: PomodoroState) {
        // 播放声音
        if (state.soundEnabled) {
            playSound()
        }
        
        // 震动提醒
        if (state.vibrationEnabled) {
            vibrate()
        }
    }
    
    /**
     * 播放提示音
     */
    private fun playSound() {
        try {
            // 使用系统默认通知声音
            val mediaPlayer = MediaPlayer()
            mediaPlayer.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            )
            mediaPlayer.setDataSource(context, Settings.System.DEFAULT_NOTIFICATION_URI)
            mediaPlayer.prepare()
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { mp ->
                mp.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * 震动提醒
     */
    private fun vibrate() {
        try {
            val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
            
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(500)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * 清理资源
     */
    fun cleanup() {
        timerJob?.cancel()
    }
}