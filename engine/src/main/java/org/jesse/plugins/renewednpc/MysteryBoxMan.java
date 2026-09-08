package org.jesse.plugins.renewednpc;

import org.jesse.game.item.ids.ItemId;
import org.jesse.ContentConstants;
import org.jesse.game.item.Item;
import org.jesse.game.util.Colour;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.LogLevel;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Andys1814
 */
public final class MysteryBoxMan extends NPCPlugin {

    private static final int MYSTERIOUS_OLD_MAN = 6742;

    public static boolean enabled = true;

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, MYSTERIOUS_OLD_MAN) {
                @Override
                public void buildDialogue() {
                    player("What are you doing here?");
                    npc("Thanks for asking! I was brought to " + ContentConstants.SERVER_NAME + " to thank everyone for their patience and participation in making this launch so memorable and fun for us all.");

                    boolean claimed = (boolean) player.getAttributes().getOrDefault("claimed osnr box", false);

                    final int seconds = (int) (player.getVariables().getPlayTime() * 0.6);
                    final int days = seconds / 86400;
                    final int hours = (seconds / 3600) - (days * 24);

                    if (enabled && (days >= 1 || hours >= 2) && !claimed && player.getSkills().getTotalLevel() > 750) {
                        npc("While I have you... Please accept this gift from our management and development teams as a token of our appreciation for your patience.");
                        item(new Item(ItemId.OSNR_MYSTERY_BOX, 1), "The Mysterious Old Man gives you an OSNR Mystery Box.").executeAction(() -> {
                            player.getInventory().addOrDrop(new Item(ItemId.OSNR_MYSTERY_BOX, 1));
                            player.getAttributes().put("claimed osnr box", true);
                            player.sendMessage(Colour.RS_GREEN + "Thank you for a great launch and goodluck with your mystery box!");
                            player.log(LogLevel.INFO, "Claimed mystery box for \"" + player.getUsername() +"\"");
                        });
                    }
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { MYSTERIOUS_OLD_MAN };
    }

}
