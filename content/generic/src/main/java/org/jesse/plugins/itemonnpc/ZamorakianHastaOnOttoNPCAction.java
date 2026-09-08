package org.jesse.plugins.itemonnpc;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.plugins.dialogue.ItemChat;

/**
 * @author Tommeh | 19/07/2019 | 19:02
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class ZamorakianHastaOnOttoNPCAction implements ItemOnNPCAction {

    private static final Item ZAMORAKIAN_SPEAR = new Item(11824);

    private static final Item ZAMORAKIAN_HASTA = new Item(11889);

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Yes, I can convert your Zamorakian hasta back into a spear.");
                options("Do you wish to convert your hasta?", "Yes", "No").onOptionOne(() -> {
                    setKey(5);
                    new FadeScreen(player, () -> {
                        player.getInventory().deleteItem(ZAMORAKIAN_HASTA);
                        player.getInventory().addItem(ZAMORAKIAN_SPEAR);
                        WorldTasksManager.schedule(() -> player.getDialogueManager().start(new ItemChat(player, ZAMORAKIAN_SPEAR, "Otto successfully transforms your Zamorakian hasta into a spear.")));
                    }).fade(2);
                });
                plain(5, "Otto sets to work...", false);
            }
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ZAMORAKIAN_HASTA.getId() };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 2914 };
    }
}
