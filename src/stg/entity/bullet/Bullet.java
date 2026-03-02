package stg.entity.bullet;

import java.awt.Color;

import stg.entity.base.Obj;
import stg.util.objectpool.Resettable;
//TODO:需完全重写
/**
 * 子弹类
 * @date 2026-01-19 使用中心原点坐标
 * @date 2026-02-20 支持对象池管理
 */
public abstract class Bullet extends Obj implements Resettable {
    protected int damage = 0; // @since 2026-01-23 子弹伤害，默认0（由玩家统一控制）
    
    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param vx X方向速度
     * @param vy Y方向速度
     * @param size 子弹大小
     * @param color 子弹颜色
     */
    public Bullet(float x, float y, float vx, float vy, float size, Color color) {
        super(x, y, vx, vy, size, color);
        // @since 2026-01-23 设置碰撞判定半径为size的5倍，确保高速子弹不会穿透敌人
        setHitboxRadius(size * 5.0f);
    }

    // ========== 伤害相关 ==========

    /**
     * 获取子弹伤害
     * @return 子弹伤害值
     * @since 2026-01-23 添加伤害获取方法
     */
    public int getDamage() {
        return damage;
    }

    /**
     * 设置子弹伤害
     * @param damage 伤害值
     * @since 2026-01-23 添加伤害设置方法
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * 任务开始时触发的方法
     * 用于处理开局对话等
     */
    protected abstract void onTaskStart();

    /**
     * 任务结束时触发的方法
     * 用于处理boss击破对话和道具掉落
     */
    protected abstract void onTaskEnd();
    
    /**
     * 重置子弹状态
     * 当子弹被回收到对象池时调用
     */
    @Override
    public void resetState() {
        // 重置子弹的基本属性
        setActive(true);
        setX(0);
        setY(0);
        setVx(0);
        setVy(0);
        setHitboxRadius(getSize() * 5.0f);
        damage = 0;
    }
}

