package org.jesse.game.content.event.easter2020;

import org.jesse.game.world.entity.ImmutableLocation;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.DynamicArea;
import org.jesse.game.world.region.dynamicregion.AllocatedArea;
import org.jesse.plugins.SkipPluginScan;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 11/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SkipPluginScan
public class OutsideInstance extends DynamicArea {

    private static final Location doorTile = new ImmutableLocation(3085, 3475, 0);

    public OutsideInstance(AllocatedArea allocatedArea) {
        super(allocatedArea, 385, 433);
    }

    @Override
    public void constructed() {

    }

    @Override
    public void enter(Player player) {

    }

    @Override
    public void leave(Player player, boolean logout) {

    }

    public void spawnAtDoor(@NotNull final Player player) {
        player.setLocation(getLocation(doorTile));
    }

    @Override
    public String name() {
        return "Easter event outside cutscene area";
    }

    @Override
    public Location onLoginLocation() {
        return new Location(doorTile);
    }
}