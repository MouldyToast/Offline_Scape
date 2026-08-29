package com.zenyte.game.content.skills.herblore.mixer;

import com.near_reality.game.content.consumables.drinks.DivinePotion;
import com.near_reality.game.item.CustomObjectId;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.consumables.Drinkable;
import com.zenyte.game.content.consumables.drinks.Potion;
import com.zenyte.game.content.zahur.Herb;
import com.zenyte.game.content.zahur.PotionResult;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.CollectionUtils;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-22
 */
public class RegentMixer implements ObjectAction {


    public static void crushSecondaries(@NotNull final Player player) {
        player.getDialogueManager().finish();
        final Inventory inventory = player.getInventory();
        final long cost = calculateSecondariesCost(player);
        final boolean free = player.getMemberRank().equalToOrGreaterThan(MemberRank.DRAGONSTONE);
        if (cost == -1) {
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                if (inventory.getAmountOf(995) < cost && !free) {
                    plain("You need " + StringFormatUtil.format(cost) + " coins if you'd like me to crush all those " + "secondaries.");
                    return;
                }
                if (cost == 0) {
                    plain("You have no secondaries with you that I'm able to crush.");
                    return;
                }
                if (!free) {
                    plain("Crushing the secondaries in your inventory will cost you " + StringFormatUtil.format(cost) + " coins.");
                }
                options("Crush the secondaries for " + (free ? "free" : StringFormatUtil.format(cost)) + "?", new DialogueOption("Yes, crush them.", () -> {
                    player.getDialogueManager().finish();
                    final long cost = calculateSecondariesCost(player);
                    if (cost == -1) {
                        return;
                    }
                    if (cost == 0) {
                        player.getDialogueManager().start(new Dialogue(player) {

                            @Override
                            public void buildDialogue() {
                                plain("You have no secondaries with you that I'm able to crush.");
                            }
                        });
                        return;
                    }
                    if (inventory.getAmountOf(995) < cost && !free) {
                        player.getDialogueManager().start(new Dialogue(player) {

                            @Override
                            public void buildDialogue() {
                                plain("You need " + StringFormatUtil.format(cost) + " coins if you'd like me to crush " + "all those secondaries.");
                            }
                        });
                        return;
                    }
                    if (!free) {
                        inventory.deleteItem(new Item(995, (int) cost));
                    }
                    for (int i = 0; i < 28; i++) {
                        final Item item = inventory.getItem(i);
                        if (item == null) {
                            continue;
                        }
                        final int id = item.getId();
                        final HerbloreSecondary secondary = CollectionUtils.findMatching(HerbloreSecondary.values, sec -> (sec.rawItem == id && sec.processedItem != -1) || (sec.rawNotedItem == id && (sec == HerbloreSecondary.LAVA_SCALE || sec.processedNotedItem != -1)));
                        if (secondary != null) {
                            final int crushedId = id == secondary.rawItem ? secondary.processedItem : secondary.processedNotedItem;
                            if (secondary == HerbloreSecondary.LAVA_SCALE) {
                                inventory.deleteItem(i, item);
                                inventory.addOrDrop(new Item(11994, Utils.random(3, 6) * item.getAmount()));
                            } else {
                                inventory.deleteItem(i, item);
                                inventory.addOrDrop(new Item(crushedId, item.getAmount()));
                            }
                        }
                    }
                    inventory.refreshAll();
                }), new DialogueOption("No, don't crush them."));
            }
        });
    }

    private static long calculateSecondariesCost(@NotNull final Player player) {
        final Inventory inventory = player.getInventory();
        int amount = 0;
        for (int i = 0; i < 28; i++) {
            final Item item = inventory.getItem(i);
            if (item == null) {
                continue;
            }
            final int id = item.getId();
            final HerbloreSecondary secondary = CollectionUtils.findMatching(HerbloreSecondary.values, sec -> (sec.rawItem == id && sec.processedItem != -1) || (sec.rawNotedItem == id && (sec == HerbloreSecondary.LAVA_SCALE || sec.processedNotedItem != -1)));
            if (secondary != null) {
                amount += item.getAmount();
                if (amount < 0) {
                    player.getDialogueManager().start(new Dialogue(player) {

                        @Override
                        public void buildDialogue() {
                            plain("You have too many items there with you, I cannot crush them all.");
                        }
                    });
                    return -1;
                }
            }
        }
        return amount * 50L;
    }

    private static void cleanHerbs(final Player player) {
        final HashMap<Herb, Item> herbs = new HashMap<>();
        final int coins = player.getInventory().getAmountOf(995);
        for (final Int2ObjectMap.Entry<Item> entry : player.getInventory().getContainer().getItems().int2ObjectEntrySet()) {
            if (entry == null) {
                continue;
            }
            final Item item = entry.getValue();
            if (!item.getName().contains("Grimy")) {
                continue;
            }
            final Herb herb = Herb.get(item.getId(), true);
            if (herb == null) {
                continue;
            }
            herbs.put(herb, item);
        }
        final int price = getPrice(player);
        int amount = price == 0 ? Integer.MAX_VALUE : coins / price;
        for (final Map.Entry<Herb, Item> entry : herbs.entrySet()) {
            if (entry == null) {
                continue;
            }
            final Herb herb = entry.getKey();
            final Item item = entry.getValue();
            final int notedCleanedHerb = herb.getClean().getDefinitions().getNotedId();
            int requestedAmount = item.getAmount();
            if (amount < requestedAmount) {
                requestedAmount = amount;
            }
            if (requestedAmount == 0) {
                player.getDialogueManager().plain("You don't have enough coins for to clean your herbs.");
                return;
            }
            if (player.getInventory().getAmountOf(notedCleanedHerb) + item.getAmount() < 0) {
                player.sendMessage("You currently have too many cleaned " + herb + "s, get rid of some and come back.");
                return;
            }
            player.getInventory().deleteItem(new Item(995, requestedAmount * price));
            player.getInventory().deleteItem(new Item(item.getId(), requestedAmount));
            player.getInventory().addItem(herb.getClean().getDefinitions().getNotedId(), requestedAmount).onFailure(i -> {
                player.sendMessage("<col=ff0000><shad=000000>Some of the herb(s) were dropped on the ground.");
                World.spawnFloorItem(i, player);
            });
            amount -= requestedAmount;
        }
        if (!herbs.isEmpty())
            player.getDialogueManager().plain("There, all done.");
        else
            player.getDialogueManager().plain("You don't seem to have any grimy herbs for me to clean right now, come back when you do.");
    }

    private static boolean decant(final Player player, final int selectedDose) {
        final HashMap<Drinkable, PotionResult> potions = new HashMap<>();
        for (final Int2ObjectMap.Entry<Item> entry : player.getInventory().getContainer().getItems().int2ObjectEntrySet()) {
            if (entry == null) {
                continue;
            }
            final Item item = entry.getValue();
            final int id = item.getDefinitions().getUnnotedOrDefault();
            Drinkable drinkable = Potion.get(id);
            if (drinkable == null) {
                drinkable = DivinePotion.Companion.getMap().get(id);
                if (drinkable == null) {
                    continue;
                }
            }
            if(drinkable == Potion.OVERLOAD)
                continue; // Overloads do not have a noted equivalent. Skip them so zahur doesnt remove them.
            if (potions.containsKey(drinkable)) {
                potions.get(drinkable).add(item);
            } else {
                final PotionResult result = new PotionResult(drinkable, item);
                potions.put(drinkable, result);
            }
        }
        if (potions.isEmpty()) {
            player.getDialogueManager().plain("You don't seem to have any potions to decant.");
            return false;
        }
        for (final Map.Entry<Drinkable, PotionResult> entry : potions.entrySet()) {
            if (entry == null) {
                continue;
            }
            final Drinkable potion = entry.getKey();
            final PotionResult result = entry.getValue();
            final int totalDose = result.getTotalDose();
            final int amount = totalDose / selectedDose;
            final int remainder = totalDose % selectedDose;
            Int2IntFunction doseFunction = dose -> {
                final int id = potion.getIds()[dose];
                return ItemDefinitions.get(id).getNotedId();
            };
            final Item decanted = new Item(doseFunction.apply(selectedDose - 1), amount);
            if (amount >= 500_000_000) {
                player.getDialogueManager().plain("I'm sorry but I can't decant that many at once.");
                return false;
            }
            if (player.getInventory().getAmountOf(decanted.getId()) + amount < 0) {
                player.getDialogueManager().plain("You have too many " + decanted.getName().toLowerCase() + "s right now, decanting would result in an overflow. Get rid of some and come back.");
                return false;
            }
            result.getItems().forEach(item -> player.getInventory().deleteItem(item));
            player.getInventory().addItem(doseFunction.apply(selectedDose - 1), amount);
            if (remainder != 0) {
                player.getInventory().addItem(doseFunction.apply(remainder - 1), 1).onFailure(item -> {
                    player.sendMessage("<col=ff0000><shad=000000>Some of the potion(s) were sent to your bank.");
                    player.getBank().add(item).onFailure(i -> {
                        player.sendMessage("<col=ff0000><shad=000000>Some of the potion(s) were dropped on the ground.");
                        World.spawnFloorItem(i, player);
                    });
                });
            }
        }
        return true;
    }

    private static void makeUnfinishedPotions(final Player player) {
        final int vialsOfWater = player.getInventory().getAmountOf(228);
        final int coins = player.getInventory().getAmountOf(995);
        final HashMap<Herb, Item> herbs = new HashMap<Herb, Item>();
        for (final Int2ObjectMap.Entry<Item> entry : player.getInventory().getContainer().getItems().int2ObjectEntrySet()) {
            if (entry == null) {
                continue;
            }
            final Item item = entry.getValue();
            final Herb herb = Herb.get(item.getId(), DiaryUtil.eligibleFor(DiaryReward.DESERT_AMULET3, player) || SkillcapePerk.HERBLORE.isEffective(player));
            if (herb == null) {
                continue;
            }
            herbs.put(herb, item);
        }
        final int price = getPrice(player);
        int amount = player.getMemberRank().equalToOrGreaterThan(MemberRank.DRAGONSTONE) ? Integer.MAX_VALUE : price == 0 ? vialsOfWater : Math.min(coins / price, vialsOfWater);
        for (final Map.Entry<Herb, Item> entry : herbs.entrySet()) {
            if (entry == null) {
                continue;
            }
            final Herb herb = entry.getKey();
            final Item item = entry.getValue();
            final Item unfinishedPotion = herb.getUnfinishedPotion();
            int requestedAmount = item.getAmount();
            if (amount < requestedAmount) {
                requestedAmount = amount;
            }
            if (requestedAmount == 0) {
                player.getDialogueManager().plain("You can't make any unfinished potions with those items. Make sure you have enough noted vials of water, noted herbs and coins on you.");
                return;
            }
            player.getInventory().deleteItem(new Item(228, requestedAmount));
            player.getInventory().deleteItem(new Item(995, requestedAmount * price));
            player.getInventory().deleteItem(new Item(item.getId(), requestedAmount));
            player.getInventory().addItem(unfinishedPotion.getDefinitions().getNotedId(), requestedAmount);
            amount -= requestedAmount;
        }
        if (!herbs.isEmpty()) {
            player.getDialogueManager().plain( "There, all done.");
        } else {
            player.getDialogueManager().plain("You don't seem to have the right supplies for me to make unfinished potions. Make sure that the and vials of water herbs are noted.");
        }
    }

    private static int getPrice(final Player player) {
        if (player.getMemberRank().equalToOrGreaterThan(MemberRank.DRAGONSTONE)) {
            return 0;
        } else if (player.getMemberRank().equalToOrGreaterThan(MemberRank.DIAMOND)) {
            return 50;
        } else if (player.getMemberRank().equalToOrGreaterThan(MemberRank.RUBY)) {
            return 75;
        } else if (player.getMemberRank().equalToOrGreaterThan(MemberRank.EMERALD)) {
            return 100;
        } else if (player.getMemberRank().equalToOrGreaterThan(MemberRank.SAPPHIRE)) {
            return 125;
        } else if (player.getMemberRank().equalToOrGreaterThan(MemberRank.TOPAZ)) {
            return 150;
        }
        return 200;
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Clean")) {
            player.getDialogueManager().plain("Keep in mind, I can only clean noted grimy herbs right now.",
                () -> RegentMixer.cleanHerbs(player));
        }
        if (option.equals("Make unfinished potion(s)")) {
            player.getDialogueManager().plain("Keep in mind, I can only make unfinished potions out of noted herbs. They can either be grimy or cleaned but you'll need the Herblore skillcape perk for me to clean your grimy herbs.",
                () -> RegentMixer.makeUnfinishedPotions(player));
        }
        if (option.equals("Decant")) {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    decant().onDose(1, () -> {
                        if (RegentMixer.decant(player, 1)) {
                            setKey(5);
                        }
                    }).onDose(2, () -> {
                        if (RegentMixer.decant(player, 2)) {
                            setKey(5);
                        }
                    }).onDose(3, () -> {
                        if (RegentMixer.decant(player, 3)) {
                            setKey(5);
                        }
                    }).onDose(4, () -> {
                        if (RegentMixer.decant(player, 4)) {
                            setKey(5);
                        }
                    });
                    plain(5, "There, all done.");
                }
            });
        }
        if (option.equals("Use")) {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    options(TITLE,
                        "Clean my noted grimy herbs.",
                        "Decant my potions.",
                        "Make unfinished potions.",
                        "Nevermind.")
                        .onOptionOne(() -> setKey(5))
                        .onOptionTwo(() -> setKey(10))
                        .onOptionThree(() -> setKey(15));

                    plain(5, "Very, well. Just keep in mind You can only clean noted grimy herbs so unnoted grimy herbs will not work.")
                        .executeAction(() -> cleanHerbs(player));
                    decant(10).onDose(1, () -> {
                        if (RegentMixer.decant(player, 1)) {
                            setKey(20);
                        }
                    }).onDose(2, () -> {
                        if (RegentMixer.decant(player, 2)) {
                            setKey(20);
                        }
                    }).onDose(3, () -> {
                        if (RegentMixer.decant(player, 3)) {
                            setKey(20);
                        }
                    }).onDose(4, () -> {
                        if (RegentMixer.decant(player, 4)) {
                            setKey(20);
                        }
                    });
                    plain(15, "Very, well. Just keep in mind, You can only make unfinished potions out of noted herbs. They can either be grimy or cleaned but you'll need the Herblore skillcape perk for me " + "to clean your grimy herbs.")
                        .executeAction(() -> makeUnfinishedPotions(player));
                    plain(20, "There, all done.");
                }
            });
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { CustomObjectId.DECANTING_TABLE };
    }
}
