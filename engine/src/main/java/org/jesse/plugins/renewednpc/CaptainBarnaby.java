package org.jesse.plugins.renewednpc;

import org.jesse.game.content.sailing.Sailing;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 26/11/2018 19:53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CaptainBarnaby extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("Right click me and select where you'd like to go.");
            }
        }));
        bind("Brimhaven", (player, npc) -> {
            String departure = player.getArea().name().equals("Rimmington") ? "Rimmington" : "Ardougne";
            travel(player, npc, departure, "Brimhaven");

        });
        bind("Rimmington", (player, npc) -> {
            String departure = player.getArea().name().equals("Brimhaven") ? "Brimhaven" : "Ardougne";
            travel(player, npc, departure, "Rimmington");

        });
        bind("Ardougne", (player, npc) -> {
            String departure = player.getArea().name().equals("Rimmington") ? "Rimmington" : "Brimhaven";
            travel(player, npc, departure, "Ardougne");
        });
    }
    private void travel(Player player, NPC npc, String departure, String destination) {
        var cost = new Item(995, 30);
        var hasGloves = player.getEquipment().containsItem(ItemId.KARAMJA_GLOVES_3);
        if (hasGloves)
            cost.setAmount(15);
        if (player.getInventory().containsItem(cost)) {
            player.sendMessage("You pay %d coins and board the ship.".formatted(cost.getAmount()));
            Sailing.sail(player, departure, destination);
        }
        else {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("You don't have enough gold on you to go on this trip.");
                }
            });
        }
    }
    @Override
    public int[] getNPCs() {
        return new int[]{NpcId.CAPTAIN_BARNABY_8764, NpcId.CAPTAIN_BARNABY_8763, NpcId.CAPTAIN_BARNABY, NpcId.CAPTAIN_BARNABY_9250};
    }
}