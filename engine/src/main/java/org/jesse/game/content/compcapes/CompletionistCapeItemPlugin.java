package org.jesse.game.content.compcapes;

import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;

import static org.jesse.game.item.ids.ItemId.*;

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
