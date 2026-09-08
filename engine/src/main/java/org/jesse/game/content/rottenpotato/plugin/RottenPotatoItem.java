package org.jesse.game.content.rottenpotato.plugin;

import org.jesse.game.content.rottenpotato.RottenPotatoAction;
import org.jesse.game.content.rottenpotato.RottenPotatoDialogue;
import org.jesse.game.content.rottenpotato.RottenPotatoItemOption;
import org.jesse.game.content.rottenpotato.handler.RottenPotatoActionHandler;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;

import java.util.List;
import java.util.Optional;

/**
 * @author Christopher
 * @since 3/23/2020
 */
public class RottenPotatoItem extends ItemPlugin {
    @Override
    public void handle() {
        for (RottenPotatoItemOption itemOption : RottenPotatoAction.itemOptionMap.keys()) {
            if (itemOption == RottenPotatoItemOption.NONE) {
                continue;
            }
            bind(itemOption.getItemOption(), ((player, item, slotId) -> {
                if (!player.getPrivilege().eligibleTo(PlayerPrivilege.SUPPORT)) {
                    player.getInventory().deleteItem(item);
                    return;
                }
                final String title = itemOption.getDialogueTitle();
                final List<RottenPotatoActionHandler> actions = RottenPotatoAction.getActions(player, itemOption);
                player.getDialogueManager().start(new RottenPotatoDialogue(player, title, Optional.empty(), actions));
            }));
        }
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.ROTTEN_POTATO};
    }
}
