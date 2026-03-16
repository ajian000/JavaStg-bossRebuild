# Bullet 类文档

## 类概述
`Bullet` 是所有子弹的基类，继承自 `Obj` 类，实现了 `Resettable` 和 `IBullet` 接口。定义了子弹的基本行为和属性，包括伤害管理、生命周期、轨迹效果等功能。

## 成员变量

### 1. damage (int)
**用途**：子弹的伤害值
**默认值**：0

### 2. lifeTime (float)
**用途**：生命周期（-1表示无限）
**默认值**：-1

### 3. currentLifeTime (float)
**用途**：当前生命周期
**默认值**：0

### 4. isPlayerBullet (boolean)
**用途**：是否为玩家子弹
**默认值**：false

### 5. pierceCount (int)
**用途**：穿透次数（0表示不可穿透）
**默认值**：0

### 6. bounceCount (int)
**用途**：反弹次数（0表示不可反弹）
**默认值**：0

### 7. homing (boolean)
**用途**：是否跟踪目标
**默认值**：false

### 8. homingStrength (float)
**用途**：跟踪强度
**默认值**：0.1f

### 9. damageMultiplier (float)
**用途**：伤害倍率
**默认值**：1.0f

### 10. speedMultiplier (float)
**用途**：速度倍率
**默认值**：1.0f

### 11. sizeMultiplier (float)
**用途**：大小倍率
**默认值**：1.0f

### 12. hasTrail (boolean)
**用途**：是否有轨迹效果
**默认值**：false

### 13. trailLength (float)
**用途**：轨迹长度
**默认值**：10.0f

### 14. spriteElement (BulletSpriteSheet.BulletElement)
**用途**：精灵元素

### 15. bulletType (BulletSpriteSheet.BulletType)
**用途**：子弹类型

## 构造方法

### 1. Bullet(float x, float y, float vx, float vy, float size, Color color)
**用途**：创建子弹对象
**参数**：
- `x` (float)：子弹的初始X坐标
- `y` (float)：子弹的初始Y坐标
- `vx` (float)：子弹的X方向速度
- `vy` (float)：子弹的Y方向速度
- `size` (float)：子弹的大小
- `color` (Color)：子弹的颜色
**说明**：
- 碰撞判定半径默认设置为size的5倍，确保高速子弹不会穿透敌人

### 2. Bullet(float x, float y, float vx, float vy, float size, Color color, BulletSpriteSheet.BulletElement spriteElement, BulletSpriteSheet.BulletType bulletType)
**用途**：创建子弹对象（带精灵元素）
**参数**：
- `x` (float)：子弹的初始X坐标
- `y` (float)：子弹的初始Y坐标
- `vx` (float)：子弹的X方向速度
- `vy` (float)：子弹的Y方向速度
- `size` (float)：子弹的大小
- `color` (Color)：子弹的颜色
- `spriteElement` (BulletSpriteSheet.BulletElement)：精灵元素
- `bulletType` (BulletSpriteSheet.BulletType)：子弹类型
**说明**：
- 碰撞判定半径默认设置为size的5倍，确保高速子弹不会穿透敌人

## 方法说明

### 1. getDamage()
**用途**：获取子弹的伤害值
**参数**：无
**返回值**：int - 子弹的伤害值

### 2. setDamage(int damage)
**用途**：设置子弹的伤害值
**参数**：
- `damage` (int)：子弹的伤害值
**返回值**：无

### 3. getSpeed()
**用途**：获取子弹速度
**参数**：无
**返回值**：float - 子弹速度

### 4. setSpeed(float speed)
**用途**：设置子弹速度
**参数**：
- `speed` (float)：子弹速度
**返回值**：无

### 5. getDirection()
**用途**：获取子弹方向（角度，单位：度）
**参数**：无
**返回值**：float - 子弹方向

### 6. setDirection(float direction)
**用途**：设置子弹方向（角度，单位：度）
**参数**：
- `direction` (float)：子弹方向
**返回值**：无

### 7. getLifeTime()
**用途**：获取子弹生命周期
**参数**：无
**返回值**：float - 生命周期

### 8. setLifeTime(float lifeTime)
**用途**：设置子弹生命周期
**参数**：
- `lifeTime` (float)：生命周期
**返回值**：无

### 9. onUpdate()
**用途**：自定义更新逻辑
**参数**：无
**返回值**：无
**说明**：处理生命周期管理

### 10. updateLifeTime()
**用途**：更新生命周期
**参数**：无
**返回值**：无
**说明**：如果设置了生命周期，当达到生命周期时设置为不活跃

### 11. isOutOfBounds(int width, int height)
**用途**：检查子弹是否超出边界
**参数**：
- `width` (int)：游戏宽度
- `height` (int)：游戏高度
**返回值**：boolean - 是否超出边界

### 12. render(Graphics2D g)
**用途**：渲染子弹（Java2D版本）
**参数**：
- `g` (Graphics2D)：图形上下文
**返回值**：无

### 13. renderTrail(Graphics2D g, float screenX, float screenY)
**用途**：渲染子弹轨迹
**参数**：
- `g` (Graphics2D)：图形上下文
- `screenX` (float)：屏幕X坐标
- `screenY` (float)：屏幕Y坐标
**返回值**：无

### 14. resetState()
**用途**：重置子弹状态（对象池回收时调用）
**参数**：无
**返回值**：无
**说明**：
- 重置子弹的基本属性
- 实现了 Resettable 接口

### 15. isPlayerBullet()
**用途**：获取是否为玩家子弹
**参数**：无
**返回值**：boolean - 是否为玩家子弹

### 16. setPlayerBullet(boolean isPlayerBullet)
**用途**：设置是否为玩家子弹
**参数**：
- `isPlayerBullet` (boolean)：是否为玩家子弹
**返回值**：无

### 17. getPierceCount()
**用途**：获取穿透次数
**参数**：无
**返回值**：int - 穿透次数

### 18. setPierceCount(int pierceCount)
**用途**：设置穿透次数
**参数**：
- `pierceCount` (int)：穿透次数
**返回值**：无

### 19. getBounceCount()
**用途**：获取反弹次数
**参数**：无
**返回值**：int - 反弹次数

### 20. setBounceCount(int bounceCount)
**用途**：设置反弹次数
**参数**：
- `bounceCount` (int)：反弹次数
**返回值**：无

### 21. isHoming()
**用途**：获取是否跟踪目标
**参数**：无
**返回值**：boolean - 是否跟踪目标

### 22. setHoming(boolean homing)
**用途**：设置是否跟踪目标
**参数**：
- `homing` (boolean)：是否跟踪目标
**返回值**：无

### 23. getHomingStrength()
**用途**：获取跟踪强度
**参数**：无
**返回值**：float - 跟踪强度

### 24. setHomingStrength(float homingStrength)
**用途**：设置跟踪强度
**参数**：
- `homingStrength` (float)：跟踪强度
**返回值**：无

### 25. getDamageMultiplier()
**用途**：获取伤害倍率
**参数**：无
**返回值**：float - 伤害倍率

### 26. setDamageMultiplier(float damageMultiplier)
**用途**：设置伤害倍率
**参数**：
- `damageMultiplier` (float)：伤害倍率
**返回值**：无

### 27. getSpeedMultiplier()
**用途**：获取速度倍率
**参数**：无
**返回值**：float - 速度倍率

### 28. setSpeedMultiplier(float speedMultiplier)
**用途**：设置速度倍率
**参数**：
- `speedMultiplier` (float)：速度倍率
**返回值**：无

### 29. getSizeMultiplier()
**用途**：获取大小倍率
**参数**：无
**返回值**：float - 大小倍率

### 30. setSizeMultiplier(float sizeMultiplier)
**用途**：设置大小倍率
**参数**：
- `sizeMultiplier` (float)：大小倍率
**返回值**：无

### 31. hasTrail()
**用途**：获取是否有轨迹效果
**参数**：无
**返回值**：boolean - 是否有轨迹效果

### 32. setHasTrail(boolean hasTrail)
**用途**：设置是否有轨迹效果
**参数**：
- `hasTrail` (boolean)：是否有轨迹效果
**返回值**：无

### 33. getTrailLength()
**用途**：获取轨迹长度
**参数**：无
**返回值**：float - 轨迹长度

### 34. setTrailLength(float trailLength)
**用途**：设置轨迹长度
**参数**：
- `trailLength` (float)：轨迹长度
**返回值**：无

### 35. getSpriteElement()
**用途**：获取精灵元素
**参数**：无
**返回值**：BulletSpriteSheet.BulletElement - 精灵元素

### 36. setSpriteElement(BulletSpriteSheet.BulletElement spriteElement)
**用途**：设置精灵元素
**参数**：
- `spriteElement` (BulletSpriteSheet.BulletElement)：精灵元素
**返回值**：无

### 37. getBulletType()
**用途**：获取子弹类型
**参数**：无
**返回值**：BulletSpriteSheet.BulletType - 子弹类型

### 38. setBulletType(BulletSpriteSheet.BulletType bulletType)
**用途**：设置子弹类型
**参数**：
- `bulletType` (BulletSpriteSheet.BulletType)：子弹类型
**返回值**：无

### 39. getX()
**用途**：获取X坐标
**参数**：无
**返回值**：float - X坐标

### 40. getY()
**用途**：获取Y坐标
**参数**：无
**返回值**：float - Y坐标

### 41. getVx()
**用途**：获取X方向速度
**参数**：无
**返回值**：float - X方向速度

### 42. getVy()
**用途**：获取Y方向速度
**参数**：无
**返回值**：float - Y方向速度

### 43. setVx(float vx)
**用途**：设置X方向速度
**参数**：
- `vx` (float)：X方向速度
**返回值**：无

### 44. setVy(float vy)
**用途**：设置Y方向速度
**参数**：
- `vy` (float)：Y方向速度
**返回值**：无

### 45. setX(float x)
**用途**：设置X坐标
**参数**：
- `x` (float)：X坐标
**返回值**：无

### 46. setY(float y)
**用途**：设置Y坐标
**参数**：
- `y` (float)：Y坐标
**返回值**：无

### 47. getSize()
**用途**：获取大小
**参数**：无
**返回值**：float - 大小

### 48. getHitboxRadius()
**用途**：获取碰撞检测半径
**参数**：无
**返回值**：float - 碰撞检测半径

### 49. setHitboxRadius(float hitboxRadius)
**用途**：设置碰撞检测半径
**参数**：
- `hitboxRadius` (float)：碰撞检测半径
**返回值**：无

### 50. isActive()
**用途**：检查是否活跃
**参数**：无
**返回值**：boolean - 是否活跃

### 51. setActive(boolean active)
**用途**：设置是否活跃
**参数**：
- `active` (boolean)：是否活跃
**返回值**：无

### 52. update()
**用途**：更新对象状态
**参数**：无
**返回值**：无

## 实现的接口

### 从 Resettable 接口继承
- `resetState()`：重置对象状态

### 从 IBullet 接口继承
- `getDamage()`：获取子弹的伤害值
- `setDamage(int damage)`：设置子弹的伤害值
- `isOutOfBounds(int width, int height)`：检查子弹是否超出游戏边界
- `getSpeed()`：获取子弹的移动速度
- `setSpeed(float speed)`：设置子弹的移动速度
- `getDirection()`：获取子弹的移动方向
- `setDirection(float direction)`：设置子弹的移动方向
- `getX()`：获取X坐标
- `getY()`：获取Y坐标
- `getSize()`：获取大小
- `getHitboxRadius()`：获取碰撞检测半径
- `isActive()`：检查是否活跃
- `setActive(boolean active)`：设置是否活跃

## 继承的方法

### 从 Obj 类继承
- `getColor()`：获取颜色
- `setColor(Color color)`：设置颜色
- `toScreenCoords(float x, float y)`：将游戏坐标转换为屏幕坐标
- `update()`：更新对象状态
- `render(Graphics2D g)`：渲染对象