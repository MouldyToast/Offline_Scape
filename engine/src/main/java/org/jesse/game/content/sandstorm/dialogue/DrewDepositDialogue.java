package org.jesse.game.content.sandstorm.dialogue;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.Inventory;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.entity.player.dialogue.impl.NPCChat;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;

/**
 * @author Chris
 * @since August 20 2020
 */
public class DrewDepositDialogue extends Dialogue {
    public static final String DREW_BUCKET_AMOUNT_KEY = "drew bucket amount";
    private static final int MAXIMUM_BUCKET_AMOUNT = 25000;

    public DrewDepositDialogue(@NotNull final Player player, @NotNull final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        final int currentBucketHeldAmount = player.getNumericAttribute(DREW_BUCKET_AMOUNT_KEY).intValue();
        final int notedBucketId = ItemDefinitions.getOrThrow(ItemId.BUCKET).getNotedOrDefault();
        if (currentBucketHeldAmount >= MAXIMUM_BUCKET_AMOUNT) {
            npc("Sorry, I can't hold any more buckets for ya.");
        } else if (player.carryingAny(ItemId.BUCKET) || player.carryingAny(notedBucketId)) {
            player.sendInputInt("How many buckets do you wish to deposit?", depositAmount -> {
                if (depositAmount > 0) {
                    final Inventory inventory = player.getInventory();
                    final int amountOfCarriedBuckets = inventory.getAmountOf(ItemId.BUCKET) + inventory.getAmountOf(notedBucketId);
                    // First check for smallest between amount provided or amount in inventory, then get smallest between that and amount until maximum.
                    final int cappedByInventory = Math.min(amountOfCarriedBuckets, depositAmount);
                    final int cappedDepositAmount = Math.min(MAXIMUM_BUCKET_AMOUNT - currentBucketHeldAmount, cappedByInventory);
                    final int deletedUnnotedBuckets = inventory.deleteItem(ItemId.BUCKET, cappedDepositAmount).getSucceededAmount();
                    inventory.deleteItem(notedBucketId, cappedDepositAmount - deletedUnnotedBuckets);
                    final Number totalBucketHeldAmount = player.incrementNumericAttribute(DREW_BUCKET_AMOUNT_KEY, cappedDepositAmount);
                    player.getDialogueManager().start(new NPCChat(player, NpcId.DREW, npc, "I am holding onto " + totalBucketHeldAmount + " buckets for ya."));
                }
            });
        } else {
            npc("You haven't got any buckets for me to hold.");
        }
    }
}
