package org.jesse.plugins.itemonnpc;

import org.jesse.game.content.follower.Follower;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.ItemChat;

import java.util.Objects;

/**
 * @author Kris | 24/04/2019 16:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MetamorphicDustSprinkling implements ItemOnNPCAction {

    private static final Graphics graphics = new Graphics(1249);

    @Override
    public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
        if (!(npc instanceof Follower) || !Objects.equals(((Follower) npc).getOwner(), player)) {
            player.sendMessage("This is not your pet.");
            return;
        }
        if (player.getNumericAttribute("Metamorphic dust sprinkle").intValue() > 0) {
            player.sendMessage("You've already sprinkled the dust over your Olmlet. There's no need to do that any more.");
            return;
        }
        player.getDialogueManager().start(new ItemChat(player, item, "You sprinkle the dust over Olmlet.<br>" + "Congratulations! You may now metamorphise them."));
        player.addAttribute("Metamorphic dust sprinkle", 1);
        player.getInventory().deleteItem(item);
        npc.setGraphics(graphics);
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 22386 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 7520 };
    }
}
