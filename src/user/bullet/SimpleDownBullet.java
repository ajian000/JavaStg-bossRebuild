package user.bullet;

import java.awt.Color;

import stg.entity.bullet.Bullet;
import stg.entity.bullet.BulletSpriteSheet;

/**
 * 简单的竖直向下发射子弹类
 * 子弹发射后始终保持竖直向下的方向
 */
public class SimpleDownBullet extends Bullet {
    
    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param speed 子弹速度
     * @param size 子弹大小
     * @param color 子弹颜色
     */
    public SimpleDownBullet(float x, float y, float speed, float size, Color color) {
        super(x, y, 0, speed, size, color); // vx=0, vy=speed 确保竖直向下
    }
    
    /**
     * 构造函数（与Bullet类兼容的6参数版本）
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param vx X方向速度（会被忽略，始终设为0）
     * @param vy Y方向速度（会被取绝对值作为向下速度）
     * @param size 子弹大小
     * @param color 子弹颜色
     */
    public SimpleDownBullet(float x, float y, float vx, float vy, float size, Color color) {
        super(x, y, 0, Math.abs(vy), size, color); // vx=0, vy=绝对值确保竖直向下
    }
    
    /**
     * 构造函数（带精灵元素）
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param speed 子弹速度
     * @param size 子弹大小
     * @param color 子弹颜色
     * @param spriteElement 精灵元素
     * @param bulletType 子弹类型
     */
    public SimpleDownBullet(float x, float y, float speed, float size, Color color, 
                          BulletSpriteSheet.BulletElement spriteElement, BulletSpriteSheet.BulletType bulletType) {
        super(x, y, 0, speed, size, color, spriteElement, bulletType); // vx=0, vy=speed 确保竖直向下
    }
    
    /**
     * 自定义更新逻辑
     * 确保子弹始终保持竖直向下的方向
     */
    @Override
    protected void onUpdate() {
        // 确保X方向速度为0，保持竖直向下
        setVx(0);
        // 调用父类的更新逻辑
        super.onUpdate();
    }
    
    /**
     * 设置子弹速度
     * 重写此方法，确保子弹始终保持竖直向下
     * @param speed 子弹速度
     */
    @Override
    public void setSpeed(float speed) {
        // 保持X方向速度为0，只设置Y方向速度
        setVx(0);
        setVy(Math.abs(speed)); // 确保速度为正数，向下运动
    }
    
    /**
     * 设置子弹方向
     * 重写此方法，忽略方向设置，始终保持竖直向下
     * @param direction 子弹方向（此参数会被忽略）
     */
    @Override
    public void setDirection(float direction) {
        // 忽略方向设置，始终保持竖直向下
        float speed = getSpeed();
        setVx(0);
        setVy(Math.abs(speed));
    }
    
    /**
     * 重置对象状态
     * 用于对象池回收和重用时
     */
    @Override
    public void resetState() {
        super.resetState();
        // 确保子弹始终保持竖直向下
        setVx(0);
        setVy(Math.abs(getVy()));
    }
}
