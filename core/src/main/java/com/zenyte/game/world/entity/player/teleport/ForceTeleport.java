package com.zenyte.game.world.entity.player.teleport;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris | 19/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ForceTeleport implements Teleport {

    public ForceTeleport(@NotNull final Location location) {
        this.location = location;
    }

    private final Location location;

    private static final Logger logger = LoggerFactory.getLogger(ForceTeleport.class);

    @Override
    public TeleportType getType() {
        return TeleportType.INSTANT_UNSAFE;
    }

    @Override
    public Location destination() {
        return location;
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
        return 0;
    }

    @Override
    public boolean isCombatRestricted() {
        return false;
    }

    @Override
    public void teleport(final Player player) {
        try {
            getType().getStructure().teleport(player, this);
        } catch (final Exception e) {
            logger.error("", e);
        }
    }
}
