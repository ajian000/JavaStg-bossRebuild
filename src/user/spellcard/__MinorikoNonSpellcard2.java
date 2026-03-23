package user.spellcard;

import java.awt.Color;
import stg.entity.enemy.Boss;
import stg.entity.enemy.EnemySpellcard;
import user.bullet.SimpleDownBullet;

public class __MinorikoNonSpellcard2 extends EnemySpellcard {
    private int shootTimer = 0;
    private static final int SHOOT_INTERVAL = 25;
    private float moveDirection = 1.0f;
    private float moveTimer = 0;

    public __MinorikoNonSpellcard2(Boss boss) {
        super("", 3, boss, 2000);
    }

    @Override
    protected void onStart() {
        shootTimer = 0;
        moveTimer = 0;
        moveDirection = 1.0f;
    }

    @Override
    protected void onEnd() {
    }

    @Override
    protected void updateLogic() {
        stg.entity.enemy.IBoss boss = getBoss();
        
        moveTimer++;
        if (moveTimer >= 60) {
            moveDirection *= -1;
            moveTimer = 0;
        }
        
        boss.setVx(moveDirection * 1.5f);
        
        shootTimer++;
        if (shootTimer >= SHOOT_INTERVAL) {
            shoot();
            shootTimer = 0;
        }
    }

    private void shoot() {
        stg.entity.enemy.IBoss boss = getBoss();
        float bulletSpeed = 3.5f;
        int bulletCount = 6;
        
        for (int i = 0; i < bulletCount; i++) {
            float offsetX = (i - bulletCount / 2) * 12.0f;
            
            SimpleDownBullet bullet = new SimpleDownBullet(
                boss.getX() + offsetX,
                boss.getY(),
                bulletSpeed,
                5.0f,
                Color.YELLOW
            );
            
            if (boss.getGameWorld() != null) {
                boss.getGameWorld().addEnemyBullet(bullet);
            }
        }
    }
}
