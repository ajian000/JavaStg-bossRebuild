package user.spellcard;

import stg.entity.enemy.Boss;
import stg.entity.enemy.EnemySpellcard;

public class __StandbySpellcard3 extends EnemySpellcard {

    public __StandbySpellcard3(Boss boss) {
        super("待机符卡3", 1, boss, 1200, 1800);
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