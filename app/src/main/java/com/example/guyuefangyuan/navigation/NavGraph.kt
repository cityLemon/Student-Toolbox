package com.example.guyuefangyuan.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.guyuefangyuan.screens.CalculatorScreen
import com.example.guyuefangyuan.screens.HomeScreen
import com.example.guyuefangyuan.screens.LoginScreen
import com.example.guyuefangyuan.screens.NotesScreen
import com.example.guyuefangyuan.screens.ProfileScreen
import com.example.guyuefangyuan.screens.RegisterScreen

// 定义导航路由
object NavRoute {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val CALCULATOR = "calculator"
    const val NOTES = "notes"
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
                onNavigateToNotes = { navController.navigate(NavRoute.NOTES) },
                onNavigateToProfile = { navController.navigate(NavRoute.PROFILE) }
            )
        }
        
        composable(NavRoute.CALCULATOR) {
            CalculatorScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        composable(NavRoute.NOTES) {
            NotesScreen(
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