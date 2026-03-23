package user.boss;

import java.awt.Color;
import stg.entity.enemy.Boss;
import user.spellcard.__StandbySpellcard1;
import user.spellcard.__StandbySpellcard2;
import user.spellcard.__StandbySpellcard3;

public class __StandbyBoss extends Boss {

    public __StandbyBoss(float x, float y) {
        super(x, y, 60, new Color(128, 128, 255));
    }

    @Override
    protected void initSpellcards() {
        addSpellcard(new __StandbySpellcard1(this));
        addSpellcard(new __StandbySpellcard2(this));
        addSpellcard(new __StandbySpellcard3(this));
    }

    @Override
    protected void onTaskStart() {
    }

    @Override
    protected void onTaskEnd() {
    }
}