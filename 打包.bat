@echo off
chcp 65001 >nul
title JavaSTG 打包工具

echo ========================================
echo   JavaSTG 打包工具
echo ========================================
echo.

echo [信息] 正在清理并打包项目...
echo.

REM 执行Maven打包命令
call mvn clean package

REM 检查打包是否成功
if %errorlevel% neq 0 (
    echo.
    echo [错误] 打包失败！请检查错误信息
    echo.
    pause
    exit /b %errorlevel%
)

echo.
echo ========================================
echo [成功] 打包完成！
echo ========================================
echo.
echo 生成的jar包位置：
echo   target\JavaStg-1.0-SNAPSHOT.jar ^(10.8MB, 包含所有依赖^)
echo.
echo 下一步：
echo   1. 双击运行 "启动游戏.bat" 启动游戏
echo   2. 或者在命令行运行：java -jar target\JavaStg-1.0-SNAPSHOT.jar
echo.
pause
