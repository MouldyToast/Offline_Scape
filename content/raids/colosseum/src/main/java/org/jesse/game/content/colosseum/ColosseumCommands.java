package org.jesse.game.content.colosseum;

import com.google.common.eventbus.Subscribe;
import org.jesse.game.world.entity.player.GameCommands;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;
import org.jesse.plugins.events.ServerLaunchEvent;

@SuppressWarnings("unused")
public class ColosseumCommands {

    @Subscribe
    public static void onLaunch(final ServerLaunchEvent event) {
        new GameCommands.Command(PlayerPrivilege.ADMINISTRATOR, "sol", "creates colosseum instance (wave system)", (p, args) -> {
            ColosseumInstance.createInstance(p);
        });
    }

}