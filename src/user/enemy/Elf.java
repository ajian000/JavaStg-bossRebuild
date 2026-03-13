package user.enemy;

import java.awt.Color;

import stg.core.GameWorld;
import stg.entity.enemy.Enemy;
import stg.render.IRenderer;
import user.bullet.SimpleDownBullet;

/**
 * 精灵敌人类 - 使用精灵图中的第一个敌人
 * @since 2026-02-26
 */
public class Elf extends Enemy {
    private static final float ENEMY_SPEED = 3.0f; // 敌人移动速度
    private static final float ENEMY_SIZE = 40.0f; // 敌人大小（原来的二倍）
    private static final Color ENEMY_COLOR = new Color(255, 100, 255); // 敌人颜色：粉色
    private static final int ENEMY_HP = 150; // 敌人生命值
    
    private int textureId = -1; // 纹理ID
    private static final String IMAGE_PATH = "resources/images/enemy1.png"; // 图片路径
    private static final float TEX_X = 256; // 素材在图片内的X坐标（左上角）- 中心坐标为 (272, 16) 的敌人
    private static final float TEX_Y = 0; // 素材在图片内的Y坐标（左上角）- 中心坐标为 (272, 16) 的敌人
    private static final float TEX_WIDTH = 32; // 精灵宽度
    private static final float TEX_HEIGHT = 32; // 精灵高度
    private static final float IMG_WIDTH = 512; // 图片总宽度
    private static final float IMG_HEIGHT = 512; // 图片总高度

    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     */
    public Elf(float x, float y) {
        super(x, y, ENEMY_SPEED, 0, ENEMY_SIZE, ENEMY_COLOR, ENEMY_HP);
        // 延迟加载纹理，直到第一次渲染时
        System.out.println("Elf created at (" + x + ", " + y + ")");
        // 生成时发射一次基础子弹
        fireBullet();
    }
    
    /**
     * 发射子弹
     */
    private void fireBullet() {
        // 获取游戏世界引用
        GameWorld world = getGameWorld();
        if (world != null) {
            // 创建基础子弹，竖直向下发射
            float bulletSpeed = 4.0f;
            float bulletSize = 8.0f;
            Color bulletColor = Color.RED;
            
            // 创建子弹，从屏幕中心发射，竖直向下
            SimpleDownBullet bullet = new SimpleDownBullet(0, 0, bulletSpeed, bulletSize, bulletColor);
            bullet.setPlayerBullet(false); // 标记为敌人子弹
            
            // 添加到游戏世界
            world.addEnemyBullet(bullet);
            System.out.println("Elf fired bullet at (0, 0) (screen center)");
        } else {
            System.out.println("GameWorld is null, cannot fire bullet");
        }
    }

    /**
     * 加载纹理
     * @return 纹理ID
     */
    private int loadTexture() {
        // 直接使用GL11来加载纹理，确保在同一个OpenGL上下文中加载
        int texId = -1;
        
        try (org.lwjgl.system.MemoryStack stack = org.lwjgl.system.MemoryStack.stackPush()) {
            java.nio.IntBuffer widthBuffer = stack.mallocInt(1);
            java.nio.IntBuffer heightBuffer = stack.mallocInt(1);
            java.nio.IntBuffer channelsBuffer = stack.mallocInt(1);
            
            // 尝试从文件系统直接读取
            java.nio.file.Path filePath = java.nio.file.Paths.get(IMAGE_PATH);
            java.nio.ByteBuffer image;
            
            if (!java.nio.file.Files.exists(filePath)) {
                // 如果文件不存在，尝试从类路径读取
                System.out.println("文件系统中找不到图片文件: " + IMAGE_PATH + "，尝试从类路径读取");
                // 从类路径读取图片
                java.io.InputStream inputStream = getClass().getClassLoader().getResourceAsStream(IMAGE_PATH);
                if (inputStream == null) {
                    System.err.println("图片文件不存在: " + IMAGE_PATH);
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
                image = org.lwjgl.stb.STBImage.stbi_load_from_memory(buffer, widthBuffer, heightBuffer, channelsBuffer, 4);
                org.lwjgl.system.MemoryUtil.memFree(buffer);
            } else {
                // 从文件系统直接读取
                image = org.lwjgl.stb.STBImage.stbi_load(IMAGE_PATH, widthBuffer, heightBuffer, channelsBuffer, 4);
            }
            
            if (image == null) {
                System.err.println("加载图片失败: " + org.lwjgl.stb.STBImage.stbi_failure_reason());
                return -1;
            }
            
            // 创建纹理
            texId = org.lwjgl.opengl.GL11.glGenTextures();
            org.lwjgl.opengl.GL11.glBindTexture(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, texId);
            
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
            
            System.out.println("加载纹理成功: " + IMAGE_PATH + " (" + imgWidth + "x" + imgHeight + ")");
        } catch (Exception e) {
            System.err.println("加载纹理失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return texId;
    }

    /**
     * 渲染敌人（使用纹理）
     * @param renderer 渲染器
     */
    @Override
    public void render(IRenderer renderer) {
        if (!isActive()) return;

        // 延迟加载纹理，直到第一次渲染时
        if (textureId == -1) {
            textureId = loadTexture();
            System.out.println("First render, loading texture: " + textureId);
        }

        // 调试信息
        System.out.println("Rendering Elf at (" + getX() + ", " + getY() + "), textureId: " + textureId);
        
        // 使用纹理渲染
        if (textureId != -1) {
            // 使用Obj类的纹理渲染方法，这样可以正确渲染图片中的特定区域
            System.out.println("Using texture render, texX: " + TEX_X + ", texY: " + TEX_Y + ", texWidth: " + TEX_WIDTH + ", texHeight: " + TEX_HEIGHT);
            render(renderer, textureId, TEX_X, TEX_Y, TEX_WIDTH, TEX_HEIGHT, IMG_WIDTH, IMG_HEIGHT);
        } else {
            // 纹理加载失败，使用默认渲染
            System.out.println("Texture ID is -1, using default render");
            super.render(renderer);
            System.out.println("Texture failed, using default render");
        }

        // 渲染生命值条
        renderHealthBar(renderer);
    }
    
    /**
     * 在屏幕中渲染敌人
     * @param renderer 渲染器
     */
    @Override
    public void renderOnScreen(IRenderer renderer) {
        render(renderer);
    }

    /**
     * 任务开始时触发的方法
     */
    @Override
    protected void onTaskStart() {
        // 空实现，不需要特殊行为
    }

    /**
     * 任务结束时触发的方法
     */
    @Override
    protected void onTaskEnd() {
        // 空实现，不需要特殊行为
    }
    
    /**
     * 更新敌人逻辑
     * @param canvasWidth 画布宽度
     * @param canvasHeight 画布高度
     */
    @Override
    public void update(int canvasWidth, int canvasHeight) {
        super.update(canvasWidth, canvasHeight);
        
        // 处理边界反弹逻辑
        float x = getX();
        float vx = getVx();
        
        // 计算屏幕边界
        float leftBound = -canvasWidth / 2.0f + getSize();
        float rightBound = canvasWidth / 2.0f - getSize();
        
        // 碰到左边界，向右移动
        if (x <= leftBound && vx < 0) {
            setVx(2.0f);
            setX(leftBound);
        }
        // 碰到右边界，向左移动
        else if (x >= rightBound && vx > 0) {
            setVx(-2.0f);
            setX(rightBound);
        }
    }
    
    /**
     * 重置对象状态
     * 用于对象池回收和重用时
     */
    @Override
    public void resetState() {
        super.resetState();
        // 重置Elf特有的属性
        size = ENEMY_SIZE;
        color = ENEMY_COLOR;
        // 重置纹理ID
        textureId = -1;
    }
}