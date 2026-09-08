package org.jesse.game.content.minigame.inferno.plugins;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.impl.NPCChat;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.plugins.dialogue.ItemChat;

/**
 * @author Tommeh | 28/12/2019 | 23:10
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class FireCapeOnTzhaarKet implements ItemOnNPCAction {

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        if (player.getNumericAttribute("infernoVar").intValue() >= 1) {
            player.getDialogueManager().start(new NPCChat(player, npc.getId(), "You have already shown to me that you're worthy on entering Mor Ul Rek, JalYt."));
            return;
        }
        player.addAttribute("infernoVar", 1);
        player.getDialogueManager().start(new ItemChat(player, item, "You hold out your fire cape and show it to TzHaar-Ket. TzHaar-Ket nods, and allows you to pass through."));
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ItemId.FIRE_CAPE, ItemId.FIRE_MAX_CAPE_21186, ItemId.FIRE_MAX_CAPE, ItemId.FIRE_MAX_CAPE_BROKEN };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LOOSE_RAILING_2186, ObjectId.STAIRS_2187 };
    }
}
