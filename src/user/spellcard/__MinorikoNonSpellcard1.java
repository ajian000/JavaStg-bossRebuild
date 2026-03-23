package user.spellcard;

import java.awt.Color;
import stg.entity.enemy.Boss;
import stg.entity.enemy.EnemySpellcard;
import user.bullet.SimpleDownBullet;

public class __MinorikoNonSpellcard1 extends EnemySpellcard {
    private int shootTimer = 0;
    private static final int SHOOT_INTERVAL = 30;

    public __MinorikoNonSpellcard1(Boss boss) {
        super("", 1, boss, 2000);
    }

    @Override
    protected void onStart() {
        shootTimer = 0;
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
        }
    }

    private void shoot() {
        stg.entity.enemy.IBoss boss = getBoss();
        float bulletSpeed = 3.0f;
        int bulletCount = 8;
        
        for (int i = 0; i < bulletCount; i++) {
            float offsetX = (i - bulletCount / 2) * 15.0f;
            
            SimpleDownBullet bullet = new SimpleDownBullet(
                boss.getX() + offsetX,
                boss.getY(),
                bulletSpeed,
                5.0f,
                Color.RED
            );
            
            if (boss.getGameWorld() != null) {
                boss.getGameWorld().addEnemyBullet(bullet);
            }
        }
    }
}
