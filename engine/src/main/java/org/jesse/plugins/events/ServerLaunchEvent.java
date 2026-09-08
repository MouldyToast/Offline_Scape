package org.jesse.plugins.events;

import org.jesse.game.world.info.WorldProfile;
import org.jesse.game.world.WorldThread;
import org.jesse.plugins.Event;

/**
 * @author Kris | 21/03/2019 23:46
 * @author Jire
 */
public final class ServerLaunchEvent implements Event {

    private final WorldProfile worldProfile;
    private final WorldThread worldThread;

    public ServerLaunchEvent(WorldProfile worldProfile, WorldThread worldThread) {
        this.worldProfile = worldProfile;
        this.worldThread = worldThread;
    }

    public WorldProfile getWorldProfile() {
        return worldProfile;
    }

    public WorldThread getWorldThread() {
        return this.worldThread;
    }

}
