package org.jesse.game.content.treasuretrails.plugins;

import org.jesse.game.content.treasuretrails.TreasureTrail;
import org.jesse.game.content.treasuretrails.npcs.UriNPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 09/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class UriPlugin extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (!(npc instanceof UriNPC)) {
                return;
            }
            if (!TreasureTrail.speakWithUri(player, (UriNPC) npc)) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("I do not believe we have any business, Comrade.");
                    }
                });
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.URI };
    }
}
