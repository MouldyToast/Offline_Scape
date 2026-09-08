package org.jesse.game.content.kebos.konar.plugins.objects;

import org.jesse.game.content.kebos.konar.actions.ModifyFerociousGloves;
import org.jesse.game.content.skills.smithing.Smithing;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.util.Colour;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 24/10/2019 | 23:52
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class LithkrenVaultMachinery implements ObjectAction, ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (item.getId() == ItemId.HYDRA_LEATHER) {
            create(player, object);
        } else {
            unassemble(player, object);
        }
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Investigate")) {
            create(player, object);
        }
    }

    private static void create(final Player player, final WorldObject object) {
        if (!player.getInventory().containsItem(ItemId.HYDRA_LEATHER, 1)) {
            player.getDialogueManager().start(new PlainChat(player, "The machine seems to have a depression and various mechanisms. It<br><br>looks dormant."));
            return;
        }
        if (!player.getInventory().containsItem(Smithing.HAMMER)) {
            player.sendMessage("You need a hammer to make things work.");
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                options("Are you sure you want to make the Ferocious Gloves?<br>" + Colour.RED.wrap("(This will make them untradeable)"), "Yes", "No").onOptionOne(() -> player.getActionManager().setAction(new ModifyFerociousGloves(object, true)));
            }
        });
    }

    private static final void unassemble(@NotNull final Player player, final WorldObject object) {
        if (!player.getInventory().containsItem(ItemId.FEROCIOUS_GLOVES, 1)) {
            player.getDialogueManager().start(new PlainChat(player, "The machine seems to have a depression and various mechanisms. It<br><br>looks dormant."));
            return;
        }
        if (!player.getInventory().containsItem(Smithing.HAMMER)) {
            player.sendMessage("You need a hammer to make things work.");
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                options("Are you sure you want to revert the gloves into leather?<br>" + Colour.RED.wrap("(This will make them tradeable)"), "Yes", "No").onOptionOne(() -> player.getActionManager().setAction(new ModifyFerociousGloves(object, false)));
            }
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ItemId.HYDRA_LEATHER, ItemId.FEROCIOUS_GLOVES };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 34628 };
    }
}
