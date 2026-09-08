package org.jesse.plugins.object;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.ItemChat;

/**
 * @author Kris | 27/04/2019 01:36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ShiloVillageCart implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Board")) {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    options("Travel to the north of the island?", new DialogueOption("Yes. (200 gp)", () -> {
                        player.getDialogueManager().finish();
                        travel(player);
                    }), new DialogueOption("Nevermind."));
                }
            });
        } else if (option.equals("Pay-fare")) {
            travel(player);
        }
    }

    private static final void travel(final Player player) {
        if (!player.getInventory().containsItem(new Item(995, 200))) {
            player.getDialogueManager().start(new ItemChat(player, new Item(995, 200), "You need at least 200 gold to use the cart."));
            return;
        }
        player.getInventory().deleteItem(new Item(995, 200));
        new FadeScreen(player, () -> player.setLocation(new Location(2777, 3209, 0))).fade(3);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TRAVEL_CART_2265 };
    }
}
