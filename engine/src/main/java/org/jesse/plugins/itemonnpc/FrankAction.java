package org.jesse.plugins.itemonnpc;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Player;

public class FrankAction extends NPCPlugin implements ItemOnNPCAction {

    private Animation PET_ANIMATION = new Animation(827);

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        if (player.getInventory().containsItem(item)) {
            switch (item.getId()) {
                case ItemId.COOKED_MYSTERY_MEAT -> player.sendMessage("You let the bloodveld eat the mystery meat. He seems to enjoy it quite a lot.");
                case ItemId.BLOOD_PINT -> player.sendMessage("You let the bloodveld drink the blood pint. He seems to enjoy it..");
            }
            player.getInventory().deleteItem(item);
            player.setAnimation(PET_ANIMATION);
        }
    }

    @Override
    public Object[] getItems() {
        return new Object[] {ItemId.COOKED_MYSTERY_MEAT, ItemId.BLOOD_PINT};
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {NpcId.FRANK_9749};
    }

    @Override
    public void handle() {
        bind("pet", (player, npc) -> {
            player.setForceTalk("Who's a good boy?");
            player.setAnimation(PET_ANIMATION);
            player.sendMessage("You pet the bloodveld.");
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {NpcId.FRANK_9749};
    }
}
