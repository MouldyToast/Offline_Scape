package org.jesse.game.content.kebos.konar.plugins.item;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnItemAction;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 24/10/2019 | 23:29
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class BootsOfBrimstoneCreation implements ItemOnItemAction {

    private static final Item bootsOfStone = new Item(ItemId.BOOTS_OF_STONE);
    private static final Item drakesClaw = new Item(ItemId.DRAKES_CLAW);
    private static final Item bootsOfBrimstone = new Item(ItemId.BOOTS_OF_BRIMSTONE);

    private static final Animation anim = new Animation(4462);
    private static final Graphics gfx = new Graphics(759);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                doubleItem(bootsOfStone, drakesClaw, "Are you sure you wish to alchemically combine the Boots of stone and Drake's claw to create Boots of brimstone? This cannot be reversed.");
                options(TITLE, "Proceed with the alchemical combination.", "Cancel.")
                        .onOptionOne(() -> {
                            player.getInventory().deleteItemsIfContains(new Item[]{bootsOfStone, drakesClaw}, () -> {
                                player.setAnimation(anim);
                                player.setGraphics(gfx);
                                player.getInventory().addItem(bootsOfBrimstone);
                            });
                            setKey(5);
                        });
                item(5, bootsOfBrimstone, "You successfully combine the Boots of stone and Drake's claw to create Boots of brimstone.");
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[]{bootsOfStone.getId(), drakesClaw.getId()};
    }
}
