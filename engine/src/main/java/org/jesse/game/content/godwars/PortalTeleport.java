package org.jesse.game.content.godwars;

import org.jesse.game.content.skills.magic.spells.teleports.Teleport;
import org.jesse.game.content.skills.magic.spells.teleports.TeleportType;
import org.jesse.game.item.Item;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent;
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 14/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PortalTeleport implements Teleport {

    private final Location destination;

    @Override
    public TeleportType getType() {
        return TeleportType.HIGH_REVISION_TELEPORT;
    }

    @Override
    public Location destination() {
        return destination;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public double getExperience() {
        return 0;
    }

    @Override
    public int getRandomizationDistance() {
        return 0;
    }

    @Override
    public Item[] getRunes() {
        return null;
    }

    @Override
    public int getWildernessLevel() {
        return WILDERNESS_LEVEL;
    }

    @Override
    public boolean isCombatRestricted() {
        return false;
    }

    @Override
    public void onArrival(final Player player) {
        WorldTasksManager.schedule(() -> player.setRouteEvent(new TileEvent(player, new TileStrategy(new Location(player.getLocation()), 1), null)), 2);
    }

    public PortalTeleport(Location destination) {
        this.destination = destination;
    }
}
