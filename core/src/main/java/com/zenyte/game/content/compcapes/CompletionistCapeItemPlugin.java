package com.zenyte.game.content.compcapes;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;

import static com.zenyte.game.item.ItemId.*;

@SuppressWarnings("unused")
public class CompletionistCapeItemPlugin extends ItemPlugin {

	@Override
	public void handle() {
		bind("Wear", (player, item, slotId) -> {
			int compCapeTier = CompletionistCape.getCompletionistCapeTier(item.getId());
			if (compCapeTier > 0) {
				int applicableTier = CompletionistCape.checkRequirements(player);
				if (compCapeTier > applicableTier && !player.getPrivilege().inherits(PlayerPrivilege.DEVELOPER)) {
					CompletionistCape.noRequirements(player);
					return;
				}
				player.getEquipment().wear(slotId);
			}
		});
	}

	@Override
	public int[] getItems() {
		return new int[] {
			COMPLETIONIST_CAPE,
			COMPLETIONIST_CAPE_T
		};
	}
}
