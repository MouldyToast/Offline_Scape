package org.jesse.game.content.breaches.entity.impl;

import org.jesse.game.content.breaches.entity.BreachEntity;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.npc.combatdefs.AttackType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;

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
