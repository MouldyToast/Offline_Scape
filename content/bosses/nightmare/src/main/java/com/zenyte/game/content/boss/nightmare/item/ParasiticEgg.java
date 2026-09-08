package com.zenyte.game.content.boss.nightmare.item;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ids.ItemId;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.npc.ids.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.RequestResult;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-12-10
 */
public class ParasiticEgg implements ItemOnNPCAction {
    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        if (player.getInventory().deleteItem(item).getResult() == RequestResult.SUCCESS) {
            player.getAttributes().put("nightmare_pet_metamorph", Boolean.TRUE);
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    item(item, "You have metamorphosed into a nightmare pet!");
                }
            });
        }
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ItemId.PARASITIC_EGG };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {NpcId.LITTLE_NIGHTMARE_9399 };
    }
}
