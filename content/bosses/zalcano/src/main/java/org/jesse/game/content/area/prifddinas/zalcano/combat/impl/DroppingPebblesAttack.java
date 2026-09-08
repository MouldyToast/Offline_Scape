package org.jesse.game.content.area.prifddinas.zalcano.combat.impl;

import org.jesse.game.content.area.prifddinas.zalcano.ZalcanoInstance;
import org.jesse.game.content.area.prifddinas.zalcano.combat.ZalcanoAttack;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;

import static org.jesse.game.content.area.prifddinas.zalcano.combat.impl.DroppingBouldersAttack.ANIM;

/**
 * Drops pebbles on players
 */
public class DroppingPebblesAttack implements ZalcanoAttack {

    public static final Graphics PEBBLES = new Graphics(60);

    @Override
    public void execute(ZalcanoInstance instance) {
        var players = instance.getPlayers();
        instance.getZalcano().setAnimation(ANIM);

        WorldTasksManager.schedule(()-> {
            for (var player :  players) {
                CombatUtilities.processHit(player, new Hit(instance.getZalcano(), Utils.random(1, 8), HitType.DEFAULT));
            }
            int randomAmount = Utils.random(4, 12);
            for (int i = 0; i < randomAmount; i++) {
                var pos = instance.getLair().getRandomPosition();
                World.sendGraphics(PEBBLES, pos);
            }
        }, 1);



    }

    @Override
    public boolean canProcess(ZalcanoInstance instance) {
        return instance.getPlayers().size() != 0;
    }

    @Override
    public void interrupt() {
        return;
    }
}
