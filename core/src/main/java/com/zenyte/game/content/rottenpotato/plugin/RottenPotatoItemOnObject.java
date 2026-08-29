package com.zenyte.game.content.rottenpotato.plugin;

import com.zenyte.game.content.rottenpotato.RottenPotatoAction;
import com.zenyte.game.content.rottenpotato.RottenPotatoActionType;
import com.zenyte.game.content.rottenpotato.RottenPotatoDialogue;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.game.world.object.WorldObject;

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
