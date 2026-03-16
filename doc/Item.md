# Item 类文档

## 类概述
`Item` 是所有物品的基类，继承自 `Obj` 类，实现了 `Resettable` 接口。包括道具、掉落物、特殊物品等。

## 成员变量

### 1. attractionDistance (float)
**用途**：道具吸引的距离阈值
**默认值**：150.0f

### 2. attractionSpeed (float)
**用途**：道具吸引的速度
**默认值**：3.0f

## 构造方法

### 1. Item(float x, float y, float size, Color color)
**用途**：创建物品对象
**参数**：
- `x` (float)：物品的初始X坐标
- `y` (float)：物品的初始Y坐标
- `size` (float)：物品的大小
- `color` (Color)：物品的颜色
**默认值**：
- 速度：0, 0

### 2. Item(float x, float y, float vx, float vy, float size, Color color)
**用途**：创建物品对象
**参数**：
- `x` (float)：物品的初始X坐标
- `y` (float)：物品的初始Y坐标
- `vx` (float)：物品的X方向速度
- `vy` (float)：物品的Y方向速度
- `size` (float)：物品的大小
- `color` (Color)：物品的颜色

## 方法说明

### 1. update()
**用途**：更新物品状态
**参数**：无
**返回值**：无
**重写**：重写了 `Obj` 类的方法
**说明**：
- 检查物品是否超出边界
- 子类可重写此方法添加特定行为

### 2. render(Graphics2D g)
**用途**：渲染物品（Java2D版本）
**参数**：
- `g` (Graphics2D)：用于绘制的图形上下文对象
**返回值**：无
**重写**：重写了 `Obj` 类的方法
**说明**：
- 绘制物品主体和高光效果
- 启用抗锯齿以获得更好的视觉效果

### 3. render(IRenderer renderer)
**用途**：渲染物品（支持OpenGL）
**参数**：
- `renderer` (IRenderer)：渲染器
**返回值**：无

### 4. onCollect()
**用途**：物品被玩家拾取时的处理
**参数**：无
**返回值**：无
**说明**：
- 默认将物品设置为不活跃
- 子类可重写此方法实现特定效果

### 5. onTaskStart()
**用途**：任务开始时触发的方法，用于处理开局对话等
**参数**：无
**返回值**：无

### 6. onTaskEnd()
**用途**：任务结束时触发的方法，用于处理boss击破对话和道具掉落
**参数**：无
**返回值**：无

### 7. applyAttraction()
**用途**：应用道具吸引逻辑
**参数**：无
**返回值**：无
**说明**：简化实现，不再依赖GameCanvas

### 8. setAttractionParams(float distance, float speed)
**用途**：设置吸引参数
**参数**：
- `distance` (float)：吸引距离
- `speed` (float)：吸引速度
**返回值**：无

### 9. resetState()
**用途**：重置道具状态（对象池回收时调用）
**参数**：无
**返回值**：无
**说明**：
- 重置道具的基本属性
- 重置吸引参数到默认值
- 实现了 Resettable 接口

## 实现的接口

### 从 Resettable 接口继承
- `resetState()`：重置对象状态

## 继承的方法

### 从 Obj 类继承
- `getX()`：获取X坐标
- `getY()`：获取Y坐标
- `setX(float x)`：设置X坐标
- `setY(float y)`：设置Y坐标
- `getVx()`：获取X方向速度
- `getVy()`：获取Y方向速度
- `setVx(float vx)`：设置X方向速度
- `setVy(float vy)`：设置Y方向速度
- `getSize()`：获取大小
- `setSize(float size)`：设置大小
- `getColor()`：获取颜色
- `setColor(Color color)`：设置颜色
- `isActive()`：检查是否活跃
- `setActive(boolean active)`：设置是否活跃
- `getHitboxRadius()`：获取碰撞检测半径
- `toScreenCoords(float x, float y)`：将游戏坐标转换为屏幕坐标
- `isOutOfBounds()`：检查对象是否超出边界
- `update()`：更新对象状态
- `render(Graphics2D g)`：渲染对象