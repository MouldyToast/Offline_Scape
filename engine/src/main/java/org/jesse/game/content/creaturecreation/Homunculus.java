package org.jesse.game.content.creaturecreation;

import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;

/**
 * @author Chris
 * @since August 22 2020
 */
public class Homunculus extends NPCPlugin {


    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new HomunculusDialogue(player, npc)));
    }

    @Override
    public int[] getNPCs() {
        return new int[]{NpcId.HOMUNCULUS};
    }
}
