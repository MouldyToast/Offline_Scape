package com.zenyte.game.content.breaches.entity.impl;

import com.zenyte.game.content.breaches.entity.BreachEntity;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.npc.ids.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;

public class MalevolentMage extends BreachEntity implements Spawnable, CombatScript {

    public MalevolentMage(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int attack(final Entity target) {
        useSpell(CombatSpell.FIRE_BLAST, target, combatDefinitions.getMaxHit());
        return this.getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public boolean validate(int id, String name) {
        return id == NpcId.MALEVOLENT_MAGE_12456;
    }
}
