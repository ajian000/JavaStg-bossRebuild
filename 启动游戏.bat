@echo off
chcp 65001 >nul
title JavaSTG 游戏启动器

echo ========================================
echo   JavaSTG 游戏启动器
echo ========================================
echo.

REM 检查jar包是否存在
if not exist "target\JavaStg-1.0-SNAPSHOT.jar" (
    echo [错误] 找不到jar包文件：target\JavaStg-1.0-SNAPSHOT.jar
    echo 请先运行 mvn clean package 打包项目
    echo.
    echo 按任意键退出...
    pause >nul
    exit /b 1
)

REM 检查resources文件夹是否存在
if not exist "resources" (
    echo [警告] 找不到resources文件夹
    echo 游戏可能无法正常加载资源文件
    echo.
    timeout /t 3 >nul
)

echo [信息] 正在启动游戏...
echo.

REM 运行jar包
java -jar target\JavaStg-1.0-SNAPSHOT.jar

REM 保存退出代码
set EXIT_CODE=%errorlevel%

REM 检查退出代码
if %EXIT_CODE% neq 0 (
    echo.
    echo [错误] 游戏异常退出，错误代码：%EXIT_CODE%
    echo.
    echo 按任意键退出...
    pause >nul
    exit /b %EXIT_CODE%
)

echo.
echo [信息] 游戏已正常退出
echo 按任意键退出...
pause >nul
