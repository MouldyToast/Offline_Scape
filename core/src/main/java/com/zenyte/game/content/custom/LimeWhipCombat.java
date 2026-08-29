package com.zenyte.game.content.custom;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.action.combat.MeleeCombat;

public class LimeWhipCombat extends MeleeCombat {

    private boolean activated = false;
    public LimeWhipCombat(Entity target) {
        super(target);
    }

    @Override
    public int getSpeed() {
        return 3;
    }

    @Override
    protected void animate() {
        if (activated) {
            player.setAnimation(new Animation(1658));
            activated = false;
        } else
            super.animate();
    }
}
