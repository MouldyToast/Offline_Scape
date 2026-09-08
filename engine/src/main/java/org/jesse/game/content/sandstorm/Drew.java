package org.jesse.game.content.sandstorm;

import org.jesse.game.content.sandstorm.dialogue.DrewCheckDialogue;
import org.jesse.game.content.sandstorm.dialogue.DrewClaimDialogue;
import org.jesse.game.content.sandstorm.dialogue.DrewDepositDialogue;
import org.jesse.game.content.sandstorm.dialogue.DrewGreetingDialogue;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;

/**
 * @author Chris
 * @since August 20 2020
 */
public class Drew extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new DrewGreetingDialogue(player, npc)));
        bind("Deposit buckets", (player, npc) -> player.getDialogueManager().start(new DrewDepositDialogue(player, npc)));
        bind("Claim Sand", (player, npc) -> player.getDialogueManager().start(new DrewClaimDialogue(player, npc)));
        bind("Check", (player, npc) -> player.getDialogueManager().start(new DrewCheckDialogue(player, npc)));
    }

    @Override
    public int[] getNPCs() {
        return new int[]{NpcId.DREW};
    }
}
