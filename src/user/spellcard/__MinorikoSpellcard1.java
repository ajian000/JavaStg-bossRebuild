package user.spellcard;

import java.awt.Color;
import stg.entity.enemy.Boss;
import stg.entity.enemy.EnemySpellcard;
import user.bullet.SimpleDownBullet;

public class __MinorikoSpellcard1 extends EnemySpellcard {
    private int shootTimer = 0;
    private static final int SHOOT_INTERVAL = 20;
    private float angleOffset = 0;

    public __MinorikoSpellcard1(Boss boss) {
        super("秋之符", 2, boss, 2500, 1800);
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
            angleOffset += 0.1f;
        }
    }

    private void shoot() {
        stg.entity.enemy.IBoss boss = getBoss();
        float bulletSpeed = 2.5f;
        int bulletCount = 12;
        
        for (int i = 0; i < bulletCount; i++) {
            float offsetX = (i - bulletCount / 2) * 10.0f;
            
            SimpleDownBullet bullet = new SimpleDownBullet(
                boss.getX() + offsetX,
                boss.getY(),
                bulletSpeed,
                6.0f,
                Color.ORANGE
            );
            
            if (boss.getGameWorld() != null) {
                boss.getGameWorld().addEnemyBullet(bullet);
            }
        }
    }
}
