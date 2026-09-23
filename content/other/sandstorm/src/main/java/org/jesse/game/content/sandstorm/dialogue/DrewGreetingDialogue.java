package org.jesse.game.content.sandstorm.dialogue;

import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jetbrains.annotations.NotNull;

/**
 * @author Chris
 * @since August 20 2020
 */
public class DrewGreetingDialogue extends Dialogue {
    public DrewGreetingDialogue(@NotNull final Player player, @NotNull final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        npc("Hey there, what can I do for ya?").executeAction(() -> player.getDialogueManager().start(new DrewOptionDialogue(player, npc)));
    }
}
