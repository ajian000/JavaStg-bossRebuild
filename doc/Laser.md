# Laser 类文档

## 类概述
`Laser` 是所有激光的基类，定义了激光的基本行为和属性，包括预警机制、碰撞检测、OpenGL 渲染支持等功能。

## 成员变量

### 1. x (float)
**用途**：激光起点的X坐标

### 2. y (float)
**用途**：激光起点的Y坐标

### 3. angle (float)
**用途**：激光的角度（弧度）

### 4. length (float)
**用途**：激光的长度

### 5. width (float)
**用途**：激光的宽度

### 6. color (Color)
**用途**：激光的颜色

### 7. gameCanvas (GameCanvas)
**用途**：画布引用

### 8. warningOnly (boolean)
**用途**：是否仅显示预警线
**默认值**：false

### 9. warningTime (int)
**用途**：预警持续时间（帧）

### 10. warningTimer (int)
**用途**：预警计时器

### 11. active (boolean)
**用途**：激光是否激活（预警结束后）
**默认值**：false

### 12. visible (boolean)
**用途**：激光是否可见
**默认值**：true

### 13. damage (int)
**用途**：激光的伤害值

## 构造方法

### 1. Laser(float x, float y, float angle, float length, float width, Color color)
**用途**：创建激光对象
**参数**：
- `x` (float)：起点X坐标
- `y` (float)：起点Y坐标
- `angle` (float)：角度（弧度）
- `length` (float)：长度
- `width` (float)：宽度
- `color` (Color)：颜色
**默认值**：
- 预警时间：60帧
- 伤害值：10

### 2. Laser(float x, float y, float angle, float length, float width, Color color, int warningTime, int damage)
**用途**：创建激光对象（完整参数）
**参数**：
- `x` (float)：起点X坐标
- `y` (float)：起点Y坐标
- `angle` (float)：角度（弧度）
- `length` (float)：长度
- `width` (float)：宽度
- `color` (Color)：颜色
- `warningTime` (int)：预警时间（帧）
- `damage` (int)：伤害值

## 方法说明

### 1. initBehavior()
**用途**：初始化行为参数
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现，在构造函数中调用

### 2. onUpdate()
**用途**：实现每帧的自定义更新逻辑
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现

### 3. onMove()
**用途**：实现自定义移动逻辑
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现

### 4. update()
**用途**：更新激光状态
**参数**：无
**返回值**：无
**说明**：
- 调用自定义更新逻辑
- 调用自定义移动逻辑
- 处理预警计时器
- 预警结束后激活激光

### 5. render(Graphics g)
**用途**：渲染激光（Java2D版本）
**参数**：
- `g` (Graphics)：图形上下文
**返回值**：无
**说明**：
- 渲染预警线或实际激光
- 启用抗锯齿以获得更好的视觉效果

### 6. render(IRenderer renderer)
**用途**：渲染激光（支持OpenGL）
**参数**：
- `renderer` (IRenderer)：渲染器
**返回值**：无

### 7. renderWarningLine(Graphics2D g2d)
**用途**：渲染预警线（Java2D版本）
**参数**：
- `g2d` (Graphics2D)：图形上下文
**返回值**：无

### 8. renderLaser(Graphics2D g2d)
**用途**：渲染实际激光（Java2D版本）
**参数**：
- `g2d` (Graphics2D)：图形上下文
**返回值**：无

### 9. renderWarningLineGL(IRenderer renderer)
**用途**：渲染预警线（OpenGL版本）
**参数**：
- `renderer` (IRenderer)：渲染器
**返回值**：无

### 10. renderLaserGL(IRenderer renderer)
**用途**：渲染实际激光（OpenGL版本）
**参数**：
- `renderer` (IRenderer)：渲染器
**返回值**：无

### 11. checkCollision(float px, float py)
**用途**：检查点是否在激光碰撞体内
**参数**：
- `px` (float)：点的X坐标
- `py` (float)：点的Y坐标
**返回值**：boolean - 是否碰撞

### 12. isOutOfBounds(int width, int height)
**用途**：检查激光是否超出边界
**参数**：
- `width` (int)：画布宽度
- `height` (int)：画布高度
**返回值**：boolean - 是否超出边界

### 13. pointToLineDistance(float px, float py, float lx, float ly, float angle, float len)
**用途**：计算点到线段的距离
**参数**：
- `px` (float)：点X坐标
- `py` (float)：点Y坐标
- `lx` (float)：线段起点X坐标
- `ly` (float)：线段起点Y坐标
- `angle` (float)：线段角度
- `len` (float)：线段长度
**返回值**：float - 点到线段的距离

### 14. reset()
**用途**：重置激光状态
**参数**：无
**返回值**：无
**说明**：
- 重置预警计时器
- 重置激活状态
- 重置可见性
- 重新初始化行为参数

### 15. onTaskStart()
**用途**：任务开始时触发的方法，用于处理开局对话等
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现

### 16. onTaskEnd()
**用途**：任务结束时触发的方法，用于处理boss击破对话和道具掉落
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现

## Getter 和 Setter 方法

### Getter 方法
- `getX()`：获取激光起点X坐标
- `getY()`：获取激光起点Y坐标
- `getAngle()`：获取激光角度
- `getLength()`：获取激光长度
- `getWidth()`：获取激光宽度
- `getColor()`：获取激光颜色
- `isWarningOnly()`：检查是否仅显示预警线
- `getWarningTime()`：获取预警时间
- `isActive()`：检查激光是否激活
- `isVisible()`：检查激光是否可见
- `getDamage()`：获取激光伤害值

### Setter 方法
- `setX(float x)`：设置激光起点X坐标
- `setY(float y)`：设置激光起点Y坐标
- `setAngle(float angle)`：设置激光角度
- `setLength(float length)`：设置激光长度
- `setWidth(float width)`：设置激光宽度
- `setColor(Color color)`：设置激光颜色
- `setWarningOnly(boolean warningOnly)`：设置是否仅显示预警线
- `setWarningTime(int warningTime)`：设置预警时间
- `setVisible(boolean visible)`：设置激光是否可见
- `setDamage(int damage)`：设置激光伤害值