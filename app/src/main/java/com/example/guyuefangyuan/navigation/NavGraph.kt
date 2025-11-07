package com.example.guyuefangyuan.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.guyuefangyuan.screens.BMICalculatorScreen
import com.example.guyuefangyuan.screens.CalculatorScreen
import com.example.guyuefangyuan.screens.CountdownTimerScreen
import com.example.guyuefangyuan.screens.HomeScreen
import com.example.guyuefangyuan.screens.LoginScreen
import com.example.guyuefangyuan.screens.NotesScreen
import com.example.guyuefangyuan.screens.ProfileScreen
import com.example.guyuefangyuan.screens.RegisterScreen
import com.example.guyuefangyuan.screens.TimetableScreen

// 定义导航路由
object NavRoute {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val CALCULATOR = "calculator"
    const val BMI_CALCULATOR = "bmi_calculator"
    const val COUNTDOWN_TIMER = "countdown_timer"
    const val NOTES = "notes"
    const val TIMETABLE = "timetable"
    const val PROFILE = "profile"
}

@Composable
fun NavGraph(navController: NavHostController, startDestination: String = NavRoute.LOGIN) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavRoute.LOGIN) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(NavRoute.REGISTER) },
                onNavigateToHome = { navController.navigate(NavRoute.HOME) {
                    popUpTo(NavRoute.LOGIN) { inclusive = true }
                }}
            )
        }
        
        composable(NavRoute.REGISTER) {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate(NavRoute.LOGIN) {
                    popUpTo(NavRoute.REGISTER) { inclusive = true }
                }},
                onNavigateToHome = { navController.navigate(NavRoute.HOME) {
                    popUpTo(NavRoute.REGISTER) { inclusive = true }
                }}
            )
        }
        
        composable(NavRoute.HOME) {
            HomeScreen(
                onNavigateToCalculator = { navController.navigate(NavRoute.CALCULATOR) },
                onNavigateToBMICalculator = { navController.navigate(NavRoute.BMI_CALCULATOR) },
                onNavigateToCountdownTimer = { navController.navigate(NavRoute.COUNTDOWN_TIMER) },
                onNavigateToNotes = { navController.navigate(NavRoute.NOTES) },
                onNavigateToTimetable = { navController.navigate(NavRoute.TIMETABLE) },
                onNavigateToProfile = { navController.navigate(NavRoute.PROFILE) }
            )
        }
        
        composable(NavRoute.CALCULATOR) {
            CalculatorScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        composable(NavRoute.BMI_CALCULATOR) {
            BMICalculatorScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        composable(NavRoute.COUNTDOWN_TIMER) {
            CountdownTimerScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        composable(NavRoute.NOTES) {
            NotesScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        composable(NavRoute.TIMETABLE) {
            TimetableScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        composable(NavRoute.PROFILE) {
            ProfileScreen(
                onNavigateBack = { navController.navigateUp() },
                onLogout = { navController.navigate(NavRoute.LOGIN) {
                    popUpTo(NavRoute.HOME) { inclusive = true }
                }}
            )
        }
    }
}