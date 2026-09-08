package org.jesse.game.content.breaches;

import com.google.common.eventbus.Subscribe;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Colour;
import org.jesse.game.world.broadcasts.BroadcastType;
import org.jesse.game.world.broadcasts.WorldBroadcasts;
import org.jesse.plugins.events.LoginEvent;
import org.jesse.plugins.events.ServerLaunchEvent;
import org.jesse.utils.TimeUnit;

public class BreachScheduler implements WorldTask {

    private static BreachScheduler instance;

    /*
    Subscribe to login event so players know a breach is active
     */
    @Subscribe
    public static void onLogin(LoginEvent event) {
        if(BreachManager.breachActive()) {
            event.getPlayer().sendMessage(Colour.RS_PINK.wrap(
                    "A breach is currently active at "+BreachManager.getInstance().getBreachLocation().broadcastLocation+"." +
                            " Powerful monsters currently roam this area!"));
        }
    }


    /*
    Registers breaches to the world
     */
    @Subscribe
    public static void boot(ServerLaunchEvent event) {
        /*
        Schedule the actual breach
         */
        instance = new BreachScheduler();
        WorldTasksManager.schedule(instance,
                (int) TimeUnit.MINUTES.toTicks(BreachSettings.BREACH_DELAY),
                (int) TimeUnit.MINUTES.toTicks(BreachSettings.BREACH_INTERVAL));
        /*
        Schedule a broadcast before the actual breach spawns
         */
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                WorldBroadcasts.broadcast(null, BroadcastType.BREACHES, "A new breach is approaching in 10 minutes! Powerful monsters will spawn soon!");
                stop();
            }
        },
            (int) TimeUnit.MINUTES.toTicks(BreachSettings.BREACH_DELAY-10),
                (int) TimeUnit.MINUTES.toTicks(BreachSettings.BREACH_INTERVAL-10));
    }

    /*
    Creates a new instance of the breaches manager
     */

    @Override
    public void run() {
        BreachManager.createNewBreach();
    }

    /*
    returns the schedule instance
     */

    public static BreachScheduler getInstance() {
        return instance;
    }
}
