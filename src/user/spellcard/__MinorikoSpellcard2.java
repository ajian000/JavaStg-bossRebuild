package user.spellcard;

import java.awt.Color;
import stg.entity.enemy.Boss;
import stg.entity.enemy.EnemySpellcard;
import user.bullet.SimpleDownBullet;

public class __MinorikoSpellcard2 extends EnemySpellcard {
    private int shootTimer = 0;
    private static final int SHOOT_INTERVAL = 15;
    private float angleOffset = 0;
    private float spiralSpeed = 0.05f;

    public __MinorikoSpellcard2(Boss boss) {
        super("秋之符", 4, boss, 3000, 2400);
    }

    @Override
    protected void onStart() {
        shootTimer = 0;
        angleOffset = 0;
    }

    @Override
    protected void onEnd() {
    }

    @Override
    protected void updateLogic() {
        shootTimer++;
        if (shootTimer >= SHOOT_INTERVAL) {
            shoot();
            shootTimer = 0;
            angleOffset += spiralSpeed;
        }
    }

    private void shoot() {
        Boss boss = getBoss();
        float bulletSpeed = 2.0f;
        int bulletCount = 16;
        
        for (int i = 0; i < bulletCount; i++) {
            float angle = (float) Math.PI / 2 + (i * 2 * (float) Math.PI / bulletCount) + angleOffset;
            
            SimpleDownBullet bullet = new SimpleDownBullet(
                boss.getX(),
                boss.getY(),
                (float) (bulletSpeed * Math.cos(angle)),
                (float) (bulletSpeed * Math.sin(angle)),
                7.0f,
                Color.RED
            );
            
            if (boss.getGameWorld() != null) {
                boss.getGameWorld().addEnemyBullet(bullet);
            }
        }
        
        for (int i = 0; i < bulletCount; i++) {
            float angle = (float) Math.PI / 2 + (i * 2 * (float) Math.PI / bulletCount) - angleOffset;
            
            SimpleDownBullet bullet = new SimpleDownBullet(
                boss.getX(),
                boss.getY(),
                (float) (bulletSpeed * Math.cos(angle)),
                (float) (bulletSpeed * Math.sin(angle)),
                7.0f,
                Color.ORANGE
            );
            
            if (boss.getGameWorld() != null) {
                boss.getGameWorld().addEnemyBullet(bullet);
            }
        }
    }
}
