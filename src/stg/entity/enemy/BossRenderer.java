package stg.entity.enemy;

import java.awt.Color;

import stg.render.IRenderer;

/**
 * Boss渲染器
 * 负责Boss的渲染逻辑，包括精灵图渲染和生命值条渲染
 * @since 2026-03-17
 */
public class BossRenderer {
    /**
     * 渲染Boss
     * @param boss Boss实例
     * @param renderer 渲染器
     */
    public void renderBoss(IBoss boss, IRenderer renderer) {
        // 这里需要根据具体的Boss实现来渲染
        // 由于Boss可能是不同的实现类，这里暂时留空
        // 实际应用中，可能需要通过反射或其他方式获取Boss的具体属性
    }
    
    /**
     * 渲染生命值条
     * @param boss Boss实例
     * @param renderer 渲染器
     */
    public void renderHealthBar(IBoss boss, IRenderer renderer) {
        ISpellcard currentSpellcard = boss.getCurrentSpellcard();
        if (currentSpellcard != null) {
            // 计算生命值条的位置和大小
            float bossX = boss.getX();
            float bossY = boss.getY();
            float bossSize = boss.getSize();
            
            // 优化：避免重复计算
            float halfBossSize = bossSize * 1.5f;
            int barWidth = (int)(bossSize * 3); // 符卡生命值条更宽
            int barHeight = 6; // 符卡生命值条更高
            int barX = (int)(bossX - halfBossSize);
            int barY = (int)(bossY - bossSize - 15);

            // 背景
            renderer.drawRect(barX, barY, barWidth, barHeight, 0.5f, 0.5f, 0.5f, 1.0f);

            // 生命值
            float hpPercent = (float)currentSpellcard.getHp() / currentSpellcard.getMaxHp();
            // 优化：避免负数和超过1的百分比
            hpPercent = Math.max(0.0f, Math.min(1.0f, hpPercent));
            int hpBarWidth = (int)(barWidth * hpPercent);
            
            // 符卡阶段使用不同颜色
            if (currentSpellcard.isSpellcardPhase()) {
                renderer.drawRect(barX, barY, hpBarWidth, barHeight, 0.0f, 0.0f, 1.0f, 1.0f);
            } else {
                renderer.drawRect(barX, barY, hpBarWidth, barHeight, 1.0f, 0.0f, 0.0f, 1.0f);
            }
            
            // 显示符卡名称
            if (currentSpellcard.isSpellcardPhase()) {
                // 这里需要实现文本渲染
                // 由于渲染器接口可能不同，这里暂时留空
                // 实际应用中，可能需要通过渲染器的文本渲染方法来实现
            }
        }
    }
}