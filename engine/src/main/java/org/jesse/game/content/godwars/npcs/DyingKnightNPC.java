package org.jesse.game.content.godwars.npcs;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * The Knight (Sir Gerry) found dying in the snow above the God Wars Dungeon
 * entrance. Part of the Temple Knights / Recruitment Drive quest chain.
 * Gives the player a scroll to deliver to Sir Tiffy Cashien in Falador Park.
 *
 * <p>NPC 5930 is a multinpc on varbit 3970: value 0 → 5929 (talkable),
 * value 1 → comatose (no ops). Since all quests are set to completion at
 * login, Recruitment Drive (varbit 657) is already finished.
 */
public class DyingKnightNPC extends NPCPlugin {

    /** The scroll Sir Gerry hands to the player. */
    private static final int SCROLL = ItemId.SCROLL_10512;

    /** Varbit controlling the 5930 multinpc (0 = talkable 5929, 1 = comatose). */
    private static final int KNIGHT_STATE_VARBIT = 3970;

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (player.getInventory().containsItem(SCROLL, 1)
                    || player.getBank().containsItem(new Item(SCROLL, 1))) {
                // Already has the scroll — knight is too weak to talk further.
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        plain("He's still alive, but in no condition to talk."
                                + " He appears to be almost comatose.");
                    }
                });
                return;
            }

            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    player("Who are you? What are you doing here in the snow?");
                    npc("Knight", "My name is...Sir Gerry. I am...a member of a"
                            + " secret...society of knights. My time is short and"
                            + " I need...your help.");
                    // Recruitment Drive is always completed (QuestManager.unlock
                    // sets all quest varbits to their finish stage at login).
                    player("A secret society of knights? You don't mean the"
                            + " Temple Knights, do you?");
                    npc("Knight", "Yes! Praise Saradomin! You...have been sent"
                            + " in...my hour of need. Please, take...this scroll"
                            + " to Sir Tiffy in Falador park... You should"
                            + " not...read it.").executeAction(() -> {
                        player.getInventory().addOrDrop(new Item(SCROLL, 1));
                    });
                    plain("The knight hands you a scroll.");
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.KNIGHT_5929 };
    }
}