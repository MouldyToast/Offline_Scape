package org.jesse.plugins.renewednpc;

import org.jesse.game.GameInterface;
import org.jesse.game.content.ItemRetrievalService;
import org.jesse.game.world.entity.Location;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 25/11/2018 16:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Torfinn extends NPCPlugin {

    private static final Location RELLEKKA_LOCATION = new Location(2641, 3698, 0);

    private static final Location UNGAEL_LOCATION = new Location(2277, 4035, 0);

    private void sail(final Player player, final boolean toVorkath) {
        new FadeScreen(player, () -> player.setLocation(toVorkath ? UNGAEL_LOCATION : RELLEKKA_LOCATION)).fade(2);
        player.sendMessage("You travel to " + (toVorkath ? "Ungael..." : "Rellekka..."));
    }

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                final boolean toVorkath = player.getLocation().withinDistance(RELLEKKA_LOCATION, 20);
                npc("Good day, " + player.getPlayerInformation().getDisplayname() + ". Would you like to sail to " + (toVorkath ? "Ungael?" : "Rellekka?"));
                options("Sail to " + (toVorkath ? "Ungael?" : "Rellekka?"), "Yes, take me there.", "No, I'd rather not.").onOptionOne(() -> sail(player, toVorkath)).onOptionTwo(this::finish);
            }
        }));
        bind("Travel", (player, npc) -> sail(player, player.getLocation().withinDistance(RELLEKKA_LOCATION, 20)));
        bind("Collect", (player, npc) -> {
            if (player.getRetrievalService().getType() != ItemRetrievalService.RetrievalServiceType.TORFINN) {
                player.getDialogueManager().start(new Dialogue(player, npc) {

                    @Override
                    public void buildDialogue() {
                        npc("You have no items waiting with me.");
                    }
                });
                return;
            }
            GameInterface.ITEM_RETRIEVAL_SERVICE.open(player);
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.TORFINN, 8133, 8132, 7504, 10403, 10404, NpcId.TORFINN_10406 };
    }
}
