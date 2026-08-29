package com.zenyte.game.content.breaches.entity.impl;

import com.zenyte.game.content.breaches.entity.BreachEntity;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
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
