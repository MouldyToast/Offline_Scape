package com.zenyte.game.content.colosseum;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.world.entity.player.GameCommands;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.plugins.events.ServerLaunchEvent;

@SuppressWarnings("unused")
public class ColosseumCommands {

	@Subscribe
	public static void onLaunch(final ServerLaunchEvent event) {
		new GameCommands.Command(PlayerPrivilege.ADMINISTRATOR, "sol", "teleports directly to sol", (p, args) -> {
			ColosseumInstance.createInstance(p);
		});
	}

}
