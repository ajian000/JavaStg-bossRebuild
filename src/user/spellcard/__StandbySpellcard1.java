package user.spellcard;

import stg.entity.enemy.Boss;
import stg.entity.enemy.EnemySpellcard;

public class __StandbySpellcard1 extends EnemySpellcard {

    public __StandbySpellcard1(Boss boss) {
        super("待机符卡1", 1, boss, 1200, 1800);
    }

    @Override
    protected void onStart() {
    }

    @Override
    protected void onEnd() {
    }

    @Override
    protected void updateLogic() {
    }
}