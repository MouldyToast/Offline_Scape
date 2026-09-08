package org.jesse.game.content.tombsofamascut.npc;

import org.jesse.game.content.tombsofamascut.encounter.RewardEncounter;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.plugins.dialogue.OptionDialogue;

import static org.jesse.game.npc.ids.NpcId.OSMUMTEN_11693;

/**
 * @author Savions
 */
public class RewardSpiritAction extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            final OptionDialogue dialogue = new OptionDialogue(player, "Are you ready to leave the Tombs of Amascut?", new String[] {"Yes.", "No."},
                    new Runnable[] {() -> player.getTOAManager().leaveTombs("You leave the Tombs of Amascut."), null});
            player.getDialogueManager().start(dialogue);
        });
        bind("Leave", (player, npc) -> {
            player.getTOAManager().leaveTombs("You leave the Tombs of Amascut.");
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {OSMUMTEN_11693};
    }
}
