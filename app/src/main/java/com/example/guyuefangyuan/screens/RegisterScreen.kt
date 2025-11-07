package com.example.guyuefangyuan.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guyuefangyuan.R
import com.example.guyuefangyuan.components.ErrorDisplay
import com.example.guyuefangyuan.components.EnhancedCard
import com.example.guyuefangyuan.components.EnhancedGradientButton
import com.example.guyuefangyuan.components.EnhancedPasswordTextField
import com.example.guyuefangyuan.components.EnhancedTextField
import com.example.guyuefangyuan.data.User
import com.example.guyuefangyuan.data.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

private suspend fun validateAndRegister(
    username: String,
    password: String,
    confirmPassword: String,
    email: String,
    userPreferences: UserPreferences,
    onError: (String) -> Unit,
    onSuccess: () -> Unit
) {
    try {
        // 检查用户名是否已存在
        val users = userPreferences.users.first()
        if (users.any { it.username == username }) {
            onError("用户名已存在，请使用其他用户名")
            return
        }
        
        // 验证密码长度
        if (password.length < 6) {
            onError("密码长度不能少于6个字符")
            return
        }
        
        // 验证密码匹配
        if (password != confirmPassword) {
            onError("两次输入的密码不一致")
            return
        }
        
        // 创建用户
        val newUser = User(
            username = username,
            password = password,
            email = email
        )
        
        // 保存用户
        userPreferences.saveUser(newUser)
        userPreferences.setCurrentUser(newUser)
        
        // 注册成功
        onSuccess()
    } catch (e: Exception) {
        onError("注册失败：${e.message}")
    }
}

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    
    // 添加渐变背景
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 应用图标和标题卡片
            EnhancedCard(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 32.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 应用图标
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "应用图标",
                        modifier = Modifier.size(80.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 应用名称
                    Text(
                        text = "学生工具箱",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "创建新账号",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            // 注册表单卡片
            EnhancedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "注册",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    
                    // 用户名输入
                    EnhancedTextField(
                        value = username,
                        onValueChange = { 
                            username = it
                            usernameError = false
                        },
                        label = "用户名",
                        isError = usernameError,
                        errorMessage = "请输入用户名",
                        imeAction = ImeAction.Next
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 邮箱输入
                    EnhancedTextField(
                        value = email,
                        onValueChange = { 
                            email = it
                            emailError = false
                        },
                        label = "邮箱",
                        isError = emailError,
                        errorMessage = "请输入邮箱",
                        imeAction = ImeAction.Next
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 密码输入
                    EnhancedPasswordTextField(
                        value = password,
                        onValueChange = { 
                            password = it
                            passwordError = false
                        },
                        label = "密码",
                        isError = passwordError,
                        errorMessage = "请输入密码",
                        onAction = { 
                            if (username.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank() && email.isNotBlank()) {
                                isLoading = true
                                coroutineScope.launch {
                                    validateAndRegister(
                                        username = username,
                                        password = password,
                                        confirmPassword = confirmPassword,
                                        email = email,
                                        userPreferences = userPreferences,
                                        onError = { errorMessage ->
                                            launch {
                                                snackbarHostState.showSnackbar(errorMessage)
                                            }
                                        },
                                        onSuccess = {
                                            onNavigateToHome()
                                        }
                                    )
                                }
                            } else {
                                usernameError = username.isBlank()
                                passwordError = password.isBlank()
                                confirmPasswordError = confirmPassword.isBlank()
                                emailError = email.isBlank()
                            }
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 确认密码输入
                    EnhancedPasswordTextField(
                        value = confirmPassword,
                        onValueChange = { 
                            confirmPassword = it
                            confirmPasswordError = false
                        },
                        label = "确认密码",
                        isError = confirmPasswordError,
                        errorMessage = "请确认密码",
                        onAction = { 
                            if (username.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank() && email.isNotBlank()) {
                                isLoading = true
                                coroutineScope.launch {
                                    validateAndRegister(
                                        username = username,
                                        password = password,
                                        confirmPassword = confirmPassword,
                                        email = email,
                                        userPreferences = userPreferences,
                                        onError = { errorMessage ->
                                            launch {
                                                snackbarHostState.showSnackbar(errorMessage)
                                            }
                                        },
                                        onSuccess = {
                                            onNavigateToHome()
                                        }
                                    )
                                }
                            } else {
                                usernameError = username.isBlank()
                                passwordError = password.isBlank()
                                confirmPasswordError = confirmPassword.isBlank()
                                emailError = email.isBlank()
                            }
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // 注册按钮
                    EnhancedGradientButton(
                        onClick = { 
                            if (username.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank() && email.isNotBlank()) {
                                isLoading = true
                                coroutineScope.launch {
                                    validateAndRegister(
                                        username = username,
                                        password = password,
                                        confirmPassword = confirmPassword,
                                        email = email,
                                        userPreferences = userPreferences,
                                        onError = { errorMessage ->
                                            launch {
                                                snackbarHostState.showSnackbar(errorMessage)
                                            }
                                            isLoading = false
                                        },
                                        onSuccess = {
                                            isLoading = false
                                            onNavigateToHome()
                                        }
                                    )
                                }
                            } else {
                                usernameError = username.isBlank()
                                passwordError = password.isBlank()
                                confirmPasswordError = confirmPassword.isBlank()
                                emailError = email.isBlank()
                            }
                        },
                        text = "注册",
                        enabled = !isLoading,
                        isLoading = isLoading
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // 登录链接
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "已有账号？",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "立即登录",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onNavigateToLogin() }
                        )
                    }
                }
            }
            
            // 错误提示
            ErrorDisplay(snackbarHostState = snackbarHostState)
        }
    }
}