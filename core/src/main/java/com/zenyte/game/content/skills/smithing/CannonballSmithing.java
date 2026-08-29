package com.zenyte.game.content.skills.smithing;

import com.near_reality.game.content.donator.new_island.area.DonatorIslandQuadrant;
import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary;
import com.zenyte.game.content.boons.impl.MasterOfTheCraft;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.Random;

/**
 * @author Tommeh | 10 jun. 2018 | 16:12:07
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class CannonballSmithing extends Action {

    private static final int PRODUCT_MULTIPLIER = 2;
    private static final int LEVEL_REQ = 35;
    private static final double XP = 25.6;
    public static final Item MATERIAL = new Item(ItemId.STEEL_BAR);
    public static final Item MOULD = new Item(ItemId.AMMO_MOULD);
    public static final Item DOUBLE_AMMO_MOULD = new Item(ItemId.DOUBLE_AMMO_MOULD);


	private final int amount;
	private int cycle, ticks;

    public CannonballSmithing(int amount) {
        this.amount = amount;
    }

	@Override
	public boolean start() {
		if (!player.getInventory().containsItem(MOULD) && !player.getInventory().containsItem(DOUBLE_AMMO_MOULD)) {
			player.sendMessage("You need an ammo mould to make cannonballs.");
			return false;
		}
		if ((player.getSkills().getLevel(SkillConstants.SMITHING) + DonatorIslandQuadrant.Companion.getQuadrantHiddenSkillBoost(player)) < LEVEL_REQ) {
			player.getDialogueManager().start(new PlainChat(player, "You need a Smithing level of at least " + LEVEL_REQ + " to smith cannonballs."));
			return false;
		}
		return true;
    }

    @Override
    public boolean process() {
        if (!player.getInventory().containsItem(MATERIAL)) {
            return false;
        }
        return cycle < amount;
    }

    @Override
    public int processWithDelay() {
        int actionDelay = player.getBoonManager().hasBoon(MasterOfTheCraft.class) ? 1 : 3;
        if (ticks == 0) {
            player.sendFilteredMessage("You heat the steel bar into a liquid state.");
            player.setAnimation(Smelting.ANIMATION);
            player.sendSound(Smelting.soundEffect);
        } else if (ticks == actionDelay) {
            int productAmount;
            Item selectedMould = null;
            int requiredBars = 0;

            if (player.getInventory().containsItem(DOUBLE_AMMO_MOULD) && player.getInventory().getAmountOf(MATERIAL.getId()) >= 2) {
                selectedMould = DOUBLE_AMMO_MOULD;
                productAmount = 8 * PRODUCT_MULTIPLIER;
                requiredBars = 2;
            } else if (player.getInventory().containsItem(MOULD)) {
                selectedMould = MOULD;
                productAmount = 4 * PRODUCT_MULTIPLIER;
                requiredBars = 1;
            } else {
                productAmount = 0;
            }

            if (selectedMould != null) {
                var chance = new Random().nextDouble();
                var memberChange = getSaveChangeForRank(player.getMemberRank());
                if (memberChange > 0 && chance < memberChange)
                    smithCannonball(productAmount);
                else
                    player.getInventory().deleteItemsIfContains(new Item[]{new Item(MATERIAL.getId(), requiredBars)}, () -> smithCannonball(productAmount));
                cycle++;
            }
            return ticks = 0;
        }
        ticks++;
        return 0;
    }

    private void smithCannonball(int productAmount) {
        player.getAchievementDiaries().update(MorytaniaDiary.MAKE_CANNONBALLS);
        player.getInventory().addItem(new Item(ItemId.CANNONBALL, productAmount ));
        player.getSkills().addXp(SkillConstants.SMITHING, XP);
        player.sendFilteredMessage("You pour the molten metal into your cannonball mould.");
        player.sendFilteredMessage("The molten metal cools slowly to form " + productAmount + " cannonballs.");
    }

    private double getSaveChangeForRank(MemberRank rank) {
        return switch (rank) {
            case TOPAZ -> 0.05;
            case SAPPHIRE -> 0.08;
            case EMERALD -> 0.1;
            case RUBY -> 0.12;
            case DIAMOND -> 0.15;
            case DRAGONSTONE -> 0.2;
            case ONYX -> 0.25;
            case ZENYTE -> 0.3;
            case ENCHANTED -> 0.35;
            case GOLD -> 0.4;
            case ETERNAL -> 0.5;
            case NEBULA -> 0.6;
            case CATALYTIC -> 0.7;
            default -> 0.0;
        };
    }
}
