package stg.entity.enemy;

/**
 * 符卡接口
 * 定义符卡的核心行为
 * @since 2026-03-17
 */
public interface ISpellcard {
    /**
     * 开始符卡
     * @param boss Boss实例
     */
    void start(IBoss boss);
    
    /**
     * 更新符卡逻辑
     */
    void update();
    
    /**
     * 结束符卡
     */
    void end();
    
    /**
     * 受到伤害
     * @param damage 伤害值
     */
    void takeDamage(int damage);
    
    /**
     * 检查是否被击败
     * @return 是否被击败
     */
    boolean isDefeated();
    
    /**
     * 获取符卡名称
     * @return 符卡名称
     */
    String getName();
    
    /**
     * 获取当前生命值
     * @return 当前生命值
     */
    int getHp();
    
    /**
     * 获取最大生命值
     * @return 最大生命值
     */
    int getMaxHp();
    
    /**
     * 检查是否为符卡阶段
     * @return 是否为符卡阶段
     */
    boolean isSpellcardPhase();
}