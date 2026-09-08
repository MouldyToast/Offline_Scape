package org.jesse.game.content.rewards;

import com.google.common.eventbus.Subscribe;
import org.jesse.game.world.WorldThread;
import org.jesse.plugins.events.ServerLaunchEvent;

public final class RewardsModule {

    @Subscribe
    public static void on(ServerLaunchEvent event) {
        start(event.getWorldThread());
    }

    private static void start(WorldThread thread) {
        Rewards.load();
    }


}
