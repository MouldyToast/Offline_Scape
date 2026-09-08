package org.jesse.game.world.entity.player.dailychallenge;

import com.google.common.eventbus.Subscribe;
import org.jesse.game.world.PlayerEvent;
import org.jesse.game.world.WorldEvent;
import org.jesse.game.world.WorldEventListener;
import org.jesse.game.world.WorldHooks;
import org.jesse.game.model.RuneDate;
import org.jesse.game.world.entity.player.Player;
import org.jesse.logger.NearRealityLogger;
import org.jesse.plugins.events.InitializationEvent;
import org.jesse.plugins.events.ServerLaunchEvent;
import org.jesse.utils.StaticInitializer;
import org.slf4j.Logger;

import java.util.Calendar;

@StaticInitializer
public final class DailyChallengeModule {

    private static int
            dayOfYear;

    private static boolean
            removePlayerPostProcessListener = false;

    private static final Logger
            log = NearRealityLogger.getLogger(DailyChallengeManager.class);

    private static final WorldEventListener<PlayerEvent.PostProcess>
            CHECK_DATE_FOR_PLAYER = event -> RuneDate.checkDate(event.getPlayer());

    @Subscribe
    public static void onServerLaunch(final ServerLaunchEvent launchEvent) {
        final WorldHooks hooks = launchEvent.getWorldThread().getHooks();
        hooks.register(WorldEvent.Tick.class, tick -> {
            try {
                final int currentDayOfYear = dayOfYear;
                dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
                if (currentDayOfYear != dayOfYear) {
                    hooks.register(PlayerEvent.PostProcess.class, CHECK_DATE_FOR_PLAYER);
                    removePlayerPostProcessListener = true;
                } else if (removePlayerPostProcessListener) {
                    hooks.remove(PlayerEvent.PostProcess.class, CHECK_DATE_FOR_PLAYER);
                    removePlayerPostProcessListener = false;
                }
            } catch (Throwable e) {
                log.error("Failed to set day of the year {}", dayOfYear, e);
            }
        });
    }

    @Subscribe
    public static void onInitialization(final InitializationEvent event) {
        final Player player = event.getPlayer();
        final Player savedPlayer = event.getSavedPlayer();
        final DailyChallengeManager otherManager = savedPlayer.getDailyChallengeManager();
        if (otherManager == null || otherManager.challengeProgression == null) {
            return;
        }
        player.getDailyChallengeManager().setChallengeProgression(otherManager.challengeProgression);
    }
}
