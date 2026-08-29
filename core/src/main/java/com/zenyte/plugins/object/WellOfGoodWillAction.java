package com.zenyte.plugins.object;

import com.zenyte.game.content.DonatorPin;
import com.zenyte.game.content.well.WellConstants;
import com.zenyte.game.content.well.WellDialogue;
import com.zenyte.game.content.well.WellDialoguePin;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;

public class WellOfGoodWillAction implements ObjectAction, ItemOnObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
//        if(option.equalsIgnoreCase("Top-contributers")) {
//            player.getDialogueManager().start(new PlainChat(player, WellHandler.get().getTopContributers(WellPerk.DOUBLE_UNIQUES)));
//            return;
//        }
        if(option.equalsIgnoreCase("examine closely")) {
            boolean pass = (boolean) player.getAttributes().getOrDefault("secret-well-adventure-dont-ask", false);
            if(pass) {
                transportPlayer(player);
                return;
            }
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    player("This looks different, I think I can see a<br>staircase further down inside of the well.", Expression.AFFLICTED);
                    options("Be a bold adventurer?", new DialogueOption("Yes", () -> {
                        transportPlayer(player);
                    }) , new DialogueOption("Yes, and don't ask again", () -> {
                        player.getAttributes().put("secret-well-adventure-dont-ask", true);
                        transportPlayer(player);
                    }), new DialogueOption("Hell no.", this::finish));
                }
            });
            return;
        }
        if(WellConstants.WELL_DISABLED) {
            player.sendMessage("The Well is currently disabled as the economy balances during launch");
            return;
        }
        player.getDialogueManager().start(new WellDialogue(player));
    }

    static void transportPlayer(Player player) {
        player.lock(3);
        player.setAnimation(Animation.JUMP);
        WorldTasksManager.schedule(() -> player.setLocation(new Location(1652, 11679)), 1);
        new FadeScreen(player).fade(3, true);
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if(WellConstants.WELL_DISABLED) {
            player.sendMessage("The Well is currently disabled as the economy balances during launch");
            return;
        }
        DonatorPin pin = DonatorPin.forId(item.getId());
        if(pin == null)
            return;
        player.getDialogueManager().start(new WellDialoguePin(player, pin));
    }

    @Override
    public Object[] getItems() {
        final ArrayList<Object> list = new ArrayList<>(DonatorPin.VALUES.length);
        for (final DonatorPin pin : DonatorPin.VALUES) {
            list.add(pin.getItemId());
        }
        return list.toArray(new Object[0]);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {WellConstants.WELL_OBJ_ID};
    }
}
