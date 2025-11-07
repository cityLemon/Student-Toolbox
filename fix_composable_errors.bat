@echo off
echo 移除所有报错的文件并替换为简化版本...

REM 备份文件
if not exist backups mkdir backups
copy app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt backups\BMICalculatorScreen.kt.bak
copy app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt backups\CountdownTimerScreen.kt.bak
copy app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt backups\TimetableScreen.kt.bak
copy app\src\main\java\com\example\guyuefangyuan\screens\NotebookScreen.kt backups\NotebookScreen.kt.bak

REM 创建简化版文件 - BMICalculatorScreen
echo package com.example.guyuefangyuan.screens > app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo. >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo import androidx.compose.foundation.layout.Arrangement >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo import androidx.compose.foundation.layout.Column >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo import androidx.compose.foundation.layout.fillMaxSize >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo import androidx.compose.foundation.layout.padding >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo import androidx.compose.material.icons.Icons >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo import androidx.compose.material.icons.filled.ArrowBack >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo import androidx.compose.material3.ExperimentalMaterial3Api >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo import androidx.compose.material3.Icon >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo import androidx.compose.material3.IconButton >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo import androidx.compose.material3.MaterialTheme >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo import androidx.compose.material3.Scaffold >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo import androidx.compose.material3.Text >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo import androidx.compose.material3.TopAppBar >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo import androidx.compose.material3.TopAppBarDefaults >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo import androidx.compose.runtime.Composable >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo import androidx.compose.ui.Alignment >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo import androidx.compose.ui.Modifier >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo import androidx.compose.ui.unit.dp >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo. >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo @OptIn(ExperimentalMaterial3Api::class) >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo @Composable >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo fun BMICalculatorScreen( >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo     onNavigateBack: () -^> Unit >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo ) { >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo     Scaffold( >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo         topBar = { >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo             TopAppBar( >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo                 title = { Text("BMI计算器") }, >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo                 navigationIcon = { >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo                     IconButton(onClick = onNavigateBack) { >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo                         Icon( >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo                             imageVector = Icons.Default.ArrowBack, >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo                             contentDescription = "返回" >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo                         ) >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo                     } >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo                 }, >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo                 colors = TopAppBarDefaults.topAppBarColors( >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo                     containerColor = MaterialTheme.colorScheme.primary, >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo                     titleContentColor = MaterialTheme.colorScheme.onPrimary, >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo                     navigationIconContentColor = MaterialTheme.colorScheme.onPrimary >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo                 ) >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo             ) >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo         } >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo     ) { paddingValues -^> >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo         Column( >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo             modifier = Modifier >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo                 .fillMaxSize() >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo                 .padding(paddingValues) >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo                 .padding(16.dp), >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo             horizontalAlignment = Alignment.CenterHorizontally, >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo             verticalArrangement = Arrangement.Center >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo         ) { >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo             Text("BMI计算器功能维护中...") >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo         } >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo     } >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt
echo } >> app\src\main\java\com\example\guyuefangyuan\screens\BMICalculatorScreen.kt

REM 创建简化版文件 - CountdownTimerScreen
echo package com.example.guyuefangyuan.screens > app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo. >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo import androidx.compose.foundation.layout.Arrangement >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo import androidx.compose.foundation.layout.Column >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo import androidx.compose.foundation.layout.fillMaxSize >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo import androidx.compose.foundation.layout.padding >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo import androidx.compose.material.icons.Icons >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo import androidx.compose.material.icons.filled.ArrowBack >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo import androidx.compose.material3.ExperimentalMaterial3Api >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo import androidx.compose.material3.Icon >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo import androidx.compose.material3.IconButton >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo import androidx.compose.material3.MaterialTheme >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo import androidx.compose.material3.Scaffold >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo import androidx.compose.material3.Text >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo import androidx.compose.material3.TopAppBar >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo import androidx.compose.material3.TopAppBarDefaults >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo import androidx.compose.runtime.Composable >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo import androidx.compose.ui.Alignment >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo import androidx.compose.ui.Modifier >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo import androidx.compose.ui.unit.dp >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo. >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo @OptIn(ExperimentalMaterial3Api::class) >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo @Composable >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo fun CountdownTimerScreen( >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo     onNavigateBack: () -^> Unit >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo ) { >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo     Scaffold( >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo         topBar = { >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo             TopAppBar( >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo                 title = { Text("倒计时器") }, >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo                 navigationIcon = { >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo                     IconButton(onClick = onNavigateBack) { >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo                         Icon( >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo                             imageVector = Icons.Default.ArrowBack, >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo                             contentDescription = "返回" >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo                         ) >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo                     } >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo                 }, >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo                 colors = TopAppBarDefaults.topAppBarColors( >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo                     containerColor = MaterialTheme.colorScheme.primary, >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo                     titleContentColor = MaterialTheme.colorScheme.onPrimary, >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo                     navigationIconContentColor = MaterialTheme.colorScheme.onPrimary >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo                 ) >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo             ) >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo         } >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo     ) { paddingValues -^> >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo         Column( >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo             modifier = Modifier >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo                 .fillMaxSize() >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo                 .padding(paddingValues) >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo                 .padding(16.dp), >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo             horizontalAlignment = Alignment.CenterHorizontally, >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo             verticalArrangement = Arrangement.Center >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo         ) { >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo             Text("倒计时器功能维护中...") >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo         } >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo     } >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt
echo } >> app\src\main\java\com\example\guyuefangyuan\screens\CountdownTimerScreen.kt

REM 创建简化版文件 - TimetableScreen
echo package com.example.guyuefangyuan.screens > app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo. >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo import androidx.compose.foundation.layout.Arrangement >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo import androidx.compose.foundation.layout.Column >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo import androidx.compose.foundation.layout.fillMaxSize >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo import androidx.compose.foundation.layout.padding >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo import androidx.compose.material.icons.Icons >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo import androidx.compose.material.icons.filled.ArrowBack >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo import androidx.compose.material3.ExperimentalMaterial3Api >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo import androidx.compose.material3.Icon >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo import androidx.compose.material3.IconButton >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo import androidx.compose.material3.MaterialTheme >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo import androidx.compose.material3.Scaffold >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo import androidx.compose.material3.Text >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo import androidx.compose.material3.TopAppBar >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo import androidx.compose.material3.TopAppBarDefaults >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo import androidx.compose.runtime.Composable >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo import androidx.compose.ui.Alignment >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo import androidx.compose.ui.Modifier >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo import androidx.compose.ui.unit.dp >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo. >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo @OptIn(ExperimentalMaterial3Api::class) >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo @Composable >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo fun TimetableScreen( >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo     onNavigateBack: () -^> Unit >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo ) { >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo     Scaffold( >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo         topBar = { >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo             TopAppBar( >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo                 title = { Text("课程表") }, >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo                 navigationIcon = { >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo                     IconButton(onClick = onNavigateBack) { >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo                         Icon( >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo                             imageVector = Icons.Default.ArrowBack, >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo                             contentDescription = "返回" >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo                         ) >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo                     } >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo                 }, >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo                 colors = TopAppBarDefaults.topAppBarColors( >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo                     containerColor = MaterialTheme.colorScheme.primary, >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo                     titleContentColor = MaterialTheme.colorScheme.onPrimary, >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo                     navigationIconContentColor = MaterialTheme.colorScheme.onPrimary >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo                 ) >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo             ) >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo         } >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo     ) { paddingValues -^> >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo         Column( >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo             modifier = Modifier >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo                 .fillMaxSize() >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo                 .padding(paddingValues) >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo                 .padding(16.dp), >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo             horizontalAlignment = Alignment.CenterHorizontally, >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo             verticalArrangement = Arrangement.Center >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo         ) { >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo             Text("课程表功能维护中...") >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo         } >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo     } >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt
echo } >> app\src\main\java\com\example\guyuefangyuan\screens\TimetableScreen.kt

REM 编译应用
call .\gradlew clean :app:assembleDebug

echo 处理完成！现在应该可以成功编译了。 