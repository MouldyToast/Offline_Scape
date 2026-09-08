package com.zenyte.game.world.entity.player.login;

import com.zenyte.cores.ScheduledExternalizable;
import com.zenyte.logger.NearRealityLogger;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.util.Set;

/**
 * @author Kris | 24/07/2019 06:01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class InvitedPlayersList implements ScheduledExternalizable {

    private static final Logger log = NearRealityLogger.getLogger(InvitedPlayersList.class);

    public static final Set<String> invitedPlayers = new ObjectOpenHashSet<>();

    @Override
    public Logger getLog() {
        return log;
    }

    @Override
    public int writeInterval() {
        return 5;
    }

    @Override
    public void read(final @NotNull BufferedReader reader) {
        for (final String name : getGSON().fromJson(reader, String[].class)) {
            invitedPlayers.add(name.toLowerCase());
        }
    }

    @Override
    public void write() {
        out(getGSON().toJson(invitedPlayers.toArray()));
    }

    @Override
    public String path() {
        return "data/invitedplayers.json";
    }
}
