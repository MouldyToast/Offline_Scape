package org.jesse.plugins.object;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.FillContainer;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;

public class FlourBinObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (name.equalsIgnoreCase("flour bin")) {
            if (option.equalsIgnoreCase("empty")) {
                if (player.getInventory().containsItem(1931, 1)) {
                    player.getActionManager().setAction(new FillContainer(object, new Item(1931)));
                } else {
                    player.getDialogueManager().start(new PlainChat(player, "You need a pot to put the flour in!"));
                }
            }
            return;
        }
        if (name.equalsIgnoreCase("hopper controls") || object.getId() == 22424) {
            if (option.equalsIgnoreCase("operate") || option.equalsIgnoreCase("pull")) {
                /**
                 * WorldTasksManager.schedule(() -> {
                 * 					player.faceObject(object);
                 * 					player.setNextAnimation(new Animation(3571));
                 * 					if(player.getBooleanAttribute("hopper")) {
                 * 						if(player.getNumericAttribute("flourbin").intValue() == 0)
                 * 							player.getVarbitManager().setVarBit(5325, 1);
                 *
                 * 						player.sendMessage("You operate the hopper. The grain slides down the chute.");
                 * 						player.toggleBooleanAttribute("hopper");
                 * 						player.addAttribute("flourbin", player.getNumericAttribute("flourbin").intValue() + 1);
                 * 					} else
                 * 						player.sendMessage("You operate the empty hopper. Nothing interesting happens.");
                 * 				});
                 */
                player.setAnimation(new Animation(3571));
                if (player.getBooleanAttribute("hopper")) {
                    if (player.getNumericAttribute("flourbin").intValue() == 0) {
                        player.getVarManager().sendBit(5325, 1);
                    }
                    player.sendMessage("You operate the hopper. The grain slides down the chute.");
                    player.toggleBooleanAttribute("hopper");
                    player.addAttribute("flourbin", player.getNumericAttribute("flourbin").intValue() + 1);
                } else {
                    player.sendMessage("You operate the empty hopper. Nothing interesting happens.");
                }
            }
            return;
        }
    }

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { "flour bin", "hopper controls", ObjectId.LEVER_22424 };
    }
}
