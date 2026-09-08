package org.jesse.game.content.area.prifddinas.zalcano.combat.impl;

import org.jesse.game.content.area.prifddinas.zalcano.ZalcanoInstance;
import org.jesse.game.content.area.prifddinas.zalcano.combat.ZalcanoAttack;
import org.jesse.game.content.area.prifddinas.zalcano.combat.actions.DroppingBouldersAction;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;

import java.util.HashSet;
import java.util.Set;

/**
 * Executes a dropping boulder attack
 */
public class DroppingBouldersAttack implements ZalcanoAttack {

    public static final Animation ANIM = new Animation(8435);

    @Override
    public void execute(ZalcanoInstance instance) {
        instance.getZalcano().setAnimation(ANIM);

        WorldTasksManager.schedule(() -> {
            Set<Location> locations = new HashSet<>();
            for (var player : instance.getPlayers()) {
                if (Utils.random(2) == 1) {
                    locations.add(player.getLocation().copy());
                }
            }

            var randomAmount = Math.max(Utils.random(0, instance.getPlayers().size() * 2), 6);
            for (int i = 0; i < randomAmount; i++) {
                var randomPos = instance.getLair().getRandomPosition();
                // Stop dropping on formations
                if (World.isTileClipped(randomPos, 1)) {
                    locations.add(randomPos);
                }
            }

            WorldTasksManager.schedule(new DroppingBouldersAction(locations, instance), 0, 0);
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
