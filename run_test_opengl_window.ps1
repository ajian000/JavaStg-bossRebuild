# 运行TestOpenGLWindow测试类脚本
$classpath = "target\classes;resources;lib\lwjgl-3.3.2.jar;lib\lwjgl-opengl-3.3.2.jar;lib\lwjgl-openal-3.3.2.jar;lib\lwjgl-glfw-3.3.2.jar;lib\lwjgl-stb-3.3.2.jar;lib\lwjgl-3.3.2-natives-windows.jar;lib\lwjgl-glfw-3.3.2-natives-windows.jar;lib\lwjgl-opengl-3.3.2-natives-windows.jar;lib\lwjgl-openal-3.3.2-natives-windows.jar;lib\lwjgl-stb-3.3.2-natives-windows.jar"

Write-Host "Running TestOpenGLWindow..."
try {
    java -cp "$classpath" Main.TestOpenGLWindow
    Write-Host "Test completed!"
} catch {
    Write-Host "Test failed: $_"
    exit 1
}