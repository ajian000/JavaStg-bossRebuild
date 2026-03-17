package stg.entity.enemy;

/**
 * 符卡基类
 * 实现 ISpellcard 接口，为具体符卡类提供基础功能
 * @since 2026-03-17
 */
public abstract class Spellcard implements ISpellcard {
    protected String name; // 符卡名称，空字符串表示非符卡阶段
    protected IBoss boss; // 所属Boss
    protected boolean active; // 是否激活
    protected int duration; // 符卡持续时间（帧数）
    protected int currentFrame; // 当前帧数
    protected int hp; // 当前阶段生命值
    protected int maxHp; // 当前阶段最大生命值
    
    /**
     * 构造函数
     * @param name 符卡名称，空字符串表示非符卡阶段
     * @param hp 阶段生命值
     */
    public Spellcard(String name, int hp) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.active = false;
        this.duration = 0; // 0表示无时间限制
        this.currentFrame = 0;
    }
    
    /**
     * 构造函数
     * @param name 符卡名称，空字符串表示非符卡阶段
     * @param hp 阶段生命值
     * @param duration 符卡持续时间（帧数）
     */
    public Spellcard(String name, int hp, int duration) {
        this(name, hp);
        this.duration = duration;
    }
    
    /**
     * 开始符卡
     * @param boss Boss实例
     */
    @Override
    public void start(IBoss boss) {
        this.boss = boss;
        this.active = true;
        this.currentFrame = 0;
        this.hp = maxHp; // 重置生命值
        onStart();
    }
    
    /**
     * 结束符卡
     */
    @Override
    public void end() {
        this.active = false;
        onEnd();
    }
    
    /**
     * 更新符卡逻辑
     */
    @Override
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
     * 检查是否被击败
     * @return 是否被击败
     */
    @Override
    public boolean isDefeated() {
        return hp <= 0;
    }
    
    /**
     * 获取符卡名称
     * @return 符卡名称
     */
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * 获取当前生命值
     * @return 当前生命值
     */
    @Override
    public int getHp() {
        return hp;
    }
    
    /**
     * 获取最大生命值
     * @return 最大生命值
     */
    @Override
    public int getMaxHp() {
        return maxHp;
    }
    
    /**
     * 检查是否为符卡阶段
     * @return 是否为符卡阶段
     */
    @Override
    public boolean isSpellcardPhase() {
        return !name.isEmpty();
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
}