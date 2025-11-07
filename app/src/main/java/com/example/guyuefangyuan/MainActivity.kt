package com.example.guyuefangyuan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.guyuefangyuan.data.UserPreferences
import com.example.guyuefangyuan.navigation.NavGraph
import com.example.guyuefangyuan.navigation.NavRoute
import com.example.guyuefangyuan.ui.theme.GuyuefangyuanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            GuyuefangyuanTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val context = LocalContext.current
                    val userPreferences = UserPreferences(context)
                    val isLoggedIn by userPreferences.isLoggedIn.collectAsState(initial = false)
                    
                    // 根据登录状态决定起始页面
                    val startDestination = if (isLoggedIn) NavRoute.HOME else NavRoute.LOGIN
                    
                    NavGraph(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}