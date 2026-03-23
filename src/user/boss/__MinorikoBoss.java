package user.boss;

import java.awt.Color;
import stg.entity.enemy.Boss;
import user.spellcard.__MinorikoNonSpellcard1;
import user.spellcard.__MinorikoNonSpellcard2;
import user.spellcard.__MinorikoSpellcard1;
import user.spellcard.__MinorikoSpellcard2;

public class __MinorikoBoss extends Boss {

    public __MinorikoBoss(float x, float y) {
        super(x, y, 60, new Color(255, 165, 0));
    }

    @Override
    protected void initSpellcards() {
        addSpellcard(new __MinorikoNonSpellcard1(this));
        addSpellcard(new __MinorikoSpellcard1(this));
        addSpellcard(new __MinorikoNonSpellcard2(this));
        addSpellcard(new __MinorikoSpellcard2(this));
    }

    @Override
    protected void onTaskStart() {
    }

    @Override
    protected void onTaskEnd() {
    }
}
