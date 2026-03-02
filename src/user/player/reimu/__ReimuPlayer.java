package user.player.reimu;

import stg.entity.player.Player;
import stg.render.IRenderer;
import stg.util.SpriteSheetRenderer;

/**
 * Reimu自机类 - 使用reimu.png精灵表
 * @since 2026-03-01
 */
public class __ReimuPlayer extends Player {
    private int animationFrame; // 动画帧索引
    private int animationCounter; // 动画计数器
    private static final int ANIMATION_SPEED = 5; // 动画速度（每5帧切换一次）
    private int currentAnimation; // 当前动画状态：0-站立，1-左移，2-右移
    private int reimuTextureId; // Reimu精灵表纹理ID
    
    public __ReimuPlayer() {
        super(0, 0, 8.0f, 3.0f, 20);
        this.animationFrame = 0;
        this.animationCounter = 0;
        this.currentAnimation = 0; // 默认站立
        this.reimuTextureId = -1; // 初始化为无效ID
    }
    
    public __ReimuPlayer(float x, float y) {
        super(x, y, 8.0f, 3.0f, 20);
        this.animationFrame = 0;
        this.animationCounter = 0;
        this.currentAnimation = 0; // 默认站立
        this.reimuTextureId = -1; // 初始化为无效ID
    }
    
    @Override
    protected void onUpdate() {
        // 更新动画
        updateAnimation();
    }
    
    /**
     * 更新动画状态
     */
    private void updateAnimation() {
        animationCounter++;
        if (animationCounter >= ANIMATION_SPEED) {
            animationCounter = 0;
            animationFrame = (animationFrame + 1) % 8; // 8帧循环
        }
        
        // 根据移动方向更新动画状态
        if (getVelocityX() < 0) {
            currentAnimation = 1; // 左移
        } else if (getVelocityX() > 0) {
            currentAnimation = 2; // 右移
        } else {
            currentAnimation = 0; // 站立
        }
    }
    
    /**
     * 设置Reimu精灵表纹理ID
     * @param textureId 纹理ID
     */
    public void setReimuTextureId(int textureId) {
        this.reimuTextureId = textureId;
    }
    
    /**
     * 获取Reimu精灵表纹理ID
     * @return 纹理ID
     */
    public int getReimuTextureId() {
        return reimuTextureId;
    }
    
    @Override
    public void render(IRenderer renderer) {
        // 无敌闪烁效果
        boolean shouldRender = true;
        if (getInvincibleTimer() > 0) {
            int flashPhase = getInvincibleTimer() % 10;
            if (flashPhase < 5) {
                shouldRender = false;
            }
        }
        
        if (!shouldRender) return;
        
        // 转换为屏幕坐标
        requireCoordinateSystem();
        float[] screenCoords = toScreenCoords(getX(), getY());
        float screenX = screenCoords[0];
        float screenY = screenCoords[1];
        
        // 绘制精灵（使用精灵表裁剪）
        if (reimuTextureId != -1) {
            // 精灵表参数
            final int SPRITE_WIDTH = 48;
            final int SPRITE_HEIGHT = 48;
            final int SPRITES_PER_ROW = 8;
            final int SPRITE_SHEET_WIDTH = 384;
            final int SPRITE_SHEET_HEIGHT = 272;
            final float SCALE = 1.5f;
            
            // 使用精灵表渲染工具类绘制动画帧
            SpriteSheetRenderer.drawSpriteFrame(
                renderer,
                reimuTextureId,
                screenX,
                screenY,
                SPRITE_WIDTH,
                SPRITE_HEIGHT,
                SPRITE_SHEET_WIDTH,
                SPRITE_SHEET_HEIGHT,
                currentAnimation, // 0: 站立, 1: 左移, 2: 右移
                animationFrame,
                SPRITES_PER_ROW,
                SCALE
            );
        } else {
            //  fallback: 绘制简单的红色圆形
            renderer.drawCircle(screenX, screenY, getSize()/2, 1.0f, 0.4f, 0.4f, 1.0f);
        }
        
        // 低速模式时显示受击判定点
        if (isSlowMode()) {
            // TODO: 重写判定点绘制逻辑
            // 放大判定点，使其更明显
            float hitboxRadius = getHitboxRadius() * 2.0f;
            renderer.drawCircle(screenX, screenY, hitboxRadius, 1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    @Override
    protected void shoot() {
        // 实现射击逻辑，使用精灵表中的子弹
        // 这里可以创建子弹对象并添加到游戏世界
    }
}
