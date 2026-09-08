package org.jesse.plugins.itemonitem;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.PairedItemOnItemPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.plugins.dialogue.ItemChat;

/**
 * @author Kris | 26/04/2019 16:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class StaffOfLightCreation implements PairedItemOnItemPlugin {
    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        final Item light = from.getId() == 13256 ? from : to;
        final Item staff = from == light ? to : from;
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                doubleItem(staff, light, "Are you sure you wish to use the Saradomin's light on the staff? This " +
                        "irreversible process consumes both the light as well as the staff, upgrading both to a staff" +
                        " of light.");
                options("Update to staff of light?", new DialogueOption("Yes.", () -> {
                    player.getInventory().deleteItemsIfContains(new Item[] {light, staff}, () -> {
                        player.getInventory().addOrDrop(new Item(22296));
                        player.getDialogueManager().start(new ItemChat(player, new Item(22296), "You combine the light with the staff of the dead to create a staff of light."));
                    });
                }), new DialogueOption("No."));
            }
        });
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(13256, 11791)};
    }
}
