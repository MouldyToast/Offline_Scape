package org.jesse.game.content.tombsofamascut.object;

import org.jesse.game.content.tombsofamascut.encounter.SecondWardenEncounter;
import org.jesse.game.content.tombsofamascut.raid.EncounterType;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent;
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Savions
 */
public class RewardCrystalAction implements ObjectAction {

    @Override
    public void handle(Player player, WorldObject object, String name, int optionId, String option) {
        final Location location = object.getLocation().transform(0, 3);
        player.setRouteEvent(new TileEvent(player, new TileStrategy(location), getRunnable(player, object, name, optionId, option), getDelay()));
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (player.getArea() instanceof final SecondWardenEncounter encounter) {
            player.getTOAManager().enter(false, EncounterType.REWARD_ROOM);
            encounter.getPlayers().forEach(p -> {
                if (p != null && !player.getUsername().equals(p.getUsername())) {
                    p.sendMessage(player.getName() + " has proceeded to Osmumten's Burial Chamber. Join " +
                            (player.getAppearance().isMale() ? "him" : "her") + "...");
                }
            });
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {45138};
    }
}
