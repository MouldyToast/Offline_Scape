package org.jesse.game.content.skills.magic.spells.teleports;

import org.jesse.game.content.skills.magic.Magic;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 19/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ForceTeleport implements Teleport {

    public ForceTeleport(@NotNull final Location location) {
        this.location = location;
    }

    private final Location location;

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
            Magic.logger.error("", e);
        }
    }
}
