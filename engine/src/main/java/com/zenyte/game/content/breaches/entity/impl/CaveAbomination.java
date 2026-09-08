package com.zenyte.game.content.breaches.entity.impl;

import com.zenyte.game.content.breaches.entity.BreachEntity;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.npc.ids.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;

import java.util.List;

public class CaveAbomination extends BreachEntity implements Spawnable, CombatScript {

    private static final Animation ATTACK_1_ANIMATION = new Animation(4234);
    private static final Animation ATTACK_2_ANIMATION = new Animation(4235);

    public CaveAbomination(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(int id, String name) {
        return id == NpcId.CAVE_ABOMINATION_12454;
    }

    @Override
    public int attack(Entity target) {
        if (!(target instanceof Player)) return 0;
        var anims = List.of(ATTACK_1_ANIMATION, ATTACK_2_ANIMATION);
        if (isWithinMeleeDistance(this, target)) {
            setAnimation(Utils.random(anims));
            var maxHit = CombatUtilities.getRandomMaxHit(this, 20, AttackType.CRUSH, target);
            delayHit(this, 0, target, new Hit(this, maxHit, HitType.MELEE).setExecuteIfLocked());
            return getCombatDefinitions().getAttackSpeed();
        }
        return 0;
    }
}
