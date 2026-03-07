# 构建脚本：编译到bin目录并清理src目录下的.class文件

# 设置变量
$srcDir = "src"
$binDir = "bin"

# 创建bin目录（如果不存在）
if (-not (Test-Path "$binDir")) {
    New-Item -ItemType Directory -Path "$binDir" -Force
    Write-Host "创建bin目录"
}

# 清理bin目录
Write-Host "清理bin目录..."
Remove-Item "$binDir\*" -Recurse -Force -ErrorAction SilentlyContinue

# 查找所有Java文件
Write-Host "查找Java文件..."
$javaFiles = Get-ChildItem -Path "$srcDir" -Recurse -Filter "*.java"

if ($javaFiles.Count -eq 0) {
    Write-Host "未找到Java文件"
    exit 1
}

# 编译Java文件到bin目录
Write-Host "编译Java文件到bin目录..."
try {
    $classpath = "lib\lwjgl-3.3.2.jar;lib\lwjgl-opengl-3.3.2.jar;lib\lwjgl-openal-3.3.2.jar;lib\lwjgl-glfw-3.3.2.jar;lib\lwjgl-stb-3.3.2.jar"
    javac -cp "$classpath" -d "$binDir" $javaFiles.FullName
    Write-Host "编译成功!"
} catch {
    Write-Host "编译失败: $_"
    exit 1
}

# 清理src目录下的.class文件
Write-Host "清理src目录下的.class文件..."
Get-ChildItem -Path "$srcDir" -Recurse -Filter "*.class" | Remove-Item -Force

Write-Host "构建完成！所有编译文件已放在bin目录，src目录下的编译文件已清理。"
