package org.jesse.plugins.renewednpc;

import org.jesse.game.GameInterface;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

public class MeleeCombatTutor extends NPCPlugin {

    private static final int CAPE_COST = 99000;

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            final boolean has99Def = player.getSkills().getLevelForXp(SkillConstants.DEFENCE) >= 99;
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    if (has99Def) {
                        npc("Ah, but I can see you're already a master in the fine art of Defence. Perhaps you have come to me to purchase a Skillcape of Defence, and thus join the elite few who have mastered this exacting skill?");
                        npc("In recognition of your defensive abilities, when you have it equipped it will act as ring of life, saving you from combat if your hitpoints become low.");
                        options(TITLE,
                                "Yes, please sell me a Skillcape of Defence.",
                                "Tell me about melee combat.",
                                "Tell me about different weapon types I can use.",
                                "I'd like a training sword and shield.",
                                "Goodbye."
                        ).onOptionOne(() -> setKey(10))
                                .onOptionTwo(() -> setKey(100))
                                .onOptionThree(() -> setKey(200))
                                .onOptionFour(() -> setKey(300))
                                .onOptionFive(() -> setKey(400));

                        // Skillcape purchase
                        player(10, "May I buy a Skillcape of Defence, please?");
                        npc("You wish to join the elite defenders of this world? I'm afraid such things do not come cheaply - in fact they cost 99000 coins, to be precise!");
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
                                player.getInventory().addItem(new Item(ItemId.DEFENCE_CAPE));
                                player.getInventory().addItem(new Item(ItemId.DEFENCE_HOOD));
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
                        npc("Greetings adventurer, I am the Melee combat tutor. Is there anything I can do for you?");
                        options(TITLE,
                                "Tell me about melee combat.",
                                "Tell me about different weapon types I can use.",
                                "Tell me about skillcapes.",
                                "I'd like a training sword and shield.",
                                "Goodbye."
                        ).onOptionOne(() -> setKey(100))
                                .onOptionTwo(() -> setKey(200))
                                .onOptionThree(() -> setKey(250))
                                .onOptionFour(() -> setKey(300))
                                .onOptionFive(() -> setKey(400));
                    }

                    // === Melee combat explanation (key 100) ===
                    player(100, "Tell me about melee combat.");
                    npc("Well adventurer, the first thing you will need is a sword and a shield appropriate for your level.").executeAction(() ->
                            GameInterface.EQUIPMENT_STATS.open(player));
                    npc("Make sure to equip your sword and shield. Click on them in your inventory, they will disappear from your inventory and move to your worn items. You can see your worn items in the worn items tab here.").executeAction(() ->
                            GameInterface.COMBAT_TAB.open(player));
                    npc("When you are wielding your sword you will then be able to see the correct options in the combat interface.");
                    npc("There are four different melee styles. Accurate, aggressive, defensive and controlled. Not all weapons will have all four styles though.");
                    player("Interesting, what does each style do?");
                    npc("Well I am glad you asked. The accurate style will give you experience points in your Attack skill, you will also find you will deal damage more frequently as a result of being, well, more accurate.");
                    npc("Next we have the aggressive style. This style will give you experience points in your Strength skill. When using this style you will notice that your attacks will hit a little harder.");
                    npc("Now for the defensive style, this style will give you experience points in your Defensive skill. When using this style you will notice that you get hit less often.");
                    npc("Finally, we have the controlled style. This style will give you the same amount of experience as the other styles would but shared across all three of the combat skills.");
                    npc("If you were using the training sword for example, there are four different attack types. Stab, lunge, slash and block.");
                    npc("Each type uses one of the attack styles. Stab uses accurate, lunge and slash use aggressive and block uses defensive.");
                    npc("To find out which style an attack type uses, hover your mouse cursor over the style button.");

                    // === Weapon types (key 200) ===
                    player(200, "Tell me about different weapon types I can use.");
                    npc("Well let me see now...There are stabbing type weapons such as daggers, then you have swords which are slashing, maces that have great crushing abilities, battle axes which are powerful.");
                    npc("It depends a lot on how you want to fight. Experiment and find out what is best for you. Never be scared to try out a new weapon; you never know, you might like it!");
                    npc("While I tried all of them for a while, I settled on this rather good sword.");
                    npc("You might also find that different weapon types are more accurate against different monsters.");

                    // === Skillcapes info (key 250, non-99 path) ===
                    player(250, "Tell me about skillcapes.");
                    npc("Of course. Skillcapes are a symbol of achievement. Only people who have mastered a skill and reached level 99 can get their hands on them and gain the benefits they carry.");
                    npc("The Cape of Defence will act as ring of life, saving you from combat if your hitpoints become low.");

                    // === Training sword and shield (key 300) ===
                    player(300, "I'd like a training sword and shield.").executeAction(() -> {
                        boolean hasSword = player.containsItem(ItemId.TRAINING_SWORD);
                        boolean hasShield = player.containsItem(ItemId.TRAINING_SHIELD);
                        if (hasSword && hasShield) {
                            setKey(310);
                        } else if (hasSword) {
                            setKey(320);
                        } else if (hasShield) {
                            setKey(330);
                        } else {
                            setKey(340);
                        }
                    });
                    npc(310, "You already have a training sword and shield. Save some for the other adventurers.");
                    npc(320, "There you go, use it well.").executeAction(() ->
                            player.getInventory().addOrDrop(new Item(ItemId.TRAINING_SHIELD)));
                    npc(330, "There you go, use it well.").executeAction(() ->
                            player.getInventory().addOrDrop(new Item(ItemId.TRAINING_SWORD)));
                    npc(340, "There you go, use it well.").executeAction(() -> {
                        player.getInventory().addOrDrop(new Item(ItemId.TRAINING_SWORD));
                        player.getInventory().addOrDrop(new Item(ItemId.TRAINING_SHIELD));
                    });

                    // === Goodbye (key 400) ===
                    player(400, "Goodbye.");
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.MELEE_COMBAT_TUTOR };
    }
}