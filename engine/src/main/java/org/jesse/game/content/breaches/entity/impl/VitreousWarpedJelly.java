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
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;
import org.jetbrains.annotations.NotNull;

public class VitreousWarpedJelly extends BreachEntity implements Spawnable, CombatScript {

    public VitreousWarpedJelly(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    protected String notificationName(@NotNull final Player player) {
        return "jellie";
    }

    @Override
    public boolean validate(int id, String name) {
        return id == NpcId.VITREOUS_WARPED_JELLY_12457;
    }

    @Override
    public int attack(Entity target) {
        if (isWithinMeleeDistance(this, target)) {
            var def = getCombatDefinitions();
            if (def != null) {
                var maxHit = CombatUtilities.getRandomMaxHit(this, def.getMaxHit(), def.getAttackStyle(), target);
                var hit = new Hit(this, maxHit, HitType.MELEE);
                delayHit(this, 0, target, hit);
                return 6;
            }
        }
        return 0;
    }
}
