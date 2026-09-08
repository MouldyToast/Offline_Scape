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
public class RedCarpetInstance extends DynamicArea {

    private static final Location doorTile = new ImmutableLocation(2199, 4395, 0);

    public RedCarpetInstance(AllocatedArea allocatedArea) {
        super(allocatedArea, 274, 548);
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
        return "Easter event red carpet cutscene area";
    }

    @Override
    public Location onLoginLocation() {
        return new Location(2208, 4394, 0);
    }
}