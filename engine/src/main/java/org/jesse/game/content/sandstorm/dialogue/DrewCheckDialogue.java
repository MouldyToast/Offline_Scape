package org.jesse.game.content.sandstorm.dialogue;

import org.jesse.game.content.sandstorm.Grinder;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jetbrains.annotations.NotNull;

/**
 * @author Chris
 * @since August 20 2020
 */
public class DrewCheckDialogue extends Dialogue {
    public DrewCheckDialogue(@NotNull final Player player, @NotNull final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        final int currentBucketsHeld = player.getNumericAttribute(DrewDepositDialogue.DREW_BUCKET_AMOUNT_KEY).intValue();
        final int currentBucketsOfSand = player.getNumericAttribute(Grinder.BUCKET_OF_SAND_AMOUNT_KEY).intValue();
        if (currentBucketsHeld <= 0 && currentBucketsOfSand <= 0) {
            npc("I got nothing for ya.");
        } else {
            final String bucketOfSandAmountString = currentBucketsOfSand + " bucket" + Utils.plural(currentBucketsOfSand);
            npc("I have " + currentBucketsHeld + " of your buckets and you've ground enough sandstone for " + bucketOfSandAmountString + " of sand.");
            npc("Would ya like to purchase some buckets of sand?");
            options(TITLE, new DialogueOption("Yes", () -> player.getDialogueManager().start(new DrewClaimDialogue(player, npc))), new DialogueOption("No"));
        }
    }
}
