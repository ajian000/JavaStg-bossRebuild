package stg.entity.enemy;

/**
 * Boss状态枚举
 * 定义Boss的不同状态
 * @since 2026-03-17
 */
public enum BossState {
    ENTERING,    // 入场中
    ACTIVE,      // 活跃状态
    TRANSITION,  // 符卡切换中
    EXITING,     // 退场中
    DEFEATED     // 被击败
}