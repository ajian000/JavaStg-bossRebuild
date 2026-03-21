package stg.entity.enemy;

/**
 * 敌方符卡基类
 * 用于定义Boss的攻击模式和阶段
 * @since 2026-02-14
 */
public abstract class EnemySpellcard implements ISpellcard {
    protected String name; // 符卡名称，空字符串表示非符卡阶段
    protected int phase; // 对应阶段
    protected IBoss boss; // 所属Boss
    protected boolean active; // 是否激活
    protected int duration; // 符卡持续时间（帧数）
    protected int currentFrame; // 当前帧数
    protected int hp; // 当前阶段生命值
    protected int maxHp; // 当前阶段最大生命值
    
    /**
     * 构造函数
     * @param name 符卡名称，空字符串表示非符卡阶段
     * @param phase 对应阶段
     * @param boss 所属Boss
     * @param hp 阶段生命值
     */
    public EnemySpellcard(String name, int phase, IBoss boss, int hp) {
        this.name = name;
        this.phase = phase;
        this.boss = boss;
        this.hp = hp;
        this.maxHp = hp;
        this.active = false;
        this.duration = 0; // 0表示无时间限制
        this.currentFrame = 0;
    }
    
    /**
     * 构造函数
     * @param name 符卡名称，空字符串表示非符卡阶段
     * @param phase 对应阶段
     * @param boss 所属Boss
     * @param hp 阶段生命值
     * @param duration 符卡持续时间（帧数）
     */
    public EnemySpellcard(String name, int phase, IBoss boss, int hp, int duration) {
        this(name, phase, boss, hp);
        this.duration = duration;
    }
    
    /**
     * 开始符卡
     * @param boss Boss实例
     */
    @Override
    public void start(IBoss boss) {
        this.active = true;
        this.currentFrame = 0;
        this.hp = maxHp; // 重置生命值
        onStart();
    }
    
    /**
     * 开始符卡（兼容旧方法）
     */
    public void start() {
        start(this.boss);
    }
    
    /**
     * 结束符卡
     */
    public void end() {
        this.active = false;
        onEnd();
    }
    
    /**
     * 更新符卡逻辑
     */
    public void update() {
        if (!active) return;
        
        currentFrame++;
        
        // 检查持续时间
        if (duration > 0 && currentFrame >= duration) {
            end();
            return;
        }
        
        updateLogic();
    }
    
    /**
     * 符卡开始时调用
     */
    protected abstract void onStart();
    
    /**
     * 符卡结束时调用
     */
    protected abstract void onEnd();
    
    /**
     * 更新符卡逻辑
     */
    protected abstract void updateLogic();
    
    /**
     * 检查是否为符卡阶段
     * @return 是否为符卡阶段
     */
    public boolean isSpellcardPhase() {
        return !name.isEmpty();
    }
    
    /**
     * 获取符卡名称
     * @return 符卡名称
     */
    public String getName() {
        return name;
    }
    
    /**
     * 获取对应阶段
     * @return 对应阶段
     */
    public int getPhase() {
        return phase;
    }
    
    /**
     * 获取所属Boss
     * @return 所属Boss
     */
    public IBoss getBoss() {
        return boss;
    }
    
    /**
     * 检查是否激活
     * @return 是否激活
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * 获取当前帧数
     * @return 当前帧数
     */
    public int getCurrentFrame() {
        return currentFrame;
    }
    
    /**
     * 获取符卡持续时间
     * @return 符卡持续时间
     */
    public int getDuration() {
        return duration;
    }
    
    /**
     * 受到伤害
     * @param damage 伤害值
     */
    @Override
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
        }
    }
    
    /**
     * 受到伤害（兼容旧方法）
     * @param damage 伤害值
     * @return 是否被击败
     */
    public boolean takeDamageWithReturn(int damage) {
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            return true;
        }
        return false;
    }
    
    /**
     * 获取当前生命值
     * @return 当前生命值
     */
    public int getHp() {
        return hp;
    }
    
    /**
     * 获取最大生命值
     * @return 最大生命值
     */
    public int getMaxHp() {
        return maxHp;
    }
    
    /**
     * 设置生命值
     * @param hp 生命值
     */
    public void setHp(int hp) {
        this.hp = hp;
    }
    
    /**
     * 检查是否被击败
     * @return 是否被击败
     */
    public boolean isDefeated() {
        return hp <= 0;
    }
}