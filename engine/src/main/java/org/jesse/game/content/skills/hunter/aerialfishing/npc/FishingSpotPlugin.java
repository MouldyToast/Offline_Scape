package org.jesse.game.content.skills.hunter.aerialfishing.npc;

import org.jesse.game.content.achievementdiary.DiaryReward;
import org.jesse.game.content.achievementdiary.DiaryUtil;
import org.jesse.game.content.skills.fishing.Fishing;
import org.jesse.game.content.skills.hunter.aerialfishing.LakeMolchArea;
import org.jesse.game.content.skills.hunter.aerialfishing.item.AerialFish;
import org.jesse.game.content.skills.hunter.aerialfishing.item.AerialFishingTools;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Colour;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.pathfinding.events.player.EntityEvent;
import org.jesse.game.world.entity.pathfinding.strategy.DistancedEntityStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.Skills;
import org.jesse.game.world.entity.player.container.impl.Inventory;
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot;

import java.util.Map;

/**
 * @author Cresinkel
 */

public class FishingSpotPlugin extends NPCPlugin {

    private static final Graphics splashGraphics = new Graphics(1633);
    private static final Projectile throwingProjectileFrom = new Projectile(1632, 136, 40, 20, 25, 0, 64, 8);
    private static final Projectile throwingProjectileTo = new Projectile(1632, 40, 136, 20, 25, 0, 64, 8);

    @Override
    public void handle() {
        bind("Catch", new OptionHandler() {
                    @Override
                    public void handle(Player player, NPC npc) {
                        player.stopAll();
                        player.sendSound(2633);
                        player.sendSound(2634);
                        if (player.inArea(LakeMolchArea.class)) {
                            player.getEquipment().set(EquipmentSlot.WEAPON, new Item(ItemId.CORMORANTS_GLOVE));
                        }
                        int delay = World.sendProjectile(player.getLocation(), npc.getLocation(), throwingProjectileFrom);
                        WorldTasksManager.schedule(() -> {
                            World.sendGraphics(splashGraphics, npc.getLocation());
                            LakeMolchArea.handleCaughtSpot(npc);
                            int delay2 = World.sendProjectile(npc, player.getLocation(), throwingProjectileTo);
                            WorldTasksManager.schedule(() -> {
                                if (player.inArea(LakeMolchArea.class)) {
                                    reward(player);
                                    if (Utils.random(4) == 0) {
                                        removeBait(player);
                                    }
                                    player.getEquipment().set(EquipmentSlot.WEAPON, new Item(ItemId.CORMORANTS_GLOVE_22817));
                                }
                            }, delay2);
                        }, delay);
                    }

                    @Override
                    public void click(final Player player, final NPC npc, final NPCOption option) {
                        player.faceEntity(npc);
                        if (player.getEquipment().containsItem(ItemId.CORMORANTS_GLOVE)) {
                            player.sendFilteredMessage("You need to wait for your cormorant to return before trying to catch more fish.");
                            return;
                        }
                        if (!AerialFishingTools.hasBird(player)) {
                            player.sendFilteredMessage("Those fish are too far out from the land for you to catch alone.");
                            return;
                        }
                        if (!AerialFishingTools.hasBait(player)) {
                            player.sendFilteredMessage("It wouldn't be fair to send the cormorant out with nothing to reward it.");
                            return;
                        }
                        if (!player.getInventory().hasFreeSlots()) {
                            player.sendFilteredMessage("You do not have enough inventory space to do this.");
                            return;
                        }
                        player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 9), true));
                        handle(player,npc);
                    }
                }
        );
    }

    @Override
    public int[] getNPCs() {
        return new int[] {NpcId.FISHING_SPOT_8523};
    }

    private void removeBait(Player player) {
        Inventory inven = player.getInventory();
        int baitId = inven.containsItem(ItemId.KING_WORM) ? ItemId.KING_WORM : ItemId.FISH_CHUNKS;
        inven.deleteItem(baitId,1);
    }

    private void reward(Player player) {
        //Rada's Blessing functionality
        int doubleFish = 0;
        var diaryChance = 0 ;
        for (Map.Entry<DiaryReward, Integer> entry : Fishing.diaryRewardMap.entrySet()) {
            if (DiaryUtil.eligibleFor(entry.getKey(), player) && player.getEquipment().containsItem(entry.getKey().getItem())) {
                diaryChance = entry.getValue();
            }
        }
        if (diaryChance > 0  && Utils.random(100) <= diaryChance) {
            doubleFish = 1;
        }


        Inventory inven = player.getInventory();
        Skills skills = player.getSkills();
        //Golden Tench
        if (Utils.random(1999) == 0) {
            inven.addItem(ItemId.GOLDEN_TENCH, 1 + doubleFish);
            player.getCollectionLog().add(new Item(ItemId.GOLDEN_TENCH));
            player.sendMessage(Colour.RED.wrap("The cormorant has brought you a very strange tench."));
            return;
        }

        //Fish
        int hunterLevel = skills.getLevel(SkillConstants.HUNTER);
        int fishingLevel = skills.getLevel(SkillConstants.FISHING);
        int x = ((fishingLevel * 2) + hunterLevel) / 3;
        int random = Utils.random(0, x-1);
        if (random > 82) {
            tryGreaterSiren(hunterLevel, fishingLevel, skills, inven, doubleFish, player);
        } else if (random > 67) {
            tryMottledEel(hunterLevel, fishingLevel, skills, inven, doubleFish);
        } else if (random > 52) {
            tryCommonTench(hunterLevel, fishingLevel, skills, inven, doubleFish);
        } else {
            tryBlueGill(skills, inven, doubleFish);
        }

        //Molch Pearls (x == 99 on both fishing and hunter levels 99)
        int chance = 99 - ((3 * x) / 4);
        if (Utils.random(chance) == 0) {
            inven.addOrDrop(ItemId.MOLCH_PEARL, 1);
        }
    }

    private void tryGreaterSiren(int hunterLevel, int fishingLevel, Skills skills, Inventory inven, int doubleFish, Player player) {
        if(hunterLevel >= AerialFish.GREATER_SIREN.getHunterLevel() && fishingLevel >= AerialFish.GREATER_SIREN.getFishingLevel()) {
            skills.addXp(SkillConstants.FISHING, AerialFish.GREATER_SIREN.getFishingXP());
            skills.addXp(SkillConstants.HUNTER, AerialFish.GREATER_SIREN.getHunterXP());
            inven.addItem(ItemId.GREATER_SIREN, 1 + doubleFish);
            //player.getDailyChallengeManager().update(SkillingChallenge.CATCH_GREATER_SIREN);
            if (doubleFish == 1) {
                //player.getDailyChallengeManager().update(SkillingChallenge.CATCH_GREATER_SIREN);
            }
        } else {
            tryMottledEel(hunterLevel, fishingLevel, skills, inven, doubleFish);
        }
    }

    private void tryMottledEel(int hunterLevel, int fishingLevel, Skills skills, Inventory inven, int doubleFish) {
        if(hunterLevel >= AerialFish.MOTTLED_EEL.getHunterLevel() && fishingLevel >= AerialFish.MOTTLED_EEL.getFishingLevel()) {
            skills.addXp(SkillConstants.FISHING, AerialFish.MOTTLED_EEL.getFishingXP());
            skills.addXp(SkillConstants.HUNTER, AerialFish.MOTTLED_EEL.getHunterXP());
            inven.addItem(ItemId.MOTTLED_EEL, 1 + doubleFish);
        } else {
            tryCommonTench(hunterLevel, fishingLevel, skills, inven, doubleFish);
        }
    }

    private void tryCommonTench(int hunterLevel, int fishingLevel, Skills skills, Inventory inven, int doubleFish) {
        if(hunterLevel >= AerialFish.COMMON_TENCH.getHunterLevel() && fishingLevel >= AerialFish.COMMON_TENCH.getFishingLevel()) {
            skills.addXp(SkillConstants.FISHING, AerialFish.COMMON_TENCH.getFishingXP());
            skills.addXp(SkillConstants.HUNTER, AerialFish.COMMON_TENCH.getHunterXP());
            inven.addItem(ItemId.COMMON_TENCH, 1 + doubleFish);
        } else {
            tryBlueGill(skills, inven, doubleFish);
        }
    }

    private void tryBlueGill(Skills skills, Inventory inven, int doubleFish) {
        skills.addXp(SkillConstants.FISHING, AerialFish.BLUEGILL.getFishingXP());
        skills.addXp(SkillConstants.HUNTER, AerialFish.BLUEGILL.getHunterXP());
        inven.addItem(ItemId.BLUEGILL, 1 + doubleFish);
    }
}
