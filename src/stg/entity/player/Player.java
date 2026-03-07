package stg.entity.player;

import java.awt.Color;

import stg.base.KeyStateProvider;
import stg.render.IRenderable;
import stg.render.IRenderer;
import stg.util.CoordinateSystem;

/**
 * 玩家类- 自机角色
 * @since 2026-03-07
 */
public class Player implements IRenderable {
    // 基本属性
    private float x; // X坐标
    private float y; // Y坐标
    private float vx; // X方向速度
    private float vy; // Y方向速度
    private float size; // 物体大小
    private Color color; // 物体颜色
    private float hitboxRadius; // 碰撞判定半径
    private boolean active; // 激活状态
    // private int frame; // 帧计数器 - 暂时未使用
    
    // 玩家特有属性
    private float speed; // 普通移动速度
    private float speedSlow; // 低速移动速度
    private boolean slowMode; // 低速模式标志
    private boolean shooting; // 射击标志
    private int shootCooldown; // 射击冷却时间
    private static final int SHOOT_INTERVAL = 1;
    private int respawnTimer; // 重生计时(帧数)
    private static final int RESPAWN_TIME = 60; // 重生等待时间(帧数)
    private float spawnX; // 重生X坐标
    private float spawnY; // 重生Y坐标
    private boolean respawning; // 重生动画标志
    private static final float RESPAWN_SPEED = 8.0f; // 重生移动速度
    private int invincibleTimer; // 无敌时间计时(帧数)
    private static final int INVINCIBLE_TIME = 120; // 无敌时间(120f)
    protected static final int BULLET_DAMAGE = 2; // 子弹伤害，DPS = (2 × 2 × 60) / 2 = 120
    private KeyStateProvider keyStateProvider; // 按键状态提供者
    private static CoordinateSystem sharedCoordinateSystem; // 共享坐标系统
    
    // 生命值系统
    private int maxLives; // 最大生命值
    private int currentLives; // 当前生命值
    
    // 符卡机制
    private int maxSpellCards; // 最大符卡数量
    private int currentSpellCards; // 当前符卡数量
    
    /**
     * 构造函数
     */
    public Player() {
        this(0, 0, 5.0f, 2.0f, 20);
    }

    /**
     * 构造函数
     * @param spawnX 初始X坐标
     * @param spawnY 初始Y坐标
     */
    public Player(float spawnX, float spawnY) {
        this(spawnX, spawnY, 5.0f, 2.0f, 20);
    }

    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param speed 普通移动速度
     * @param speedSlow 低速移动速度
     * @param size 玩家大小
     */
    public Player(float x, float y, float speed, float speedSlow, float size) {
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        this.size = size;
        this.color = new Color(255, 100, 100);
        this.hitboxRadius = 2.0f;
        this.active = true;
        // this.frame = 0; // 帧计数器暂时未使用
        this.speed = speed;
        this.speedSlow = speedSlow;
        this.slowMode = false;
        this.shooting = false;
        this.shootCooldown = 0;
        this.respawnTimer = 0;
        this.spawnX = x;
        this.spawnY = y;
        this.respawning = false;
        this.invincibleTimer = INVINCIBLE_TIME;
        this.keyStateProvider = null;
        
        // 初始化生命值系统
        this.maxLives = 3;
        this.currentLives = maxLives;
        
        // 初始化符卡机制
        this.maxSpellCards = 2;
        this.currentSpellCards = maxSpellCards;
    }
    
    /**
     * 设置按键状态提供者
     * @param provider 按键状态提供者
     */
    public void setKeyStateProvider(KeyStateProvider provider) {
        this.keyStateProvider = provider;
    }
    
    /**
     * 设置共享的坐标系统
     * @param coordinateSystem 坐标系统实例
     */
    public static void setSharedCoordinateSystem(CoordinateSystem coordinateSystem) {
        sharedCoordinateSystem = coordinateSystem;
    }

    /**
     * 获取共享的坐标系统
     * @return 坐标系统实例
     */
    public static CoordinateSystem getSharedCoordinateSystem() {
        return sharedCoordinateSystem;
    }

    /**
     * 检查坐标系统是否已初始化
     * @return 是否已初始化
     */
    public static boolean isCoordinateSystemInitialized() {
        return sharedCoordinateSystem != null;
    }

    /**
     * 要求坐标系统必须已初始化
     * @throws IllegalStateException 如果坐标系统未初始化
     */
    public static void requireCoordinateSystem() {
        if (sharedCoordinateSystem == null) {
            throw new IllegalStateException(
                "CoordinateSystem not initialized. " +
                "Please call Player.setSharedCoordinateSystem() before using player."
            );
        }
    }

    /**
     * 将游戏坐标转换为屏幕坐标
     * @param worldX 游戏世界X坐标
     * @param worldY 游戏世界Y坐标
     * @return 屏幕坐标数组 [x, y]
     */
    public static float[] toScreenCoords(float worldX, float worldY) {
        requireCoordinateSystem();
        return sharedCoordinateSystem.toScreenCoords(worldX, worldY);
    }
    
    /**
     * 处理键盘输入
     */
    private void handleKeyboardInput() {
        if (keyStateProvider == null || respawning) {
            return;
        }
        
        float currentSpeed = slowMode ? speedSlow : speed;
        
        boolean leftPressed = keyStateProvider.isLeftPressed();
        boolean rightPressed = keyStateProvider.isRightPressed();
        boolean upPressed = keyStateProvider.isUpPressed();
        boolean downPressed = keyStateProvider.isDownPressed();
        
        // 处理X方向输入 - 同时按住相反方向键时悬停
        if (leftPressed && rightPressed) {
            setVx(0);
        } else if (leftPressed) {
            setVx(-currentSpeed);
        } else if (rightPressed) {
            setVx(currentSpeed);
        } else {
            setVx(0);
        }
        
        // 处理Y方向输入 - 同时按住相反方向键时悬停
        if (upPressed && downPressed) {
            setVy(0);
        } else if (upPressed) {
            setVy(currentSpeed);
        } else if (downPressed) {
            setVy(-currentSpeed);
        } else {
            setVy(0);
        }
        
        slowMode = keyStateProvider.isShiftPressed();
        shooting = keyStateProvider.isZPressed();
    }
    
    /**
     * 获取X坐标
     * @return X坐标
     */
    public float getX() {
        return x;
    }
    
    /**
     * 获取Y坐标
     * @return Y坐标
     */
    public float getY() {
        return y;
    }
    
    /**
     * 设置X坐标
     * @param x X坐标
     */
    public void setX(float x) {
        this.x = x;
    }
    
    /**
     * 设置Y坐标
     * @param y Y坐标
     */
    public void setY(float y) {
        this.y = y;
    }
    
    /**
     * 获取X方向速度
     * @return X方向速度
     */
    public float getVx() {
        return vx;
    }
    
    /**
     * 获取Y方向速度
     * @return Y方向速度
     */
    public float getVy() {
        return vy;
    }
    
    /**
     * 设置X方向速度
     * @param vx X方向速度
     */
    public void setVx(float vx) {
        this.vx = vx;
    }
    
    /**
     * 设置Y方向速度
     * @param vy Y方向速度
     */
    public void setVy(float vy) {
        this.vy = vy;
    }
    
    /**
     * 获取碰撞判定半径
     * @return 碰撞判定半径
     */
    public float getHitboxRadius() {
        return hitboxRadius;
    }
    
    /**
     * 获取物体大小
     * @return 物体大小
     */
    public float getSize() {
        return size;
    }
    
    /**
     * 获取物体颜色
     * @return 物体颜色
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * 检查物体是否激活
     * @return 是否激活
     */
    @Override
    public boolean isActive() {
        return active;
    }
    
    /**
     * 设置物体激活状态
     * @param active 是否激活
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * 获取X方向速度
     * @return X方向速度
     */
    public float getVelocityX() {
        return getVx();
    }
    
    /**
     * 获取Y方向速度
     * @return Y方向速度
     */
    public float getVelocityY() {
        return getVy();
    }
    
    /**
     * 设置速度分量
     * @param component 分量索引 (0: X, 1: Y)
     * @param value 速度值
     */
    public void setVelocityByComponent(int component, float value) {
        if (component == 0) {
            setVx(value);
        } else if (component == 1) {
            setVy(value);
        }
    }
    
    /**
     * 设置速度分量
     * @param component 分量索引 (0: X, 1: Y)
     * @param value 速度值
     */
    public void setVelocityByComponent(int component, int value) {
        if (component == 0) {
            setVx(value);
        } else if (component == 1) {
            setVy(value);
        }
    }
    
    /**
     * 移动指定距离
     * @param dx X方向移动距离
     * @param dy Y方向移动距离
     */
    public void moveOn(float dx, float dy) {
        setX(getX() + dx);
        setY(getY() + dy);
    }

    /**
     * 实现每帧的自定义更新逻辑
     */
    protected void onUpdate() {
        // 子类可以重写此方法实现每帧的自定义更新逻辑
    }

    /**
     * 实现自定义移动逻辑
     */
    protected void onMove() {
        // 子类可以重写此方法实现自定义移动逻辑
    }

    /**
     * 更新玩家状态
     */
    public void update() {
        if (!active) return;
        
        // 调用自定义更新逻辑
        onUpdate();

        // 处理重生等待计时
        if (respawnTimer > 0) {
            respawnTimer--;
            if (respawnTimer == 0) {
                respawning = true;
                // 使用游戏逻辑坐标系的底部边界作为重生起点
                if (isCoordinateSystemInitialized()) {
                    CoordinateSystem cs = getSharedCoordinateSystem();
                    float bottomBound = cs.getBottomBound();
                    setPosition(spawnX, bottomBound - getSize());
                    setVelocityByComponent(1, RESPAWN_SPEED);
                }
            }
            return;
        }

        // 处理重生动画
        if (respawning) {
            // 调用自定义移动逻辑
            onMove();

            // 更新位置
            moveOn(getVelocityX(), getVelocityY());

            // 检查是否到达重生位置
            if (getY() >= spawnY) {
                setPosition(spawnX, spawnY);
                setVelocityByComponent(0, 0);
                setVelocityByComponent(1, 0);
                respawning = false;
                invincibleTimer = INVINCIBLE_TIME; // 重生后获得无敌时间
                System.out.println("Player respawned at: (" + getX() + ", " + getY() + ") with " + INVINCIBLE_TIME + " frames invincible");
            }
            return; // 重生动画期间不接受玩家输入
        }

        // 调用自定义移动逻辑
        onMove();

        // 更新位置
        moveOn(getVelocityX(), getVelocityY());

        // 使用游戏逻辑坐标系的固定边界进行边界检测
        if (isCoordinateSystemInitialized()) {
            CoordinateSystem cs = getSharedCoordinateSystem();
            float leftBound = cs.getLeftBound();
            float rightBound = cs.getRightBound();
            float bottomBound = cs.getBottomBound();
            float topBound = cs.getTopBound();

            // 边界限制
            if (getX() < leftBound + getSize()) setPosition(leftBound + getSize(), getY());
            if (getX() > rightBound - getSize()) setPosition(rightBound - getSize(), getY());
            if (getY() < bottomBound + getSize()) setPosition(getX(), bottomBound + getSize());
            if (getY() > topBound - getSize()) setPosition(getX(), topBound - getSize());
        }
        
        // 键盘输入处理
        handleKeyboardInput();
        
        // 更新射击冷却
        if (shootCooldown > 0) {
            shootCooldown--;
        }

        // 更新无敌时间计时
        if (invincibleTimer > 0) {
            invincibleTimer--;
        }

        // 射击逻辑
        if (shooting && shootCooldown == 0) {
            shoot();
            shootCooldown = SHOOT_INTERVAL;
        }
    }

    /**
     * 发射子弹
     * 子类可重写此方法实现不同的射击模式
     */
    protected void shoot() {
        // 简化射击逻辑
        System.out.println("Player shot");
    }

    /**
     * 渲染玩家（IRenderer版本，支持OpenGL）
     * @param renderer 渲染器
     */
    @Override
    public void render(IRenderer renderer) {
        if (!active) return;
        
        // 无敌闪烁效果：每5帧闪烁一次
        boolean shouldRender = true;
        if (invincibleTimer > 0) {
            int flashPhase = invincibleTimer % 10;
            if (flashPhase < 5) {
                shouldRender = false;
            }
        }

        if (!shouldRender) return;

        // 转换为屏幕坐标
        if (isCoordinateSystemInitialized()) {
            float[] screenCoords = toScreenCoords(getX(), getY());
            float screenX = screenCoords[0];
            float screenY = screenCoords[1];

            // 绘制角色主体（仅为一个简单的红色球体）
            Color playerColor = getColor();
            float r = playerColor.getRed() / 255.0f;
            float g = playerColor.getGreen() / 255.0f;
            float b = playerColor.getBlue() / 255.0f;
            float a = playerColor.getAlpha() / 255.0f;
            renderer.drawCircle(screenX, screenY, getSize()/2, r, g, b, a);

            // 低速模式时显示受击判定点（在球体上方）
            if (slowMode) {
                renderer.drawCircle(screenX, screenY, getHitboxRadius(), 1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }
    
    /**
     * 在屏幕中渲染玩家
     * @param renderer 渲染器
     */
    public void renderOnScreen(IRenderer renderer) {
        render(renderer);
    }
    
    /**
     * 向上移动
     */
    public void moveUp() {
        setVelocityByComponent(1, slowMode ? speedSlow : speed);
    }

    /**
     * 向下移动
     */
    public void moveDown() {
        setVelocityByComponent(1, slowMode ? -speedSlow : -speed);
    }

    /**
     * 向左移动
     */
    public void moveLeft() {
        setVelocityByComponent(0, slowMode ? -speedSlow : -speed);
    }

    /**
     * 向右移动
     */
    public void moveRight() {
        setVelocityByComponent(0, slowMode ? speedSlow : speed);
    }

    /**
     * 停止垂直移动
     */
    public void stopVertical() {
        setVelocityByComponent(1, 0);
    }

    /**
     * 停止水平移动
     */
    public void stopHorizontal() {
        setVelocityByComponent(0, 0);
    }

    /**
     * 停止所有移动
     */
    public void stopAll() {
        setVelocityByComponent(0, 0);
        setVelocityByComponent(1, 0);
    }

    /**
     * 射击
     */
    public void startShooting() {
        this.shooting = true;
    }

    /**
     * 停止射击
     */
    public void stopShooting() {
        this.shooting = false;
    }

    /**
     * 切换到低速模式
     */
    public void enterSlowMode() {
        this.slowMode = true;
    }

    /**
     * 退出低速模式
     */
    public void exitSlowMode() {
        this.slowMode = false;
    }

    /**
     * 设置射击状态
     * @param shooting 是否射击
     */
    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    /**
     * 设置低速模式
     * @param slow 是否低速模式
     */
    public void setSlowMode(boolean slow) {
        this.slowMode = slow;
    }

    /**
     * 设置位置
     * @param x X坐标
     * @param y Y坐标
     */
    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
        // 保存初始位置用于重生
        this.spawnX = x;
        this.spawnY = y;
    }

    /**
     * 获取普通移动速度
     * @return 移动速度
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * 获取低速移动速度
     * @return 低速移动速度
     */
    public float getSpeedSlow() {
        return speedSlow;
    }

    /**
     * 设置普通移动速度
     * @param speed 移动速度
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * 设置低速移动速度
     * @param speedSlow 低速移动速度
     */
    public void setSpeedSlow(float speedSlow) {
        this.speedSlow = speedSlow;
    }

    /**
     * 是否低速模式
     * @return 是否低速模式
     */
    public boolean isSlowMode() {
        return slowMode;
    }

    /**
     * 受击处理
     * 玩家中弹后立即移到屏幕下方，然后等待重生
     */
    public void onHit() {
        // 减少生命值
        if (currentLives > 0) {
            currentLives--;
            System.out.println("Player hit! Lives left: " + currentLives);
        }
        
        // 立即移动到屏幕下方
        if (isCoordinateSystemInitialized()) {
            CoordinateSystem cs = getSharedCoordinateSystem();
            float bottomBound = cs.getBottomBound();
            setPosition(getX(), bottomBound - getSize() - 100);
        } else {
            // 备用方案
            setPosition(getX(), -500);
        }
        
        setVelocityByComponent(0, 0);
        setVelocityByComponent(1, 0);
        respawning = false;

        // 启动重生等待计时器
        respawnTimer = RESPAWN_TIME;

        System.out.println("Player hit! Moved off-screen. Respawn animation in " + RESPAWN_TIME + " frames");
    }

    /**
     * 重置玩家状态（用于重新开始游戏）
     */
    public void reset() {
        this.active = true;
        // this.frame = 0; // 帧计数器暂时未使用
        this.vx = 0;
        this.vy = 0;
        this.slowMode = false;
        this.shooting = false;
        this.shootCooldown = 0;
        this.respawnTimer = 0;
        this.respawning = false;
        this.invincibleTimer = INVINCIBLE_TIME; // 重置时获得无敌时间
        
        // 重置生命值和符卡
        this.currentLives = maxLives;
        this.currentSpellCards = maxSpellCards;
    }

    /**
     * 检查玩家是否处于无敌状态
     * @return 是否无敌
     */
    public boolean isInvincible() {
        return invincibleTimer > 0;
    }

    /**
     * 获取无敌计时器剩余帧数
     * @return 无敌剩余帧数
     */
    protected int getInvincibleTimer() {
        return invincibleTimer;
    }

    /**
     * 获取子弹伤害
     * @return 子弹伤害
     */
    public int getBulletDamage() {
        return BULLET_DAMAGE;
    }
    
    /**
     * 获取渲染层级
     * @return 渲染层级
     */
    @Override
    public int getRenderLayer() {
        return 5;
    }
    
    /**
     * 任务开始时触发的方法 - 用于处理开局对话
     */
    protected void onTaskStart() {
        // 实现任务开始逻辑
    }
    
    /**
     * 任务结束时触发的方法 - 用于处理boss击破对话和道具掉落
     */
    protected void onTaskEnd() {
        // 实现任务结束逻辑
    }
    
    // 生命值系统方法
    
    /**
     * 获取最大生命值
     * @return 最大生命值
     */
    public int getMaxLives() {
        return maxLives;
    }
    
    /**
     * 获取当前生命值
     * @return 当前生命值
     */
    public int getCurrentLives() {
        return currentLives;
    }
    
    /**
     * 设置最大生命值
     * @param maxLives 最大生命值
     */
    public void setMaxLives(int maxLives) {
        this.maxLives = maxLives;
        if (currentLives > maxLives) {
            currentLives = maxLives;
        }
    }
    
    /**
     * 设置当前生命值
     * @param currentLives 当前生命值
     */
    public void setCurrentLives(int currentLives) {
        this.currentLives = Math.max(0, Math.min(currentLives, maxLives));
    }
    
    /**
     * 增加生命值
     * @param amount 增加的生命值
     */
    public void addLives(int amount) {
        setCurrentLives(currentLives + amount);
    }
    
    /**
     * 减少生命值
     * @param amount 减少的生命值
     */
    public void loseLives(int amount) {
        setCurrentLives(currentLives - amount);
    }
    
    /**
     * 检查玩家是否存活
     * @return 是否存活
     */
    public boolean isAlive() {
        return currentLives > 0;
    }
    
    // 符卡机制方法
    
    /**
     * 获取最大符卡数量
     * @return 最大符卡数量
     */
    public int getMaxSpellCards() {
        return maxSpellCards;
    }
    
    /**
     * 获取当前符卡数量
     * @return 当前符卡数量
     */
    public int getCurrentSpellCards() {
        return currentSpellCards;
    }
    
    /**
     * 设置最大符卡数量
     * @param maxSpellCards 最大符卡数量
     */
    public void setMaxSpellCards(int maxSpellCards) {
        this.maxSpellCards = maxSpellCards;
        if (currentSpellCards > maxSpellCards) {
            currentSpellCards = maxSpellCards;
        }
    }
    
    /**
     * 设置当前符卡数量
     * @param currentSpellCards 当前符卡数量
     */
    public void setCurrentSpellCards(int currentSpellCards) {
        this.currentSpellCards = Math.max(0, Math.min(currentSpellCards, maxSpellCards));
    }
    
    /**
     * 增加符卡
     * @param amount 增加的符卡数量
     */
    public void addSpellCards(int amount) {
        setCurrentSpellCards(currentSpellCards + amount);
    }
    
    /**
     * 使用符卡
     * @return 是否成功使用符卡
     */
    public boolean useSpellCard() {
        if (currentSpellCards > 0) {
            currentSpellCards--;
            // 实现符卡效果
            System.out.println("Spell card used! Remaining: " + currentSpellCards);
            return true;
        }
        return false;
    }
    
    /**
     * 检查是否有可用符卡
     * @return 是否有可用符卡
     */
    public boolean hasSpellCards() {
        return currentSpellCards > 0;
    }
}