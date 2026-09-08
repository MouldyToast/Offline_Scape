package org.jesse.plugins.renewednpc;

import org.jesse.game.item.Item;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.plugins.dialogue.KylieMinnowD;

/**
 * @author Kris | 26/11/2018 17:33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KylieMinnow extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new KylieMinnowD(player, npc.getId(), false)));
        bind("Trade", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                if (player.getInventory().getAmountOf(21356) < 40) {
                    npc("I can give you a shark for every 40 minnows that you give me.");
                    return;
                }
                npc("I can give you a shark for every 40 minnows that you give me. How many sharks would you like?").executeAction(() -> {
                    player.getDialogueManager().finish();
                    player.sendInputInt("How many sharks would you like? (0-" + (player.getInventory().getAmountOf(21356) / 40) + ")", value -> {
                        final int amount = Math.min(value, (player.getInventory().getAmountOf(21356) / 40));
                        player.getInventory().ifDeleteItem(new Item(21356, amount * 40), () -> player.getInventory().addOrDrop(new Item(384, amount)));
                    });
                });
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.KYLIE_MINNOW, NpcId.KYLIE_MINNOW_7728, 7735 };
    }
}
