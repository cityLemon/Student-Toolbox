package com.example.guyuefangyuan.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guyuefangyuan.data.PomodoroRepository
import kotlinx.coroutines.launch

/**
 * 番茄钟设置界面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroSettingsScreen(
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val repository = remember { PomodoroRepository(context) }
    
    var settings by remember { mutableStateOf(repository.settings.value) }
    var hasChanges by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    
    // 监听设置变化
    LaunchedEffect(Unit) {
        repository.settings.collect { newSettings ->
            if (!hasChanges) {
                settings = newSettings
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("番茄钟设置") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    if (hasChanges) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    repository.updateSettings(settings)
                                    hasChanges = false
                                }
                            }
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "保存")
                        }
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 时间设置
            TimeSettingsSection(
                settings = settings,
                onSettingsChanged = { newSettings ->
                    settings = newSettings
                    hasChanges = true
                }
            )
            
            // 通知设置
            NotificationSettingsSection(
                settings = settings,
                onSettingsChanged = { newSettings ->
                    settings = newSettings
                    hasChanges = true
                }
            )
            
            // 自动开始设置
            AutoStartSettingsSection(
                settings = settings,
                onSettingsChanged = { newSettings ->
                    settings = newSettings
                    hasChanges = true
                }
            )
            
            // 重置设置按钮
            Button(
                onClick = {
                    scope.launch {
                        repository.resetSettings()
                        settings = repository.settings.value
                        hasChanges = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("重置为默认设置")
            }
        }
    }
}

/**
 * 时间设置部分
 */
@Composable
fun TimeSettingsSection(
    settings: com.example.guyuefangyuan.data.PomodoroSettings,
    onSettingsChanged: (com.example.guyuefangyuan.data.PomodoroSettings) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "时间设置",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            
            // 工作时间
            DurationInputField(
                label = "工作时间",
                value = (settings.workDuration / 60000).toInt(),
                onValueChanged = { minutes ->
                    onSettingsChanged(settings.copy(workDuration = minutes * 60000L))
                }
            )
            
            // 短休息时间
            DurationInputField(
                label = "短休息时间",
                value = (settings.shortBreakDuration / 60000).toInt(),
                onValueChanged = { minutes ->
                    onSettingsChanged(settings.copy(shortBreakDuration = minutes * 60000L))
                }
            )
            
            // 长休息时间
            DurationInputField(
                label = "长休息时间",
                value = (settings.longBreakDuration / 60000).toInt(),
                onValueChanged = { minutes ->
                    onSettingsChanged(settings.copy(longBreakDuration = minutes * 60000L))
                }
            )
            
            // 长休息间隔
            DurationInputField(
                label = "长休息间隔",
                value = settings.longBreakInterval,
                onValueChanged = { pomodoros ->
                    onSettingsChanged(settings.copy(longBreakInterval = pomodoros))
                },
                min = 2,
                max = 10,
                suffix = "个番茄钟"
            )
        }
    }
}

/**
 * 通知设置部分
 */
@Composable
fun NotificationSettingsSection(
    settings: com.example.guyuefangyuan.data.PomodoroSettings,
    onSettingsChanged: (com.example.guyuefangyuan.data.PomodoroSettings) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "通知设置",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            
            // 声音提醒
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("声音提醒")
                Switch(
                    checked = settings.soundEnabled,
                    onCheckedChange = { enabled ->
                        onSettingsChanged(settings.copy(soundEnabled = enabled))
                    }
                )
            }
            
            // 震动提醒
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("震动提醒")
                Switch(
                    checked = settings.vibrationEnabled,
                    onCheckedChange = { enabled ->
                        onSettingsChanged(settings.copy(vibrationEnabled = enabled))
                    }
                )
            }
        }
    }
}

/**
 * 自动开始设置部分
 */
@Composable
fun AutoStartSettingsSection(
    settings: com.example.guyuefangyuan.data.PomodoroSettings,
    onSettingsChanged: (com.example.guyuefangyuan.data.PomodoroSettings) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "自动开始",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            
            // 自动开始休息
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("自动开始休息")
                    Text(
                        text = "工作结束后自动开始休息",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = settings.autoStartBreak,
                    onCheckedChange = { enabled ->
                        onSettingsChanged(settings.copy(autoStartBreak = enabled))
                    }
                )
            }
            
            // 自动开始工作
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("自动开始工作")
                    Text(
                        text = "休息结束后自动开始工作",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = settings.autoStartWork,
                    onCheckedChange = { enabled ->
                        onSettingsChanged(settings.copy(autoStartWork = enabled))
                    }
                )
            }
        }
    }
}

/**
 * 时长输入字段
 */
@Composable
fun DurationInputField(
    label: String,
    value: Int,
    onValueChanged: (Int) -> Unit,
    min: Int = 1,
    max: Int = 60,
    suffix: String = "分钟"
) {
    var textValue by remember { mutableStateOf(value.toString()) }
    
    LaunchedEffect(value) {
        textValue = value.toString()
    }
    
    Column {
        Text(label)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = textValue,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                        textValue = newValue
                        newValue.toIntOrNull()?.let { intValue ->
                            if (intValue in min..max) {
                                onValueChanged(intValue)
                            }
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(100.dp),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(suffix)
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // 减少按钮
            IconButton(
                onClick = {
                    if (value > min) {
                        onValueChanged(value - 1)
                    }
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "减少"
                )
            }
            
            // 增加按钮
            IconButton(
                onClick = {
                    if (value < max) {
                        onValueChanged(value + 1)
                    }
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "增加"
                )
            }
        }
    }
}