package org.jesse.game.content.tog.juna;

import org.jesse.game.content.tog.TearsOfGuthixCaveArea;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Chris
 * @since September 07 2020
 */
public class Juna implements ObjectAction {
    public static final WorldObject JUNA_OBJECT = new WorldObject(3193, 10, 1, 3252, 9516, 2);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        switch (option.toLowerCase()) {
            case "talk-to":
                Dialogue greetingDialogue = player.inArea(TearsOfGuthixCaveArea.class) ? new JunaInsideGreetingDialogue(player, NpcId.JUNA)
                        : new JunaOutsideGreetingDialogue(player, NpcId.JUNA);
                player.getDialogueManager().start(greetingDialogue);
                break;
            case "story":
                player.getDialogueManager().start(new JunaEnterDialogue(player, NpcId.JUNA));
                break;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{JUNA_OBJECT.getId()};
    }
}
