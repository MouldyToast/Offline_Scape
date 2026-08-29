package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.scar_essence_mine.npc.HagusDialogue;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.RequestResult;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import static com.near_reality.game.world.entity.player.PlayerAttributesKt.getTotalWrathToHagus;
import static com.zenyte.game.item.ItemId.BLOOD_RUNE;
import static com.zenyte.game.item.ItemId.WRATH_RUNE;

/**
 * @author Zei | Glabay-Studios
 * @project server-production
 * @social Discord: Z
 * @social Github: <a href="https://github.com/Zeighe">Zeighe</a>
 * @since 1/12/2025
 */
public class Hagus extends NPCPlugin implements ItemOnNPCAction {

    private final Integer MAX_WRATH_RUNES = 1_000;

    @Override
    public void handle() {
        bind("Talk-to", (Player player, NPC npc) -> {
            if (TreasureTrail.talk(player, npc)) return;

            player.getDialogueManager().start(new HagusDialogue(player, npc));
        });
    }

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        switch (item.getId()) {
            case WRATH_RUNE -> handleWrathRune(player, npc);
            case BLOOD_RUNE -> handleBloodRune(player, npc);
            default -> handleDefaultItem(player, npc);
        }
    }

    @Override
    public Object[] getItems() {
        return new Object[] {WRATH_RUNE, BLOOD_RUNE};
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {NpcId.HAGUS};
    }

    private void handleWrathRune(Player player, NPC npc) {
        int wrathRuneCount = player.getInventory().getAmountOf(WRATH_RUNE); // 10K
        var currentWrathRunes = getTotalWrathToHagus(player); // 50
        var amountToAdd = Math.min(MAX_WRATH_RUNES, wrathRuneCount) - currentWrathRunes;
        if (player.getInventory().deleteItem(WRATH_RUNE, amountToAdd).getResult() == RequestResult.SUCCESS) {
            if (currentWrathRunes == 0) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("That's the one! Where did you find this?");
                        player("Wrath runes? There are several sources, really. You can-");
                        npc("They exist! I knew it!");
                        npc("Now, tell me, where does it come from? Where's the altar?");
                        npc("I must have one. To study it. Can I have it?");
                    }
                });
            }
            else
                handleDialogueForRuneCount(player, npc, getTotalWrathToHagus(player));
            player.getAttributes().put("totalWrathToHagus", Math.min(MAX_WRATH_RUNES, currentWrathRunes + amountToAdd));
        }
    }

    private void handleDialogueForRuneCount(Player player, NPC npc, int updateTotalWrathsInHagus) {
        if (updateTotalWrathsInHagus <= 9) {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("Another wrath rune! Can I have it?");
                }
            });
        }
        else if (updateTotalWrathsInHagus <= 49) {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("Yes! More. More wrath runes for my collection. Can I have it?");
                }
            });
        }
        else if (updateTotalWrathsInHagus <= 99) {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("Another wrath rune? For me? Don't mind if I do!");
                }
            });
        }
        else if (updateTotalWrathsInHagus <= 199) {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("The power of these runes... It's drawing me in... And I love it! May I have it?");
                }
            });
        }
        else if (updateTotalWrathsInHagus <= 499) {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("For me? You shouldn't have.");
                }
            });
        }
        else if (updateTotalWrathsInHagus <= 999) {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("You know what? I don't need more wrath runes... Ah who am I kidding.");
                }
            });
        }
        else {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("I can't hold more wrath runes.");
                    npc("I must thank you, friend. You've been a strong supporter of the cause.");
                    npc("I can now return and take my rightful place.");
                }
            });
        }
    }

    private void handleBloodRune(Player player, NPC npc) {
        player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("Shaz- Oh, what's this? Ah, yes. A powerful rune indeed.");
            }
        });
    }

    private void handleDefaultItem(Player player, NPC npc) {
        player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("I'm not sure what you want me to do with that.");
            }
        });
    }


    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.HAGUS };
    }
}
