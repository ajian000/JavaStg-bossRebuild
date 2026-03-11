package user.bullet;

import java.awt.Color;
import stg.entity.bullet.Bullet;
import stg.util.objectpool.Pooled;

/**
 * 默认玩家主子弹类 - 沿y方向竖直向上的子弹
 * 弹速为75像素/tick（原速度的五倍）
 * @since 2026-02-11
 * @date 2026-02-22 添加@Pooled注解支持对象池
 */
@Pooled(initialCapacity = 50, maxCapacity = 200, name = "PlayerMainBulletPool")
public class DefaultPlayerMainBullet extends Bullet {
    private static final float BULLET_SPEED = 75.0f; // 弹速：75像素/tick（原速度的五倍）
    private static final float BULLET_SIZE = 4.0f; // 子弹大小
    private static final Color BULLET_COLOR = new Color(255, 255, 255); // 子弹颜色：白色

    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     */
    public DefaultPlayerMainBullet(float x, float y) {
        // 沿y方向竖直向上（Y轴正方向），X方向速度为0
        super(x, y, 0, BULLET_SPEED, BULLET_SIZE, BULLET_COLOR);
    }

    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param size 子弹大小
     */
    public DefaultPlayerMainBullet(float x, float y, float size) {
        // 沿y方向竖直向上（Y轴正方向），X方向速度为0
        super(x, y, 0, BULLET_SPEED, size, BULLET_COLOR);
    }

    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param size 子弹大小
     * @param color 子弹颜色
     */
    public DefaultPlayerMainBullet(float x, float y, float size, Color color) {
        // 沿y方向竖直向上（Y轴正方向），X方向速度为0
        super(x, y, 0, BULLET_SPEED, size, color);
    }

    /**
     * 任务开始时触发的方法
     */
    protected void onTaskStart() {
        // 空实现，不需要特殊行为
    }

    /**
     * 任务结束时触发的方法
     */
    protected void onTaskEnd() {
        // 空实现，不需要特殊行为
    }
}
