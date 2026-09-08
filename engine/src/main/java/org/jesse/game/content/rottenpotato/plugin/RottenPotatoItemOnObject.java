package org.jesse.game.content.rottenpotato.plugin;

import org.jesse.game.content.rottenpotato.RottenPotatoAction;
import org.jesse.game.content.rottenpotato.RottenPotatoActionType;
import org.jesse.game.content.rottenpotato.RottenPotatoDialogue;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;
import org.jesse.game.world.object.WorldObject;

import java.util.Optional;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-12-03
 */
public class RottenPotatoItemOnObject implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (!player.getPrivilege().inherits(PlayerPrivilege.SUPPORT)) {
            player.getInventory().deleteItem(item);
            return;
        }
        player.getDialogueManager().start(new RottenPotatoDialogue(player, "Using on " + object.getName(),
            Optional.of(object),
            RottenPotatoAction.getActions(player, RottenPotatoActionType.ITEM_ON_OBJECT)));
    }

    @Override
    public Object[] getItems() {
        return new Object[] {
            ItemId.ROTTEN_POTATO
        };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
            46220
        };
    }
}
