package com.zenyte.game.content.breaches;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.plugins.events.LoginEvent;
import com.zenyte.plugins.events.ServerLaunchEvent;
import com.zenyte.utils.TimeUnit;

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
