package org.jesse.game.content.boss.nightmare.item;

import org.jesse.game.world.entity.player.PlayerAttributesKt;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.RequestResult;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

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
