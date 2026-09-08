package org.jesse.game.world.entity.npc.impl.slayer.superior.impl;


import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.npc.combatdefs.AttackType;
import org.jesse.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;

import static org.jesse.game.npc.ids.NpcId.DREADBORN_ARAXYTE;

public class DreadbornAraxyte extends SuperiorNPC implements CombatScript {

    public DreadbornAraxyte(Player owner, NPC root, Location spawnLocation) {
        super(owner, root, DREADBORN_ARAXYTE, spawnLocation);
    }

    @Override
    public int attack(Entity target) {
        if (target == null) {
            return 1;  // nothing to smack
        }

        // play the attack animation
        this.setAnimation(new Animation(9140));

        // roll your random max hit
        int damage = CombatUtilities.getRandomMaxHit(
                this,
                31,
                AttackType.MELEE,
                target
        );

        // schedule the hit on them
        Hit hit = new Hit(this, damage, HitType.DEFAULT);
        target.scheduleHit(this, hit, 0);

        return 4;  // attack delay ticks
    }
}