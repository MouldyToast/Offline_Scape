package org.jesse.game.content.breaches.entity.impl;

import org.jesse.game.content.breaches.entity.BreachEntity;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;

public class GreaterAbyssalDemon extends BreachEntity implements Spawnable, CombatScript {

    public GreaterAbyssalDemon(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int attack(Entity target) {
        if (!(target instanceof Player)) {
            return 0;
        }
        if (isWithinMeleeDistance(this, target)) {
            setAnimation(getCombatDefinitions().getAttackAnim());
            var maxHit = getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target);
            var hit = new Hit(this, maxHit, HitType.MELEE);
            delayHit(this, 0, target, hit);
            return getCombatDefinitions().getAttackSpeed();
        }
        return 0;
    }

    @Override
    public boolean validate(int id, String name) {
        return id == NpcId.GREATER_ABYSSAL_DEMON_12451;
    }
}
