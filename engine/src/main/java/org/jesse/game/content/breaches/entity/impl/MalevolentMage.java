package org.jesse.game.content.breaches.entity.impl;

import org.jesse.game.content.breaches.entity.BreachEntity;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.action.combat.magic.CombatSpell;

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
