# Enemy 类文档

## 类概述
`Enemy` 是所有敌人的基类，继承自 `Obj` 类，实现了 `Resettable` 接口。定义了敌人的基本行为和属性，包括生命值管理、伤害处理、渲染等功能。

## 成员变量

### 1. hp (int)
**用途**：敌人的当前生命值

### 2. maxHp (int)
**用途**：敌人的最大生命值

### 3. gameWorld (GameWorld)
**用途**：游戏世界引用

## 构造方法

### 1. Enemy(int x, int y)
**用途**：创建敌人对象，使用默认参数
**参数**：
- `x` (int)：敌人的X坐标
- `y` (int)：敌人的Y坐标
**默认值**：
- 速度：0, 0
- 大小：20
- 颜色：蓝色
- 生命值：10

### 2. Enemy(float x, float y)
**用途**：创建敌人对象，使用默认参数
**参数**：
- `x` (float)：敌人的X坐标
- `y` (float)：敌人的Y坐标
**默认值**：
- 速度：0, 0
- 大小：20
- 颜色：蓝色
- 生命值：10

### 3. Enemy(float x, float y, float vx, float vy, float size, Color color, int hp)
**用途**：创建敌人对象，使用指定参数
**参数**：
- `x` (float)：敌人的X坐标
- `y` (float)：敌人的Y坐标
- `vx` (float)：敌人的X方向速度
- `vy` (float)：敌人的Y方向速度
- `size` (float)：敌人的大小
- `color` (Color)：敌人的颜色
- `hp` (int)：敌人的生命值

## 方法说明

### 1. update()
**用途**：更新敌人状态，包括移动和生命值检查
**参数**：无
**返回值**：无
**重写**：重写了 `Obj` 类的方法
**说明**：
- 调用父类的 update 方法
- 检查生命值，如果生命值小于等于0，设置为不活跃

### 2. update(int canvasWidth, int canvasHeight)
**用途**：更新敌人状态（支持动态画布尺寸）
**参数**：
- `canvasWidth` (int)：画布宽度
- `canvasHeight` (int)：画布高度
**返回值**：无
**说明**：调用无参数版本的 update 方法

### 3. render(Graphics2D g)
**用途**：渲染敌人，包括绘制敌人本体和生命值条（Java2D版本）
**参数**：
- `g` (Graphics2D)：用于绘制的图形上下文对象
**返回值**：无
**重写**：重写了 `Obj` 类的方法

### 4. render(IRenderer renderer)
**用途**：渲染敌人（支持OpenGL）
**参数**：
- `renderer` (IRenderer)：渲染器
**返回值**：无

### 5. renderHealthBar(IRenderer renderer)
**用途**：渲染生命值条（IRenderer版本）
**参数**：
- `renderer` (IRenderer)：渲染器
**返回值**：无

### 6. renderHealthBar(Graphics2D g, float screenX, float screenY)
**用途**：渲染敌人的生命值条（Java2D版本）
**参数**：
- `g` (Graphics2D)：用于绘制的图形上下文对象
- `screenX` (float)：敌人在屏幕上的X坐标
- `screenY` (float)：敌人在屏幕上的Y坐标
**返回值**：无

### 7. takeDamage(int damage)
**用途**：使敌人承受伤害
**参数**：
- `damage` (int)：敌人承受的伤害值
**返回值**：无
**说明**：
- 减少敌人的生命值
- 如果生命值小于等于0，设置为不活跃并调用 onDeath 方法

### 8. onDeath()
**用途**：敌人死亡时的回调方法
**参数**：无
**返回值**：无
**说明**：子类可以重写此方法添加死亡特效、掉落物等

### 9. isOutOfBounds()
**用途**：检查敌人是否超出边界
**参数**：无
**返回值**：boolean - 是否越界
**说明**：调用带参数的版本

### 10. isOutOfBounds(int canvasWidth, int canvasHeight)
**用途**：检查敌人是否超出游戏边界
**参数**：
- `canvasWidth` (int)：游戏画布的宽度（兼容参数，不使用）
- `canvasHeight` (int)：游戏画布的高度（兼容参数，不使用）
**返回值**：boolean - 敌人是否超出边界
**重写**：重写了 `Obj` 类的方法
**说明**：使用游戏逻辑坐标系的固定边界

### 11. isAlive()
**用途**：检查敌人是否存活
**参数**：无
**返回值**：boolean - 敌人是否存活
**说明**：返回 isActive() 的结果

### 12. getHp()
**用途**：获取敌人当前的生命值
**参数**：无
**返回值**：int - 当前生命值

### 13. setHp(int hp)
**用途**：设置敌人的生命值
**参数**：
- `hp` (int)：要设置的生命值
**返回值**：无

### 14. getMaxHp()
**用途**：获取敌人的最大生命值
**参数**：无
**返回值**：int - 最大生命值

### 15. setGameWorld(GameWorld gameWorld)
**用途**：设置游戏世界引用
**参数**：
- `gameWorld` (GameWorld)：游戏世界引用
**返回值**：无

### 16. getGameWorld()
**用途**：获取游戏世界引用
**参数**：无
**返回值**：GameWorld - 游戏世界引用

### 17. reset()
**用途**：重置敌人状态
**参数**：无
**返回值**：无
**重写**：重写了 `Obj` 类的方法
**说明**：调用父类的 reset 方法，并重置生命值

### 18. onTaskStart()
**用途**：任务开始时触发的方法，用于处理开局对话等
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现

### 19. onTaskEnd()
**用途**：任务结束时触发的方法，用于处理boss击破对话和道具掉落
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现

### 20. resetState()
**用途**：重置敌人状态（对象池回收时调用）
**参数**：无
**返回值**：无
**说明**：
- 重置敌人的基本属性
- 保留 gameWorld 引用
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
- `update()`：更新对象状态
- `render(Graphics2D g)`：渲染对象