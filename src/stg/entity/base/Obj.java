package stg.entity.base;

import java.awt.Color;
import java.awt.Graphics2D;

import stg.render.IRenderable;
import stg.render.IRenderer;
import stg.util.CoordinateSystem;
import stg.util.objectpool.ObjectPoolConfig;
import stg.util.objectpool.ObjectPoolManager;

/**
 * 游戏物体基类 - 所有游戏中的物体都继承自此类
 * @since 26-01-19 初始创建
 * @author JavaSTG Team
 * @date 2026-01-19 初始创建
 * @date 2026-01-20 将类移动到stg.game.obj
 * @date 2026-01-29 功能优化
 * @date 2026-02-16 重构坐标系统，使用固定360*480游戏逻辑尺寸
 * @date 2026-02-18 迁移到stg.entity.base包
 * @date 2026-02-21 添加对象池支持
 * @date 2026-02-26 添加加载素材方法和纹理渲染支持
 * @date 2026-02-22 将对象池配置独立到 ObjectPoolConfig 类，支持@Pooled注解自动注册
 */
public abstract class Obj implements IRenderable {
    // 生命周期状态枚举
    public enum LifecycleState {
        CREATED,    // 已创建
        INITIALIZED, // 已初始化
        ACTIVE,     // 活动中
        DESTROYED   // 已销毁
    }
    
    protected float x; // X坐标
    protected float y; // Y坐标
    protected float vx; // X方向速度
    protected float vy; // Y方向速度
    protected float size; // 物体大小
    protected Color color; // 物体颜色
    protected float hitboxRadius; // 碰撞判定半径
    protected boolean active; // 激活状态
    protected int frame; // 帧计数器
    protected LifecycleState lifecycleState; // 生命周期状态
    protected float angle; // 角度（度），x轴正方向为0，顺时针为正
    protected float angularVelocity; // 角速度（度/秒）
    
    // 坐标系统（用于动态坐标转换）
    private static CoordinateSystem sharedCoordinateSystem;
    
    // 静态初始化块：当Obj类被加载时执行
    static {
        // 初始化对象池系统
        ObjectPoolConfig.initialize();
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
                "Please call Obj.setSharedCoordinateSystem() before using game objects."
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
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param vx X方向速度
     * @param vy Y方向速度
     * @param size 物体大小
     * @param color 物体颜色
     */
    public Obj(float x, float y, float vx, float vy, float size, Color color) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.size = size;
        this.color = color;
        this.hitboxRadius = size;
        this.active = true;
        this.frame = 0;
        this.angle = 0;
        this.angularVelocity = 0;
        this.lifecycleState = LifecycleState.CREATED;
        initBehavior();
        init();
    }

    /**
     * 初始化行为参数
     * 在构造函数中调用，用于初始化行为参数
     */
    protected void initBehavior() {
        // 子类可以重写此方法初始化行为参数
    }
    
    /**
     * 初始化实体
     * 用于初始化实体的资源和状态
     */
    public void init() {
        if (lifecycleState == LifecycleState.CREATED) {
            // 执行初始化逻辑
            onInit();
            lifecycleState = LifecycleState.INITIALIZED;
        }
    }
    
    /**
     * 自定义初始化逻辑
     * 子类可以重写此方法实现特定的初始化行为
     */
    protected void onInit() {
        // 子类可以重写此方法实现特定的初始化行为
    }

    /**
     * 实现每帧的自定义更新逻辑
     * 子类可以重写此方法来实现特定的更新行为，如状态变化、动画控制等
     * 此方法在update()方法中被调用，位于位置更新之前
     * 注意：此方法仅在物体处于激活状态时被调用
     */
    protected void onUpdate() {
        // 子类可以重写此方法实现每帧的自定义更新逻辑
    }

    /**
     * 实现自定义移动逻辑
     * 子类可以重写此方法来实现特定的移动行为，如路径跟随、避障等
     * 此方法在update()方法中被调用，位于位置更新之前
     * 注意：此方法仅在物体处于激活状态时被调用
     * 子类可以在此方法中修改vx和vy的值，以实现复杂的移动效果
     */
    protected void onMove() {
        // 子类可以重写此方法实现自定义移动逻辑
    }

    /**
     * 更新物体状态
     */
    public void update() {
        // 检查生命周期状态
        if (lifecycleState != LifecycleState.INITIALIZED && lifecycleState != LifecycleState.ACTIVE) {
            return;
        }
        
        // 检查激活状态，如果未激活则不执行更新
        if (!active) {
            return;
        }
        
        // 如果是首次更新，设置为活动状态
        if (lifecycleState == LifecycleState.INITIALIZED) {
            lifecycleState = LifecycleState.ACTIVE;
        }
        
        frame++;

        // 调用自定义更新逻辑
        onUpdate();

        // 调用自定义移动逻辑
        onMove();

        // 更新位置
        x += vx;
        y += vy;
        
        // 更新角度
        angle += angularVelocity;
        
        // 检查边界，如果超出边界则设置为非激活状态
        if (isOutOfBounds()) {
            setActive(false);
        }
    }

    /**
     * 渲染物体（Java2D版本）
     * @param g 图形上下文
     * @throws IllegalStateException 如果坐标系统未初始化
     */
    public void render(Graphics2D g) {
        if (!active) return;

        requireCoordinateSystem();
        float[] screenCoords = toScreenCoords(x, y);
        float screenX = screenCoords[0];
        float screenY = screenCoords[1];

        g.setColor(color);
        g.fillOval((int)(screenX - size/2), (int)(screenY - size/2), (int)size, (int)size);
    }

    /**
     * 渲染物体,仅用于纯色物体渲染
     * @param renderer 渲染器
     * @throws IllegalStateException 如果坐标系统未初始化
     */
    @Override
    public void render(IRenderer renderer) {
        if (!active) return;

        requireCoordinateSystem();
        float[] screenCoords = toScreenCoords(x, y);
        float screenX = screenCoords[0];
        float screenY = screenCoords[1];
        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;
        float a = color.getAlpha() / 255.0f;
        renderer.drawCircle(screenX, screenY, size/2, r, g, b, a);
    }
    
    /**
     * 渲染物体,使用纹理，支持OpenGL
     * @param renderer 渲染器
     * @param textureId 纹理ID
     * @param texX 素材在图片内的X坐标
     * @param texY 素材在图片内的Y坐标
     * @param texWidth 素材宽度
     * @param texHeight 素材高度
     * @param imgWidth 图片总宽度
     * @param imgHeight 图片总高度
     * @throws IllegalStateException 如果坐标系统未初始化
     */
    public void render(IRenderer renderer, int textureId, float texX, float texY, float texWidth, float texHeight, float imgWidth, float imgHeight) {
        if (!active) return;

        requireCoordinateSystem();
        float[] screenCoords = toScreenCoords(x, y);
        float screenX = screenCoords[0];
        float screenY = screenCoords[1];
        
        // 计算纹理坐标（归一化到0-1范围）
        float texCoordX1 = texX / imgWidth;
        float texCoordY1 = texY / imgHeight;
        float texWidthNorm = texWidth / imgWidth;
        float texHeightNorm = texHeight / imgHeight;
        
        // 使用IRenderer接口绘制图片，避免直接操作OpenGL状态
        renderer.drawImage(textureId, screenX - size/2, screenY - size/2, size, size, texCoordX1, texCoordY1, texWidthNorm, texHeightNorm);
    }

    /**
     * 在屏幕中渲染物体
     * @param renderer 渲染器
     * @throws IllegalStateException 如果坐标系统未初始化
     */
    public void renderOnScreen(IRenderer renderer) {
        render(renderer);
    }

    /**
     * 检查物体是否超出边界
     * @return 是否超出边界
     * @throws IllegalStateException 如果坐标系统未初始化
     */
    //TODO,整理到新的工具类
    public boolean isOutOfBounds() {
        requireCoordinateSystem();
        CoordinateSystem cs = sharedCoordinateSystem;
        float leftBound = cs.getLeftBound() - size;
        float rightBound = cs.getRightBound() + size;
        float topBound = cs.getTopBound() - size;
        float bottomBound = cs.getBottomBound() + size;
        return x < leftBound || x > rightBound || y < topBound || y > bottomBound;
    }

    /**
     * 移动到指定坐标
     * @param x 目标X坐标
     * @param y 目标Y坐标
     */
    public void moveTo(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 检查物体是否激活
     * @return 是否激活
     */
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
     * 重置物体状态
     * 用于对象池回收和重用时，确保对象状态完全重置
     */
    public void reset() {
        this.active = true;
        this.frame = 0;
        this.x = 0;
        this.y = 0;
        this.vx = 0;
        this.vy = 0;
        this.angle = 0;
        this.angularVelocity = 0;
        this.lifecycleState = LifecycleState.CREATED;
    }
    
    /**
     * 销毁实体
     * 用于释放实体占用的资源
     */
    public void destroy() {
        if (lifecycleState != LifecycleState.DESTROYED) {
            // 执行销毁逻辑
            onDestroy();
            lifecycleState = LifecycleState.DESTROYED;
            active = false;
        }
    }
    
    /**
     * 自定义销毁逻辑
     * 子类可以重写此方法实现特定的销毁行为
     */
    protected void onDestroy() {
        // 子类可以重写此方法实现特定的销毁行为
    }
    
    /**
     * 获取生命周期状态
     * @return 生命周期状态
     */
    public LifecycleState getLifecycleState() {
        return lifecycleState;
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
     * 设置碰撞判定半径
     * @param hitboxRadius 碰撞判定半径
     */
    public void setHitboxRadius(float hitboxRadius) {
        this.hitboxRadius = hitboxRadius;
    }

    /**
     * 获取碰撞判定半径
     * @return 碰撞判定半径
     */
    public float getHitboxRadius() {
        return hitboxRadius;
    }
    
    /**
     * 获取角度
     * @return 角度（度）
     */
    public float getAngle() {
        return angle;
    }
    
    /**
     * 设置角度
     * @param angle 角度（度）
     */
    public void setAngle(float angle) {
        this.angle = angle;
    }
    
    /**
     * 获取角速度
     * @return 角速度（度/秒）
     */
    public float getAngularVelocity() {
        return angularVelocity;
    }
    
    /**
     * 设置角速度
     * @param angularVelocity 角速度（度/秒）
     */
    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }
    
    /**
     * 获取渲染层级
     * @return 渲染层级
     */
    @Override
    public int getRenderLayer() {
        return 3;
    }
    
    // ==================== 对象池操作方法 ====================
    
    /**
     * 从对象池创建实例
     * 注意：使用此方法前需要先调用 ObjectPoolConfig.initialize() 初始化对象池
     * @param <T> 对象类型
     * @param clazz 对象类型的 Class
     * @param args 构造函数参数
     * @return 对象实例
     */
    @SuppressWarnings("unchecked")
    public static <T extends Obj> T create(Class<T> clazz, Object... args) {
        try {
            // 尝试从对象池获取或创建对象
            try {
                // 获取或创建对象池
                ObjectPoolManager.getInstance().getOrCreatePool(clazz);
                
                // 尝试从对象池获取
                T object = ObjectPoolManager.getInstance().acquire(clazz);
                if (object != null) {
                    // 设置对象的属性
                    if (args.length >= 2) {
                        // 处理不同类型的参数
                        //TODO,存在访问安全问题
                        if (args[0] instanceof Number) {
                            object.setX(((Number) args[0]).floatValue());
                        }
                        if (args[1] instanceof Number) {
                            object.setY(((Number) args[1]).floatValue());
                        }
                    }
                    return object;
                }
            } catch (Exception poolEx) {
                // 对象池获取失败，记录异常
                System.err.println("Object pool acquire failed: " + poolEx.getMessage());
            }
            
            // 直接创建对象
            try {
                // 查找匹配的构造函数
                for (java.lang.reflect.Constructor<?> constructor : clazz.getConstructors()) {
                    if (constructor.getParameterCount() == args.length) {
                        T object = (T) constructor.newInstance(args);
                        
                        // 将新创建的对象添加到对象池中（如果对象池存在）
                        try {
                            ObjectPoolManager.getInstance().release(object);
                            // 重新从对象池获取，这样可以确保对象被正确跟踪
                            return ObjectPoolManager.getInstance().acquire(clazz);
                        } catch (Exception e) {
                            // 忽略异常，直接返回创建的对象
                        }
                        
                        return object;
                    }
                }
                throw new IllegalArgumentException("No suitable constructor found for " + clazz.getName());
            } catch (Exception ex) {
                throw new RuntimeException("Failed to create object: " + ex.getMessage(), ex);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create object: " + e.getMessage(), e);
        }
    }
    
    /**
     * 回收对象到对象池
     * @param <T> 对象类型
     * @param object 要回收的对象
     */
    public static <T extends Obj> void release(T object) {
        if (object == null) {
            return;
        }
        
        try {
            ObjectPoolManager.getInstance().release(object);
        } catch (Exception e) {
            // 如果对象池未初始化或失败，忽略异常
            // 对象会被 GC 回收
        }
    }
    
    /**
     * 加载素材
     * 使用OpenGL加载图片，支持从图片中截取特定区域的素材
     * @param path 图片文件路径
     * @param x 素材在图片内的X坐标
     * @param y 素材在图片内的Y坐标
     * @param width 素材宽度
     * @param height 素材高度
     * @return 纹理ID
     */
    //TODO,整理到新的工具类
    public static int loadTexture(String path, float x, float y, float width, float height) {
        // 直接使用GL11来加载纹理，不创建新的GLRenderer实例
        // 这样可以确保在同一个OpenGL上下文中加载纹理
        int textureId = -1;
        
        try (org.lwjgl.system.MemoryStack stack = org.lwjgl.system.MemoryStack.stackPush()) {
            java.nio.IntBuffer widthBuffer = stack.mallocInt(1);
            java.nio.IntBuffer heightBuffer = stack.mallocInt(1);
            java.nio.IntBuffer channelsBuffer = stack.mallocInt(1);
            
            // 尝试从文件系统直接读取
            java.nio.file.Path filePath = java.nio.file.Paths.get(path);
            if (!java.nio.file.Files.exists(filePath)) {
                // 如果文件不存在，尝试从类路径读取
                System.out.println("文件系统中找不到图片文件: " + path + "，尝试从类路径读取");
                // 从类路径读取图片
                java.io.InputStream inputStream = Obj.class.getClassLoader().getResourceAsStream(path);
                if (inputStream == null) {
                    System.err.println("图片文件不存在: " + path);
                    return -1;
                }
                
                // 读取输入流到字节数组
                byte[] bytes = inputStream.readAllBytes();
                inputStream.close();
                
                // 分配内存并填充数据
                java.nio.ByteBuffer buffer = org.lwjgl.system.MemoryUtil.memAlloc(bytes.length);
                buffer.put(bytes);
                buffer.flip();
                
                // 加载图片
                java.nio.ByteBuffer image = org.lwjgl.stb.STBImage.stbi_load_from_memory(buffer, widthBuffer, heightBuffer, channelsBuffer, 4);
                org.lwjgl.system.MemoryUtil.memFree(buffer);
                
                if (image == null) {
                    System.err.println("加载图片失败: " + org.lwjgl.stb.STBImage.stbi_failure_reason());
                    return -1;
                }
                
                // 创建纹理
                textureId = org.lwjgl.opengl.GL11.glGenTextures();
                org.lwjgl.opengl.GL11.glBindTexture(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, textureId);
                
                // 设置纹理参数
                org.lwjgl.opengl.GL11.glTexParameteri(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER, org.lwjgl.opengl.GL11.GL_LINEAR);
                org.lwjgl.opengl.GL11.glTexParameteri(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER, org.lwjgl.opengl.GL11.GL_LINEAR);
                
                // 保存宽度和高度值
                int imgWidth = widthBuffer.get();
                int imgHeight = heightBuffer.get();
                
                // 上传纹理数据
                org.lwjgl.opengl.GL11.glTexImage2D(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, 0, org.lwjgl.opengl.GL11.GL_RGBA, imgWidth, imgHeight, 0, org.lwjgl.opengl.GL11.GL_RGBA, org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE, image);
                
                // 释放图片数据
                org.lwjgl.stb.STBImage.stbi_image_free(image);
                
                System.out.println("从类路径加载纹理成功: " + path + " (" + imgWidth + "x" + imgHeight + ")");
            } else {
                // 从文件系统直接读取
                java.nio.ByteBuffer image = org.lwjgl.stb.STBImage.stbi_load(path, widthBuffer, heightBuffer, channelsBuffer, 4);
                
                if (image == null) {
                    System.err.println("加载图片失败: " + org.lwjgl.stb.STBImage.stbi_failure_reason());
                    return -1;
                }
                
                // 创建纹理
                textureId = org.lwjgl.opengl.GL11.glGenTextures();
                org.lwjgl.opengl.GL11.glBindTexture(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, textureId);
                
                // 设置纹理参数
                org.lwjgl.opengl.GL11.glTexParameteri(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER, org.lwjgl.opengl.GL11.GL_LINEAR);
                org.lwjgl.opengl.GL11.glTexParameteri(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER, org.lwjgl.opengl.GL11.GL_LINEAR);
                
                // 保存宽度和高度值
                int imgWidth = widthBuffer.get();
                int imgHeight = heightBuffer.get();
                
                // 上传纹理数据
                org.lwjgl.opengl.GL11.glTexImage2D(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, 0, org.lwjgl.opengl.GL11.GL_RGBA, imgWidth, imgHeight, 0, org.lwjgl.opengl.GL11.GL_RGBA, org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE, image);
                
                // 释放图片数据
                org.lwjgl.stb.STBImage.stbi_image_free(image);
                
                System.out.println("从文件系统加载纹理成功: " + path + " (" + imgWidth + "x" + imgHeight + ")");
            }
            
        } catch (Exception e) {
            System.err.println("加载纹理失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return textureId;
    }
}
