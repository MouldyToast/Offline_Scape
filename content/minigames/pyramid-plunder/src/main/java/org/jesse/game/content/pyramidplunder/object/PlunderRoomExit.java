package org.jesse.game.content.pyramidplunder.object;

import org.jesse.game.GameInterface;
import org.jesse.game.content.pyramidplunder.PlunderRoom;
import org.jesse.game.content.pyramidplunder.PyramidPlunderConstants;
import org.jesse.game.content.pyramidplunder.area.PyramidPlunderArea;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 4/5/2020
 */
public class PlunderRoomExit implements ObjectAction {
    public static void leave(final Player player) {
        leave(player, true);
    }

    public static void leave(final Player player, final boolean move) {
        player.stopAll(true, false, true);
        player.getInterfaceHandler().closeInterface(GameInterface.PYRAMID_PLUNDER);
        PyramidPlunderArea.resetTimer(player);
        if (move) {
            player.setLocation(PyramidPlunderConstants.OUTSIDE_PYRAMID);
        }
        PlunderRoom.reset(player);
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Quick-leave")) {
            leave(player);
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                final PlunderRoom currentRoom = PlunderRoom.get(player.getVarManager().getBitValue(PlunderDoor.ROOM_VARBIT));
                if (!currentRoom.hasNext()) {
                    plain("Do you really want to leave the tomb?");
                }
                options("Leave the Tomb?", new DialogueOption("Yes, I\'m out of here.", () -> leave(player)), new DialogueOption("Ah, I think I\'ll stay a little longer here."));
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ObjectId.TOMB_DOOR};
    }
}
