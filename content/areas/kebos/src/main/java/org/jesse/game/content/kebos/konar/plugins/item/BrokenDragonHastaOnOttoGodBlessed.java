package org.jesse.game.content.kebos.konar.plugins.item;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.plugins.dialogue.ItemChat;

/**
 * @author Tommeh | 03/11/2019 | 12:55
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class BrokenDragonHastaOnOttoGodBlessed implements ItemOnNPCAction {

    private static final Item dragonHasta = new Item(ItemId.DRAGON_HASTA);

    private static final Item brokenDragonHasta = new Item(ItemId.BROKEN_DRAGON_HASTA);

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Yes, I can repair that Broken dragon hasta!");
                options("Do you wish to repair it?", "Yes", "No").onOptionOne(() -> {
                    setKey(5);
                    new FadeScreen(player, () -> {
                        player.getInventory().ifDeleteItem(brokenDragonHasta, () -> {
                            player.getInventory().addItem(dragonHasta);
                            WorldTasksManager.schedule(() -> player.getDialogueManager().start(new ItemChat(player, dragonHasta, "Otto successfully repaired your Broken dragon hasta.")));
                        });
                    }).fade(2);
                });
                plain(5, "Otto sets to work...", false);
            }
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ItemId.BROKEN_DRAGON_HASTA };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 2914 };
    }
}
