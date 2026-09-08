package org.jesse.game.content.pyramidplunder.item;

import org.jesse.game.content.pyramidplunder.npc.GuardianMummyDialogue;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemChain;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Chris
 * @since May 20 2020
 */
public class UnchargedSceptreOnMummy implements ItemOnNPCAction {
    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        player.getDialogueManager().start(new GuardianMummyDialogue(player, npc, true));
    }

    @Override
    public Object[] getItems() {
        return new Object[] {ItemChain.PHARAOH_SCEPTRE.first()};
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {NpcId.GUARDIAN_MUMMY};
    }
}
