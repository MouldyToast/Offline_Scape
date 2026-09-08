package org.jesse.game.content.area.prifddinas.zalcano.combat.actions;

import org.jesse.game.content.area.prifddinas.zalcano.ZalcanoConstants;
import org.jesse.game.content.area.prifddinas.zalcano.ZalcanoInstance;
import org.jesse.game.task.TickTask;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;
import org.jesse.game.world.entity.player.calog.CAType;

import java.util.Set;

public class DroppingBouldersAction extends TickTask {

    private final Set<Location> locations;
    private final ZalcanoInstance instance;

    public DroppingBouldersAction(final Set<Location> locations, final ZalcanoInstance instance) {
        this.locations = locations;
        this.instance = instance;
    }

    @Override
    public void run() {
        switch (ticks) {
            case 0:
                for (Location location : locations) {
                    World.sendGraphics(new Graphics(ZalcanoConstants.FALLEN_BOULDER_GFX), location);
                }
                break;
            case 7:
                for (Location location : locations) {
                    for (Player player : instance.getPlayers()) {
                        if (player.getLocation().hashCode() == location.hashCode()) {
                            player.getCombatAchievements().setCurrentTaskValue(CAType.PERFECT_ZALCANO, 0);
                            CombatUtilities.processHit(player, new Hit(instance.getZalcano(), Utils.random(1, 40), HitType.REGULAR));
                        }
                    }
                }
                this.stop();
                return;
        }
        ticks++;
    }


}
