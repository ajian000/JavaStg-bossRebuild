package user.enemy;

import java.awt.Color;

import stg.entity.base.Obj;
import stg.entity.enemy.Enemy;
import user.bullet.SimpleDownBullet;

public class __FairyEnemy extends Enemy {
    private static final float FAIRY_SIZE = 20.0f;
    private static final Color FAIRY_COLOR = new Color(255, 200, 200);
    private static final int FAIRY_HP = 100;
    private static final float FAIRY_SPEED = 3.0f;
    private static final int SHOOT_INTERVAL = 60;
    private int shootTimer = 0;

    public __FairyEnemy(float x, float y) {
        super(x, y, 0, FAIRY_SPEED, FAIRY_SIZE, FAIRY_COLOR, FAIRY_HP);
    }

    @Override
    public void update(int canvasWidth, int canvasHeight) {
        super.update(canvasWidth, canvasHeight);
        
        if (getGameWorld() != null) {
            shootTimer++;
            if (shootTimer >= SHOOT_INTERVAL) {
                shoot();
                shootTimer = 0;
            }
        }
    }

    private void shoot() {
        float bulletSpeed = 4.0f;
        
        SimpleDownBullet bullet = Obj.create(SimpleDownBullet.class,
            getX(),
            getY(),
            bulletSpeed,
            5.0f,
            Color.RED
        );
        
        if (getGameWorld() != null) {
            getGameWorld().addEnemyBullet(bullet);
        }
    }

    @Override
    protected void onTaskStart() {
    }

    @Override
    protected void onTaskEnd() {
    }
    
    @Override
    public void resetState() {
        super.resetState();
        this.shootTimer = 0;
    }
}
