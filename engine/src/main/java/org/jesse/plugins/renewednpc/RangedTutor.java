package org.jesse.plugins.renewednpc;

import org.jesse.game.GameInterface;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.util.Utils;

public class RangedTutor extends NPCPlugin {

    private static final int CAPE_COST = 99000;
    private static final long CLAIM_COOLDOWN_MS = 30 * 60 * 1000L;
    static final String CLAIM_ATTR = "combat_tutor_claim_time";

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            final boolean has99Range = player.getSkills().getLevelForXp(SkillConstants.RANGED) >= 99;
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    if (has99Range) {
                        npc("Ah, but I can see you already have amazing skill with a bow. Perhaps you have come to me to purchase a Skillcape of Ranging, and thus join the elite few who have mastered this skill?");
                        options(TITLE,
                                "Yes, please sell me a Skillcape of Ranging.",
                                "How can I train my Ranged?",
                                "How do I create a bow and arrows?",
                                "Can you toggle my ammo to equip when I pick it up please?",
                                "Goodbye."
                        ).onOptionOne(() -> setKey(10))
                                .onOptionTwo(() -> setKey(100))
                                .onOptionThree(() -> setKey(200))
                                .onOptionFour(() -> setKey(300))
                                .onOptionFive(() -> setKey(400));

                        // Skillcape purchase
                        player(10, "May I buy a Skillcape of Ranging, please?");
                        npc("Such things do not come cheaply - in fact they cost 99000 coins, to be precise!");
                        options(TITLE,
                                "I think I have the money right here, actually.",
                                "99000 coins? That's much too expensive."
                        ).onOptionOne(() -> {
                            if (!player.getInventory().checkSpace(2)) {
                                setKey(16);
                            } else if (!player.getInventory().containsItem(995, CAPE_COST)) {
                                setKey(14);
                            } else {
                                player.getInventory().deleteItem(995, CAPE_COST);
                                player.getInventory().addItem(new Item(ItemId.RANGING_CAPE));
                                player.getInventory().addItem(new Item(ItemId.RANGING_HOOD));
                                setKey(18);
                            }
                        }).onOptionTwo(() -> setKey(12));

                        player(12, "99000 coins? That's much too expensive.");
                        npc("Not at all; there are many other adventurers who would love the opportunity to purchase such a prestigious item! You can find me here if you change your mind.");
                        player(14, "I think I have the money right here, actually.").executeAction(() -> setKey(15));
                        player(15, "But, unfortunately, I was mistaken.");
                        npc("Well, come back and see me when you do.");
                        npc(16, "Unfortunately all Skillcapes are only available with a free hood, it's part of a skill promotion deal; buy one get one free, you know. So you'll need to free up some inventory space before I can sell you one.");
                        npc(18, "Excellent! Wear that cape with pride my friend.");
                    } else {
                        npc("Hey there adventurer, I am the Ranged combat tutor. Is there anything you would like to know?");
                        options(TITLE,
                                "How can I train my Ranged?",
                                "How do I create a bow and arrows?",
                                "Can you toggle my ammo to equip when I pick it up please?",
                                "Tell me about skillcapes.",
                                "Goodbye."
                        ).onOptionOne(() -> setKey(100))
                                .onOptionTwo(() -> setKey(200))
                                .onOptionThree(() -> setKey(300))
                                .onOptionFour(() -> setKey(350))
                                .onOptionFive(() -> setKey(400));
                    }

                    // === Ranged training (key 100) ===
                    player(100, "How can I train my Ranged?");
                    if (has99Range) {
                        npc("My word, I think you should be the one giving me advice instead, but I will do my best.");
                    }
                    npc("To start with you'll need a bow and arrows, you were given a Shortbow and some arrows when you arrived here from Tutorial Island.");
                    npc("Alternatively, you can claim a training bow and some arrows from me.");
                    npc("Mikasi, the Magic Combat tutor and I both give out items every 30 minutes, however you must choose whether you want runes or ranged equipment.");
                    npc("To claim the Training bow and arrows, right-click on me and choose Claim, to claim runes right-click on the Magic Combat tutor and select Claim.").executeAction(() ->
                            GameInterface.SKILLS_TAB.open(player));
                    npc("Not all bows can use every type of arrow, most bows have a limit. You can find out your bows limit by checking the Ranged skill guide.");
                    npc("If you do decide to use the Training bow, you will only be able to use the Training arrows with it. Remember to pick up your arrows, re-use them and come back when you need more.");
                    npc("Once you have your bow and arrows, equip them by selecting their Wield option in your inventory.").executeAction(() ->
                            GameInterface.COMBAT_TAB.open(player));
                    npc("You can change the way you attack by going to the combat options tab. There are three attack styles for bows. Those styles are Accurate, Rapid and Longrange.");
                    npc("Accurate increases your bows attack accuracy. Rapid will increase your attack speed with the bow. Longrange will let you attack your enemies from a greater distance.");

                    // === Fletching (key 200) ===
                    player(200, "How do I create a bow and arrows?");
                    npc("Ahh the art of fletching. Fletching is used to create your own bow and arrows.");
                    npc("It's quite simple really. You'll need an axe to cut some logs from trees and a knife. Knives can be found in and around the Lumbridge castle and in the Varrock General store upstairs.");
                    npc("Use your knife on the logs. This will bring up a menu listing items you can fletch.");
                    npc("For arrows you will need to smith some arrow heads and kill some chickens for feathers.");
                    npc("Add the feathers to your Arrow shafts to make Headless arrows, then use your chosen arrow heads on the Headless arrows to make your arrows.");
                    npc("Now for making bows. When accessing the fletching menu, instead of choosing Arrow shafts, you can make an unstrung bow instead.");
                    npc("To complete the bow you will need to get your hands on a Bow string.");
                    npc("First you will need to get some flax from a flax field. There's one south of Seers' Village. Gather flax, then spin it on a spinning wheel, there's one in Seers' Village too.");
                    npc("This makes bow strings which you can then use on the unstrung bows to make a working bow!");
                    player("Brilliant. If I forget anything I'll come talk to you again.");

                    // === Ammo toggle (key 300) ===
                    player(300, "Can you toggle my ammo to equip when I pick it up please?");
                    npc("Certainly. If you pick up ammo that is the same ammo you are currently using, I can make that ammo automatically equip for you, providing you have space of course!");
                    npc("Or you can leave it to going into your inventory like normal.");
                    npc("How do you want to handle picking up ammo?");
                    options(TITLE,
                            "Automatically equip it.",
                            "Place it in my inventory."
                    ).onOptionOne(() -> {
                        player.addAttribute("equip ammunition picked up", 1);
                        setKey(310);
                    }).onOptionTwo(() -> {
                        player.addAttribute("equip ammunition picked up", 0);
                        setKey(320);
                    });
                    npc(310, "There you go! Your ammo will now automatically be equipped when you pick it up.");
                    npc(320, "There you go! Your ammo will now go to your inventory when you pick it up.");

                    // === Skillcapes info (key 350, non-99 path) ===
                    player(350, "Tell me about skillcapes.");
                    npc("Of course. Skillcapes are a symbol of achievement. Only people who have mastered a skill and reached level 99 can get their hands on them and gain the benefits they carry.");

                    // === Goodbye (key 400) ===
                    player(400, "Goodbye.");
                }
            });
        });

        bind("Claim", (player, npc) -> claimItems(player, npc));
    }

    private void claimItems(Player player, NPC npc) {
        long currentTime = Utils.currentTimeMillis();
        long lastClaim = player.getNumericAttribute(CLAIM_ATTR).longValue();
        if (lastClaim > currentTime) {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("I work with the Magic tutor to give out consumable items that you may need for combat such as arrows and runes. However we have had some cheeky people try to take both!");
                    npc("So, every half an hour, you may come back and claim either arrows OR runes, but not both. Come back in a while for arrows, or simply buy some.");
                }
            });
            return;
        }

        boolean hasBow = player.containsItem(ItemId.TRAINING_BOW);
        boolean hasArrows = player.containsItem(ItemId.TRAINING_ARROWS);

        if (hasBow && hasArrows) {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("You already have a training bow and training arrows. Save some for the other adventurers.");
                }
            });
            return;
        }

        if (!hasBow && !hasArrows) {
            if (player.getInventory().getFreeSlots() < 2) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("If you had any room in your pack I'd give you a training bow. That's a shame. Come back when you do. If you had enough space in your inventory I'd give you some training arrows, come back when you do.");
                    }
                });
                return;
            }
            player.getInventory().addItem(new Item(ItemId.TRAINING_BOW));
            player.getInventory().addItem(new Item(ItemId.TRAINING_ARROWS, 25));
            player.addAttribute(CLAIM_ATTR, currentTime + CLAIM_COOLDOWN_MS);
            player.sendMessage("Nemarti gives you a Training shortbow and 25 arrows. They can only be used together.");
        } else if (hasBow) {
            if (player.getInventory().getFreeSlots() < 1) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("If you had enough space in your inventory I'd give you some training arrows, come back when you do.");
                    }
                });
                return;
            }
            player.getInventory().addItem(new Item(ItemId.TRAINING_ARROWS, 25));
            player.addAttribute(CLAIM_ATTR, currentTime + CLAIM_COOLDOWN_MS);
            player.sendMessage("You already have a training bow.");
            player.sendMessage("Nemarti gives you 25 training arrows. They can only be used with the Training shortbow.");
        } else {
            if (player.getInventory().getFreeSlots() < 1) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("If you had any room in your pack I'd give you a training bow. That's a shame. Come back when you do.");
                    }
                });
                return;
            }
            player.getInventory().addItem(new Item(ItemId.TRAINING_BOW));
            player.addAttribute(CLAIM_ATTR, currentTime + CLAIM_COOLDOWN_MS);
            player.sendMessage("You already have some training arrows.");
            player.sendMessage("Nemarti gives you a Training shortbow. It can only be used with Training arrows.");
        }
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.RANGED_COMBAT_TUTOR };
    }
}