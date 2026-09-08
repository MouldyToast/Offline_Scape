package org.jesse.game.content.rottenpotato.plugin;

import org.jesse.game.content.rottenpotato.RottenPotatoAction;
import org.jesse.game.content.rottenpotato.RottenPotatoActionType;
import org.jesse.game.content.rottenpotato.RottenPotatoDialogue;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnPlayerPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;

import java.util.Optional;

/**
 * @author Christopher
 * @since 3/23/2020
 */
public class RottenPotatoItemOnPlayer implements ItemOnPlayerPlugin {
    @Override
    public void handleItemOnPlayerAction(Player player, Item item, int slot, Player target) {
        if (!player.getPrivilege().eligibleTo(PlayerPrivilege.SUPPORT)) {
            player.getInventory().deleteItem(item);
            return;
        }
        player.getDialogueManager().start(new RottenPotatoDialogue(player, "Using on " + target.getName(),
                Optional.of(target),
                RottenPotatoAction.getActions(player, RottenPotatoActionType.ITEM_ON_PLAYER)));
    }

    @Override
    public int[] getItems() {
        return new int[]{ItemId.ROTTEN_POTATO};
    }
}
