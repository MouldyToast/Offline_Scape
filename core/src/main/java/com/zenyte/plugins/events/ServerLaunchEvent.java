package com.zenyte.plugins.events;

import com.near_reality.game.world.info.WorldProfile;
import com.zenyte.game.world.WorldThread;
import com.zenyte.plugins.Event;

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
