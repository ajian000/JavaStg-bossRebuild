package stg.util;

import stg.render.IRenderer;

/**
 * 精灵表渲染工具类
 * 用于处理精灵表的渲染，支持动画帧的裁剪和显示
 * @since 2026-03-02
 */
public class SpriteSheetRenderer {
    /**
     * 绘制精灵表中的单个动画帧
     * @param renderer 渲染器
     * @param textureId 纹理ID
     * @param screenX 屏幕X坐标
     * @param screenY 屏幕Y坐标
     * @param spriteWidth 精灵宽度
     * @param spriteHeight 精灵高度
     * @param spriteSheetWidth 精灵表宽度
     * @param spriteSheetHeight 精灵表高度
     * @param currentAnimation 当前动画状态（行索引）
     * @param animationFrame 当前动画帧（列索引）
     * @param spritesPerRow 每行的精灵数量
     * @param scale 缩放比例
     */
    public static void drawSpriteFrame(
            IRenderer renderer,
            int textureId,
            float screenX,
            float screenY,
            int spriteWidth,
            int spriteHeight,
            int spriteSheetWidth,
            int spriteSheetHeight,
            int currentAnimation,
            int animationFrame,
            int spritesPerRow,
            float scale
    ) {
        if (textureId == -1) {
            return;
        }
        
        // 计算当前帧的纹理坐标
        int row = currentAnimation;
        int col = animationFrame;
        
        // 计算纹理坐标（0-1范围）
        float texX = (float) (col * spriteWidth) / spriteSheetWidth;
        float texY = (float) (row * spriteHeight) / spriteSheetHeight;
        float texWidth = (float) spriteWidth / spriteSheetWidth;
        float texHeight = (float) spriteHeight / spriteSheetHeight;
        
        // 计算渲染大小
        float renderWidth = spriteWidth * scale;
        float renderHeight = spriteHeight * scale;
        
        // 绘制精灵
        renderer.drawImage(
                textureId,
                screenX - renderWidth / 2,
                screenY - renderHeight / 2,
                renderWidth,
                renderHeight,
                texX,
                texY,
                texWidth,
                texHeight
        );
    }
    
    /**
     * 绘制精灵表中的单个动画帧（默认缩放比例为1.0）
     * @param renderer 渲染器
     * @param textureId 纹理ID
     * @param screenX 屏幕X坐标
     * @param screenY 屏幕Y坐标
     * @param spriteWidth 精灵宽度
     * @param spriteHeight 精灵高度
     * @param spriteSheetWidth 精灵表宽度
     * @param spriteSheetHeight 精灵表高度
     * @param currentAnimation 当前动画状态（行索引）
     * @param animationFrame 当前动画帧（列索引）
     * @param spritesPerRow 每行的精灵数量
     */
    public static void drawSpriteFrame(
            IRenderer renderer,
            int textureId,
            float screenX,
            float screenY,
            int spriteWidth,
            int spriteHeight,
            int spriteSheetWidth,
            int spriteSheetHeight,
            int currentAnimation,
            int animationFrame,
            int spritesPerRow
    ) {
        drawSpriteFrame(
                renderer,
                textureId,
                screenX,
                screenY,
                spriteWidth,
                spriteHeight,
                spriteSheetWidth,
                spriteSheetHeight,
                currentAnimation,
                animationFrame,
                spritesPerRow,
                1.0f
        );
    }
}
