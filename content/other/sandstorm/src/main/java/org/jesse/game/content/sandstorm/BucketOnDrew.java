package org.jesse.game.content.sandstorm;

import org.jesse.game.content.sandstorm.dialogue.DrewDepositDialogue;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.Player;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Chris
 * @since August 26 2020
 */
public class BucketOnDrew implements ItemOnNPCAction {
    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        player.getDialogueManager().start(new DrewDepositDialogue(player, npc));
    }

    @Override
    public Object[] getItems() {
        return new Object[]{ItemId.BUCKET, ItemDefinitions.getOrThrow(ItemId.BUCKET).getNotedOrDefault()};
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{NpcId.DREW};
    }
}
