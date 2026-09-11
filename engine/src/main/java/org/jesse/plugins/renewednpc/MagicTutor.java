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

public class MagicTutor extends NPCPlugin {

    private static final int CAPE_COST = 99000;
    private static final long CLAIM_COOLDOWN_MS = 30 * 60 * 1000L;

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            final boolean has99Magic = player.getSkills().getLevelForXp(SkillConstants.MAGIC) >= 99;
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    if (has99Magic) {
                        npc("Ah, I can see you are already a very accomplished mage. Perhaps you have come to purchase a Skillcape of Magic?");
                        options(TITLE,
                                "Yes, please sell me a Skillcape of Magic.",
                                "Tell me about magic combat please.",
                                "How do I make runes?",
                                "Let's discuss how my runes act when I pick them up.",
                                "Goodbye."
                        ).onOptionOne(() -> setKey(10))
                                .onOptionTwo(() -> setKey(100))
                                .onOptionThree(() -> setKey(200))
                                .onOptionFour(() -> setKey(300))
                                .onOptionFive(() -> setKey(400));

                        // Skillcape purchase
                        player(10, "May I buy a Skillcape of Magic, please?");
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
                                player.getInventory().addItem(new Item(ItemId.MAGIC_CAPE));
                                player.getInventory().addItem(new Item(ItemId.MAGIC_HOOD));
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
                        npc("Hello there adventurer, I am the Magic combat tutor. Would you like to learn about magic combat, or perhaps how to make runes?");
                        options(TITLE,
                                "Tell me about magic combat please.",
                                "How do I make runes?",
                                "Let's discuss how my runes act when I pick them up.",
                                "Tell me about skillcapes.",
                                "Goodbye."
                        ).onOptionOne(() -> setKey(100))
                                .onOptionTwo(() -> setKey(200))
                                .onOptionThree(() -> setKey(300))
                                .onOptionFour(() -> setKey(350))
                                .onOptionFive(() -> setKey(400));
                    }

                    // === Magic combat (key 100) ===
                    player(100, "Tell me about magic combat please.");
                    npc("Of course " + player.getName() + "! As a rule of thumb, if you cast the highest spell of which you're capable, you'll get the best experience possible.").executeAction(() ->
                            GameInterface.SPELLBOOK.open(player));
                    npc("This is your spell book, this contains all the spells you can cast. To cast a spell, you will need to have the required magic level and have the right amount of the runes used to cast the spell.");
                    npc("To check the level and runes required to cast a spell, hover your mouse cursor over the spell.");
                    npc("If you have the right amount of runes and the magic level the spell will light up, meaning you will be able to cast it.");
                    npc("When you have a combat spell available, click on it once, then select your target. A good target would be a monster that is below your combat level.");
                    npc("Try rats in the castle or, if you're feeling brave, the goblins to the west have been causing a nuisance of themselves.");
                    npc("Wearing metal armour and ranged armour can seriously impair your magical abilities. Make sure you wear some robes to maximise your capabilities.");
                    npc("Mikasi, the Magic Combat tutor and I both give out items every 30 minutes, however you must choose whether you want runes or ranged equipment.");
                    npc("To claim runes, right-click on me and choose Claim, to claim ranged equipment right-click on the Ranged Combat tutor and select Claim.");
                    npc("Superheat Item and the Alchemy spells are good ways to level magic if you are not interested in the combat aspect of magic.");
                    npc("There's always the Magic Training Arena. You can find it north of the Emir's Arena, north-east of Al Kharid. You will be able to earn some special rewards there by practicing your magic there.");

                    // === Runecrafting (key 200) ===
                    player(200, "How do I make runes?");
                    npc("There are a couple of things you will need to make runes, rune essence and a talisman to enter the temple ruins.");
                    npc("To get rune essence you will need to gather them in the essence mine. You can get to the mine by talking to Aubury who owns the runes shop in south east Varrock.");
                    npc("You will need a talisman for the rune you would like to create. You can right-click on it and select the Locate option. This will tell you the rough location of the altar.");
                    npc("When you find the ruined altar, use the talisman on it to be transported to a temple where you can craft your runes.");
                    npc("Clicking on the temple's altar will imbue your rune essence with the altar's magical property.");
                    npc("If you want to save yourself an inventory space, you could always try binding the talisman to a tiara.");
                    npc("To make one, take a tiara and talisman to the ruins and use the tiara on the temple altar. This will bind the talisman to your tiara.");

                    // === Rune pouch toggle (key 300) ===
                    player(300, "Let's discuss how my runes act when I pick them up.");
                    npc("When you pick up runes from the ground, if you're carrying a rune pouch that contains matching runes, I can make the runes you've picked up go straight into the pouch, provided there's space.");
                    npc("Alternatively, you can let them go into your inventory like normal.");
                    npc("How do you want to handle picking up runes?");
                    options(TITLE,
                            "Automatically send to rune pouch.",
                            "Place them in my inventory."
                    ).onOptionOne(() -> {
                        player.addAttribute("put looted runes in rune pouch", 1);
                        setKey(310);
                    }).onOptionTwo(() -> {
                        player.addAttribute("put looted runes in rune pouch", 0);
                        setKey(320);
                    });
                    npc(310, "Certainly! Runes will go straight to your rune pouch when you pick them up, if it contains matching runes.");
                    npc("You can also control this option via the Settings menu, rather than having to return to this Tutor in future.");
                    npc(320, "Certainly! Runes will go to your inventory when you pick them up, rather than going into a rune pouch.");
                    npc("You can also control this option via the Settings menu, rather than having to return to this Tutor in future.");

                    // === Skillcapes info (key 350, non-99 path) ===
                    player(350, "Tell me about skillcapes.");
                    npc("Of course. Skillcapes are a symbol of achievement. Only people who have mastered a skill and reached level 99 can get their hands on them and gain the benefits they carry.");

                    // === Goodbye (key 400) ===
                    player(400, "Goodbye.");
                }
            });
        });

        bind("Claim", (player, npc) -> claimRunes(player, npc));
    }

    private void claimRunes(Player player, NPC npc) {
        long currentTime = Utils.currentTimeMillis();
        long lastClaim = player.getNumericAttribute(RangedTutor.CLAIM_ATTR).longValue();
        if (lastClaim > currentTime) {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("I work with the Ranged Combat tutor to give out consumable items that you may need for combat such as arrows and runes. However we have had some cheeky people try to take both!");
                    npc("So, every half an hour, you may come back and claim either arrows OR runes, but not both. Come back in a while for runes, or simply make your own.");
                }
            });
            return;
        }

        boolean hasMindInBank = player.getBank().containsItem(new Item(ItemId.MIND_RUNE));
        boolean hasMindInInv = player.getInventory().containsItem(ItemId.MIND_RUNE);
        boolean hasAirInBank = player.getBank().containsItem(new Item(ItemId.AIR_RUNE));
        boolean hasAirInInv = player.getInventory().containsItem(ItemId.AIR_RUNE);
        int freeSlots = player.getInventory().getFreeSlots();

        boolean gaveSomething = false;
        StringBuilder messages = new StringBuilder();

        // Mind runes
        if (hasMindInBank) {
            messages.append("You have some mind runes in your bank.\n");
        } else if (hasMindInInv) {
            messages.append("You already have some mind runes.\n");
        } else if (freeSlots < 1) {
            messages.append("If you had enough space in your inventory I'd give you some mind runes, come back when you do.\n");
        } else {
            player.getInventory().addItem(new Item(ItemId.MIND_RUNE, 30));
            freeSlots--;
            gaveSomething = true;
        }

        // Air runes
        if (hasAirInBank) {
            messages.append("You have some air runes in your bank.\n");
        } else if (hasAirInInv) {
            messages.append("You already have some air runes.\n");
        } else if (freeSlots < 1) {
            messages.append("If you had enough space in your inventory I'd give you some air runes, come back when you do.\n");
        } else {
            player.getInventory().addItem(new Item(ItemId.AIR_RUNE, 30));
            gaveSomething = true;
        }

        if (gaveSomething) {
            player.addAttribute(RangedTutor.CLAIM_ATTR, currentTime + CLAIM_COOLDOWN_MS);
            player.sendMessage("Mikasi gives you some runes.");
        }

        boolean hasRunesInBank = hasMindInBank || hasAirInBank;
        String dialogueText = messages.toString().trim();
        if (!dialogueText.isEmpty()) {
            final boolean showBankHint = hasRunesInBank;
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    String[] lines = dialogueText.split("\n");
                    for (String line : lines) {
                        npc(line);
                    }
                    if (showBankHint) {
                        plain("You have some runes in your bank. Climb the stairs in Lumbridge Castle until you see the bank icon on your minimap. There you will find a bank.");
                    }
                }
            });
        }
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.MAGIC_COMBAT_TUTOR };
    }
}