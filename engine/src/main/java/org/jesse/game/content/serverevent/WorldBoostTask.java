package org.jesse.game.content.serverevent;

import com.google.common.eventbus.Subscribe;

import org.jesse.game.model.ui.testinterfaces.ServerEventsInterface;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.broadcasts.BroadcastType;
import org.jesse.game.world.broadcasts.WorldBroadcasts;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.events.ServerLaunchEvent;
import org.jesse.utils.StaticInitializer;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;

import static org.jesse.game.model.ui.InterfaceHandler.Journal.SERVER_EVENTS;
@StaticInitializer
public class WorldBoostTask implements WorldTask {

    @Subscribe
    public static void on(ServerLaunchEvent event) {
        WorldTasksManager.schedule(new WorldBoostTask(), 1 , 0);
    }

    private int tick;

    @Override
    public void run() {
        tick++;

        if(World.getWorldBoosts().isEmpty())
            return;

        ObjectListIterator<WorldBoost> iterator = World.getWorldBoosts().iterator();

        boolean updated = false;
        while (iterator.hasNext()) {
            WorldBoost worldBoost = iterator.next();

            if(worldBoost.isExpired()) {
                WorldBroadcasts.sendMessage(worldBoost.getBoostType().getMssg()+" boost deactivated", BroadcastType.XAMPHUR, false);
                iterator.remove();
                updated = true;
                // Discord broadcast deleted
            }
        }

        if(updated || tick % 50 == 0) {
            for (Player player : World.getPlayers()) {
                if (player.getInterfaceHandler().getJournal() == SERVER_EVENTS)
                    ServerEventsInterface.update(player);
            }
        }
    }
}
