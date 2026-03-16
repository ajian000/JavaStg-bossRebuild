# Player 类文档

## 类概述
`Player` 是玩家角色的基类，实现了 `IRenderable` 接口。定义了玩家的基本行为和属性，包括移动、射击、重生、无敌、生命值系统和符卡机制等功能。

## 成员变量

### 1. speed (float)
**用途**：普通移动速度
**默认值**：5.0f

### 2. speedSlow (float)
**用途**：低速移动速度
**默认值**：2.0f

### 3. slowMode (boolean)
**用途**：是否处于低速模式
**默认值**：false

### 4. shooting (boolean)
**用途**：是否正在射击
**默认值**：false

### 5. shootCooldown (int)
**用途**：射击冷却时间（帧）
**默认值**：0

### 6. SHOOT_INTERVAL (int)
**用途**：射击间隔（帧）
**默认值**：1

### 7. respawnTimer (int)
**用途**：重生计时器
**默认值**：0

### 8. RESPAWN_TIME (int)
**用途**：重生等待时间（帧数）
**默认值**：60

### 9. spawnX (float)
**用途**：重生X坐标

### 10. spawnY (float)
**用途**：重生Y坐标

### 11. respawning (boolean)
**用途**：重生动画标志
**默认值**：false

### 12. RESPAWN_SPEED (float)
**用途**：重生移动速度
**默认值**：8.0f

### 13. invincibleTimer (int)
**用途**：无敌时间计时器（帧数）
**默认值**：0

### 14. INVINCIBLE_TIME (int)
**用途**：无敌时间（帧数）
**默认值**：120（2秒）

### 15. BULLET_DAMAGE (int)
**用途**：子弹伤害
**默认值**：2

### 16. keyStateProvider (KeyStateProvider)
**用途**：按键状态提供者

### 17. sharedCoordinateSystem (CoordinateSystem)
**用途**：共享坐标系统

### 18. maxLives (int)
**用途**：最大生命值
**默认值**：3

### 19. currentLives (int)
**用途**：当前生命值
**默认值**：3

### 20. maxSpellCards (int)
**用途**：最大符卡数量
**默认值**：2

### 21. currentSpellCards (int)
**用途**：当前符卡数量
**默认值**：2

## 构造方法

### 1. Player()
**用途**：创建玩家对象，使用默认参数
**默认值**：
- 位置：(0, 0)
- 普通移动速度：5.0f
- 低速移动速度：2.0f
- 大小：20
- 生命值：3
- 符卡数量：2

### 2. Player(float spawnX, float spawnY)
**用途**：创建玩家对象，指定重生位置
**参数**：
- `spawnX` (float)：重生X坐标
- `spawnY` (float)：重生Y坐标
**默认值**：
- 普通移动速度：5.0f
- 低速移动速度：2.0f
- 大小：20
- 生命值：3
- 符卡数量：2

### 3. Player(float x, float y, float speed, float speedSlow, float size)
**用途**：创建玩家对象，使用完整参数
**参数**：
- `x` (float)：初始X坐标
- `y` (float)：初始Y坐标
- `speed` (float)：普通移动速度
- `speedSlow` (float)：低速移动速度
- `size` (float)：玩家大小
**默认值**：
- 生命值：3
- 符卡数量：2

## 方法说明

### 1. onUpdate()
**用途**：实现每帧的自定义更新逻辑
**参数**：无
**返回值**：无
**说明**：子类可以重写此方法实现每帧的自定义更新逻辑

### 2. onMove()
**用途**：实现自定义移动逻辑
**参数**：无
**返回值**：无
**说明**：子类可以重写此方法实现自定义移动逻辑

### 3. update()
**用途**：更新玩家状态
**参数**：无
**返回值**：无
**说明**：
- 处理重生等待和重生动画
- 更新玩家位置和边界限制
- 处理射击逻辑
- 更新无敌时间计时
- 处理键盘输入

### 4. shoot()
**用途**：发射子弹
**参数**：无
**返回值**：无
**说明**：
- 简化射击逻辑
- 子类可重写此方法实现不同的射击模式

### 5. render(IRenderer renderer)
**用途**：渲染玩家（支持OpenGL）
**参数**：
- `renderer` (IRenderer)：渲染器
**返回值**：无
**说明**：
- 实现了 IRenderable 接口
- 处理无敌闪烁效果
- 渲染角色主体和低速模式时的受击判定点

### 6. renderOnScreen(IRenderer renderer)
**用途**：在屏幕中渲染玩家
**参数**：
- `renderer` (IRenderer)：渲染器
**返回值**：无

### 7. moveUp()
**用途**：向上移动
**参数**：无
**返回值**：无

### 8. moveDown()
**用途**：向下移动
**参数**：无
**返回值**：无

### 9. moveLeft()
**用途**：向左移动
**参数**：无
**返回值**：无

### 10. moveRight()
**用途**：向右移动
**参数**：无
**返回值**：无

### 11. stopVertical()
**用途**：停止垂直移动
**参数**：无
**返回值**：无

### 12. stopHorizontal()
**用途**：停止水平移动
**参数**：无
**返回值**：无

### 13. stopAll()
**用途**：停止所有移动
**参数**：无
**返回值**：无

### 14. startShooting()
**用途**：开始射击
**参数**：无
**返回值**：无

### 15. stopShooting()
**用途**：停止射击
**参数**：无
**返回值**：无

### 16. enterSlowMode()
**用途**：切换到低速模式
**参数**：无
**返回值**：无

### 17. exitSlowMode()
**用途**：退出低速模式
**参数**：无
**返回值**：无

### 18. setShooting(boolean shooting)
**用途**：设置射击状态
**参数**：
- `shooting` (boolean)：是否射击
**返回值**：无

### 19. setSlowMode(boolean slow)
**用途**：设置低速模式
**参数**：
- `slow` (boolean)：是否低速模式
**返回值**：无

### 20. setPosition(float x, float y)
**用途**：设置位置
**参数**：
- `x` (float)：X坐标
- `y` (float)：Y坐标
**返回值**：无
**说明**：保存初始位置用于重生

### 21. getSpeed()
**用途**：获取普通移动速度
**参数**：无
**返回值**：float - 普通移动速度

### 22. getSpeedSlow()
**用途**：获取低速移动速度
**参数**：无
**返回值**：float - 低速移动速度

### 23. setSpeed(float speed)
**用途**：设置普通移动速度
**参数**：
- `speed` (float)：普通移动速度
**返回值**：无

### 24. setSpeedSlow(float speedSlow)
**用途**：设置低速移动速度
**参数**：
- `speedSlow` (float)：低速移动速度
**返回值**：无

### 25. isSlowMode()
**用途**：检查是否处于低速模式
**参数**：无
**返回值**：boolean - 是否处于低速模式

### 26. onHit()
**用途**：受击处理
**参数**：无
**返回值**：无
**说明**：
- 减少生命值
- 玩家中弹后立即移到屏幕下方，然后等待重生

### 27. reset()
**用途**：重置玩家状态（用于重新开始游戏）
**参数**：无
**返回值**：无
**说明**：
- 重置所有状态变量
- 恢复生命值和符卡数量
- 重置无敌时间

### 28. isInvincible()
**用途**：检查玩家是否处于无敌状态
**参数**：无
**返回值**：boolean - 是否无敌

### 29. getInvincibleTimer()
**用途**：获取无敌计时器剩余帧数
**参数**：无
**返回值**：int - 无敌剩余帧数

### 30. getBulletDamage()
**用途**：获取子弹伤害
**参数**：无
**返回值**：int - 子弹伤害值

### 31. onTaskStart()
**用途**：任务开始时触发的方法，用于处理开局对话等
**参数**：无
**返回值**：无

### 32. onTaskEnd()
**用途**：任务结束时触发的方法，用于处理boss击破对话和道具掉落
**参数**：无
**返回值**：无

### 33. setKeyStateProvider(KeyStateProvider provider)
**用途**：设置按键状态提供者
**参数**：
- `provider` (KeyStateProvider)：按键状态提供者
**返回值**：无

### 34. setSharedCoordinateSystem(CoordinateSystem coordinateSystem)
**用途**：设置共享的坐标系统
**参数**：
- `coordinateSystem` (CoordinateSystem)：坐标系统实例
**返回值**：无

### 35. getSharedCoordinateSystem()
**用途**：获取共享的坐标系统
**参数**：无
**返回值**：CoordinateSystem - 坐标系统实例

### 36. isCoordinateSystemInitialized()
**用途**：检查坐标系统是否已初始化
**参数**：无
**返回值**：boolean - 是否已初始化

### 37. requireCoordinateSystem()
**用途**：要求坐标系统必须已初始化
**参数**：无
**返回值**：无
**说明**：如果坐标系统未初始化，抛出 IllegalStateException

### 38. toScreenCoords(float worldX, float worldY)
**用途**：将游戏坐标转换为屏幕坐标
**参数**：
- `worldX` (float)：游戏世界X坐标
- `worldY` (float)：游戏世界Y坐标
**返回值**：float[] - 屏幕坐标数组 [x, y]

### 39. getMaxLives()
**用途**：获取最大生命值
**参数**：无
**返回值**：int - 最大生命值

### 40. getCurrentLives()
**用途**：获取当前生命值
**参数**：无
**返回值**：int - 当前生命值

### 41. setMaxLives(int maxLives)
**用途**：设置最大生命值
**参数**：
- `maxLives` (int)：最大生命值
**返回值**：无

### 42. setCurrentLives(int currentLives)
**用途**：设置当前生命值
**参数**：
- `currentLives` (int)：当前生命值
**返回值**：无

### 43. addLives(int amount)
**用途**：增加生命值
**参数**：
- `amount` (int)：增加的生命值
**返回值**：无

### 44. loseLives(int amount)
**用途**：减少生命值
**参数**：
- `amount` (int)：减少的生命值
**返回值**：无

### 45. isAlive()
**用途**：检查玩家是否存活
**参数**：无
**返回值**：boolean - 是否存活

### 46. getMaxSpellCards()
**用途**：获取最大符卡数量
**参数**：无
**返回值**：int - 最大符卡数量

### 47. getCurrentSpellCards()
**用途**：获取当前符卡数量
**参数**：无
**返回值**：int - 当前符卡数量

### 48. setMaxSpellCards(int maxSpellCards)
**用途**：设置最大符卡数量
**参数**：
- `maxSpellCards` (int)：最大符卡数量
**返回值**：无

### 49. setCurrentSpellCards(int currentSpellCards)
**用途**：设置当前符卡数量
**参数**：
- `currentSpellCards` (int)：当前符卡数量
**返回值**：无

### 50. addSpellCards(int amount)
**用途**：增加符卡
**参数**：
- `amount` (int)：增加的符卡数量
**返回值**：无

### 51. useSpellCard()
**用途**：使用符卡
**参数**：无
**返回值**：boolean - 是否成功使用符卡

### 52. hasSpellCards()
**用途**：检查是否有可用符卡
**参数**：无
**返回值**：boolean - 是否有可用符卡

### 53. getRenderLayer()
**用途**：获取渲染层级
**参数**：无
**返回值**：int - 渲染层级
**说明**：实现了 IRenderable 接口

## 实现的接口

### 从 IRenderable 接口继承
- `render(IRenderer renderer)`：渲染对象
- `isActive()`：检查对象是否活跃
- `getRenderLayer()`：获取渲染层级

## 核心方法

### 位置和速度相关
- `getX()`：获取X坐标
- `getY()`：获取Y坐标
- `setX(float x)`：设置X坐标
- `setY(float y)`：设置Y坐标
- `getVx()`：获取X方向速度
- `getVy()`：获取Y方向速度
- `setVx(float vx)`：设置X方向速度
- `setVy(float vy)`：设置Y方向速度
- `getVelocityX()`：获取X方向速度
- `getVelocityY()`：获取Y方向速度
- `setVelocityByComponent(int component, float value)`：设置速度分量
- `setVelocityByComponent(int component, int value)`：设置速度分量
- `moveOn(float dx, float dy)`：移动指定距离

### 基本属性相关
- `getSize()`：获取物体大小
- `getColor()`：获取物体颜色
- `isActive()`：检查物体是否激活
- `setActive(boolean active)`：设置物体激活状态
- `getHitboxRadius()`：获取碰撞判定半径