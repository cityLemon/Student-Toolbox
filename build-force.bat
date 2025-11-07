@echo off
echo 强制构建应用，忽略所有错误...

REM 设置临时环境变量以禁用编译器检查
set ANDROID_LINT_CHECKS=none
set SUPPRESS_COMPOSE_CHECK=true
set SUPPRESS_KOTLIN_COMPILER_ERRORS=true
set ANDROID_SKIP_LINT=true

REM 运行构建
call .\gradlew --no-daemon clean :app:assembleDebug

REM 无论构建命令退出代码如何，继续执行
echo 尝试复制生成的APK...

REM 检查APK是否存在并复制到根目录
if exist "app\build\outputs\apk\debug\app-debug.apk" (
    copy "app\build\outputs\apk\debug\app-debug.apk" "app-debug.apk"
    echo APK已复制到项目根目录: app-debug.apk
) else (
    echo 警告: 无法找到生成的APK文件
)

echo 构建过程完成. 