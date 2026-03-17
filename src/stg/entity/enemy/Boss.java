package stg.entity.enemy;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import stg.entity.base.Obj;
import stg.render.IRenderer;

/**
 * Boss基类
 * 管理Boss的入场、退场和符卡系统
 * @since 2026-03-17
 */
public abstract class Boss extends Obj implements IBoss {
    protected List<ISpellcard> spellcards;
    protected ISpellcard currentSpellcard;
    protected int currentPhase;
    protected int maxPhase;
    protected BossState state;
    protected int enterFrameCount;
    protected int exitFrameCount;
    protected static final int ENTER_DURATION = 120; // 入场动画持续120帧
    protected static final int EXIT_DURATION = 90; // 退场动画持续90帧
    
    // 精灵图资源
    protected int spriteTextureId;
    protected float spriteX;
    protected float spriteY;
    protected float spriteWidth;
    protected float spriteHeight;
    protected float imgWidth;
    protected float imgHeight;
    
    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param size 大小
     * @param color 颜色
     */
    public Boss(float x, float y, float size, Color color) {
        super(x, y, 0, 0, size, color);
        this.spellcards = new ArrayList<>();
        this.currentSpellcard = null;
        this.currentPhase = 0;
        this.maxPhase = 0;
        this.state = BossState.ENTERING;
        this.enterFrameCount = 0;
        this.exitFrameCount = 0;
        this.spriteTextureId = -1;
    }
    
    /**
     * 初始化符卡
     * 由子类实现，添加所有符卡
     */
    protected abstract void initSpellcards();
    
    /**
     * 开始Boss
     */
    @Override
    public void start() {
        // 初始化符卡
        initSpellcards();
        maxPhase = spellcards.size();
    }
    
    /**
     * 更新Boss状态
     */
    @Override
    public void update() {
        super.update();
        
        switch (state) {
            case ENTERING:
                updateEnterLogic();
                break;
            case ACTIVE:
                updateActiveLogic();
                break;
            case EXITING:
                updateExitLogic();
                break;
            default:
                break;
        }
    }
    
    /**
     * 渲染Boss
     * @param renderer 渲染器
     */
    @Override
    public void render(IRenderer renderer) {
        if (!isActive()) return;
        
        // 优化：避免重复的坐标系统检查
        if (!isCoordinateSystemInitialized()) return;
        
        // 如果有精灵图，使用精灵图渲染
        if (spriteTextureId != -1) {
            // 使用 IRenderer 接口的 drawImage 方法来渲染精灵图
            requireCoordinateSystem();
            float[] screenCoords = toScreenCoords(getX(), getY());
            float screenX = screenCoords[0];
            float screenY = screenCoords[1];
            float renderSize = getSize();
            
            // 计算纹理坐标（归一化到0-1范围）
            float texCoordX = spriteX / imgWidth;
            float texCoordY = spriteY / imgHeight;
            float texWidth = spriteWidth / imgWidth;
            float texHeight = spriteHeight / imgHeight;
            
            // 使用 IRenderer 接口的方法来渲染
            renderer.drawImage(spriteTextureId, screenX - renderSize/2, screenY - renderSize/2, renderSize, renderSize, texCoordX, texCoordY, texWidth, texHeight);
        } else {
            // 否则使用默认渲染
            super.render(renderer);
        }
        
        // 渲染生命值条
        renderHealthBar(renderer);
    }
    
    /**
     * 受到伤害
     * @param damage 伤害值
     */
    @Override
    public void takeDamage(int damage) {
        // 将伤害传递给当前符卡
        if (currentSpellcard != null) {
            currentSpellcard.takeDamage(damage);
        }
    }
    
    /**
     * 获取Boss状态
     * @return Boss状态
     */
    @Override
    public BossState getState() {
        return state;
    }
    
    /**
     * 获取当前符卡
     * @return 当前符卡
     */
    @Override
    public ISpellcard getCurrentSpellcard() {
        return currentSpellcard;
    }
    
    /**
     * 加载精灵图资源
     * @param path 图片路径
     * @param x 素材X坐标
     * @param y 素材Y坐标
     * @param width 素材宽度
     * @param height 素材高度
     */
    @Override
    public void loadSprite(String path, float x, float y, float width, float height) {
        this.spriteTextureId = loadTexture(path, x, y, width, height);
        this.spriteX = x;
        this.spriteY = y;
        this.spriteWidth = width;
        this.spriteHeight = height;
        
        // 假设图片大小与素材大小相同，实际应用中可能需要调整
        this.imgWidth = width;
        this.imgHeight = height;
    }
    
    /**
     * 更新入场逻辑
     */
    protected void updateEnterLogic() {
        enterFrameCount++;
        
        // 入场动画：从屏幕上方可见位置移动到指定位置
        float targetY = getY();
        // 从游戏逻辑坐标系的顶部边界附近进入，确保可见
        requireCoordinateSystem();
        stg.util.CoordinateSystem cs = getSharedCoordinateSystem();
        float topBound = cs.getTopBound();
        float enterStartY = topBound + 10; // 从顶部边界下方10像素处开始入场，确保可见
        
        setY(enterStartY + (targetY - enterStartY) * (float)enterFrameCount / ENTER_DURATION);
        
        if (enterFrameCount >= ENTER_DURATION) {
            state = BossState.ACTIVE;
            // 开始第一个符卡
            startNextSpellcard();
        }
    }
    
    /**
     * 更新活跃逻辑
     */
    protected void updateActiveLogic() {
        // 更新当前符卡
        if (currentSpellcard != null) {
            currentSpellcard.update();
            
            // 检查符卡是否被击败
            if (currentSpellcard.isDefeated()) {
                currentSpellcard.end();
                startNextSpellcard();
            }
        } else {
            // 如果没有当前符卡，开始第一个符卡
            startNextSpellcard();
        }
    }
    
    /**
     * 更新退场逻辑
     */
    protected void updateExitLogic() {
        exitFrameCount++;
        
        // 退场动画：向屏幕上方移动
        setY(getY() - 2.0f);
        
        if (exitFrameCount >= EXIT_DURATION) {
            setActive(false);
        }
    }
    
    /**
     * 开始下一个符卡
     */
    protected void startNextSpellcard() {
        currentPhase++;
        
        if (currentPhase > spellcards.size()) {
            // 所有符卡都已完成，开始退场
            state = BossState.EXITING;
            return;
        }
        
        // 获取并开始当前符卡
        currentSpellcard = spellcards.get(currentPhase - 1);
        currentSpellcard.start(this);
    }
    
    /**
     * 添加符卡
     * @param spellcard 符卡
     */
    protected void addSpellcard(ISpellcard spellcard) {
        spellcards.add(spellcard);
    }
    
    /**
     * 渲染生命值条
     * @param renderer 渲染器
     */
    protected void renderHealthBar(IRenderer renderer) {
        // 如果有当前符卡，显示符卡的生命值
        if (currentSpellcard != null) {
            // 计算生命值条的位置和大小
            float bossX = getX();
            float bossY = getY();
            float bossSize = getSize();
            
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
                String spellcardName = currentSpellcard.getName();
                float fontSize = 12.0f;
                float textX = barX;
                float textY = barY - 5.0f;
                float[] color = {1.0f, 1.0f, 1.0f, 1.0f}; // 白色
                renderer.drawText(spellcardName, textX, textY, fontSize, color);
            }
        }
    }
    
    /**
     * 获取当前阶段
     * @return 当前阶段
     */
    public int getCurrentPhase() {
        return currentPhase;
    }
    
    /**
     * 获取最大阶段
     * @return 最大阶段
     */
    public int getMaxPhase() {
        return maxPhase;
    }
    
    /**
     * 获取X坐标
     * @return X坐标
     */
    @Override
    public float getX() {
        return super.getX();
    }
    
    /**
     * 获取Y坐标
     * @return Y坐标
     */
    @Override
    public float getY() {
        return super.getY();
    }
    
    /**
     * 获取大小
     * @return 大小
     */
    @Override
    public float getSize() {
        return super.getSize();
    }
}