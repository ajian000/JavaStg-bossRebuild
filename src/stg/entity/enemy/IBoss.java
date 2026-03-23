package stg.entity.enemy;

import stg.render.IRenderer;

/**
 * Boss接口
 * 定义Boss的核心行为
 * @since 2026-03-17
 */
public interface IBoss {
    /**
     * 开始Boss
     */
    void start();
    
    /**
     * 更新Boss状态
     */
    void update();
    
    /**
     * 渲染Boss
     * @param renderer 渲染器
     */
    void render(IRenderer renderer);
    
    /**
     * 受到伤害
     * @param damage 伤害值
     */
    void takeDamage(int damage);
    
    /**
     * 获取Boss状态
     * @return Boss状态
     */
    BossState getState();
    
    /**
     * 获取当前符卡
     * @return 当前符卡
     */
    ISpellcard getCurrentSpellcard();
    
    /**
     * 加载精灵图资源
     * @param path 图片路径
     * @param x 素材X坐标
     * @param y 素材Y坐标
     * @param width 素材宽度
     * @param height 素材高度
     */
    void loadSprite(String path, float x, float y, float width, float height);
    
    /**
     * 获取X坐标
     * @return X坐标
     */
    float getX();
    
    /**
     * 获取Y坐标
     * @return Y坐标
     */
    float getY();
    
    /**
     * 获取大小
     * @return 大小
     */
    float getSize();
    
    /**
     * 获取游戏世界引用
     * @return 游戏世界引用
     */
    stg.core.GameWorld getGameWorld();
    
    /**
     * 设置X方向速度
     * @param vx X方向速度
     */
    void setVx(float vx);
}