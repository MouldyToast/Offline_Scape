package org.jesse.game.content.rottenpotato.plugin;

import org.jesse.game.content.rottenpotato.RottenPotatoAction;
import org.jesse.game.content.rottenpotato.RottenPotatoActionType;
import org.jesse.game.content.rottenpotato.RottenPotatoDialogue;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;

import java.util.Optional;

/**
 * @author Christopher
 * @since 3/27/2020
 */
public class RottenPotatoItemOnNpc implements ItemOnNPCAction {
    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        if (!player.getPrivilege().eligibleTo(PlayerPrivilege.SUPPORT)) {
            player.getInventory().deleteItem(item);
            return;
        }
        player.getDialogueManager().start(new RottenPotatoDialogue(player, "Using on " + npc.getName(player),
                Optional.of(npc),
                RottenPotatoAction.getActions(player, RottenPotatoActionType.ITEM_ON_NPC)));
    }

    @Override
    public Object[] getItems() {
        return new Object[]{ItemId.ROTTEN_POTATO};
    }

    @Override
    public Object[] getObjects() {
        return new Object[0];
    }
}
