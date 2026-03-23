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
        } else {
            System.err.println("GameWorld is null, cannot fire bullet");
        }
    }

    /**
     * 加载纹理
     * @param renderer 渲染器实例
     * @return 纹理ID
     */
    private int loadTexture(IRenderer renderer) {
        // 使用GLRenderer的loadTexture方法加载纹理
        if (renderer instanceof stg.render.GLRenderer) {
            return ((stg.render.GLRenderer) renderer).loadTexture(IMAGE_PATH);
        }
        return -1;
    }

    /**
     * 渲染敌人（使用纹理）
     * @param renderer 渲染器
     */
    @Override
    public void render(IRenderer renderer) {
        try {
            if (!isActive()) return;

            // 延迟加载纹理，直到第一次渲染时
            if (textureId == -1) {
                textureId = loadTexture(renderer);
            }

            // 使用纹理渲染
            if (textureId != -1) {
                // 计算纹理坐标（归一化到0-1范围）
                float texCoordX1 = TEX_X / IMG_WIDTH;
                float texCoordY1 = TEX_Y / IMG_HEIGHT;
                float texCoordX2 = (TEX_X + TEX_WIDTH) / IMG_WIDTH;
                float texCoordY2 = (TEX_Y + TEX_HEIGHT) / IMG_HEIGHT;
                
                // 转换为屏幕坐标
                requireCoordinateSystem();
                float[] screenCoords = toScreenCoords(getX(), getY());
                float screenX = screenCoords[0];
                float screenY = screenCoords[1];
                
                // 使用renderer绘制图片
                renderer.drawImage(textureId, screenX - size/2, screenY - size/2, size, size, texCoordX1, texCoordY1, texCoordX2 - texCoordX1, texCoordY2 - texCoordY1);
            } else {
                // 纹理加载失败，使用默认渲染
                super.render(renderer);
            }

            // 渲染生命值条
            // 使用父类的renderHealthBar方法
            super.renderHealthBar(renderer);
        } catch (Exception e) {
            System.err.println("Error in Elf.render(): " + e.getMessage());
            e.printStackTrace();
        }
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