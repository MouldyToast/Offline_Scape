package com.zenyte.game.content.skills.runecrafting;

import com.google.common.base.Preconditions;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.achievementdiary.diaries.*;
import com.zenyte.game.content.advent.AdventCalendarManager;
import com.zenyte.game.content.follower.Pet;
import com.zenyte.game.content.follower.impl.SkillingPet;
import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.RequestResult;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.plugins.itemonitem.ChipDarkEssenceBlockItemAction;
import mgi.types.config.items.ItemDefinitions;

import java.util.OptionalInt;

/**
 * @author Kris | 19. dets 2017 : 2:47.03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BasicRunecraftingAction extends Action {

	public static final Animation RUNECRAFTING_ANIM = new Animation(791);
	public static final Graphics RUNECRAFTING_GFX = new Graphics(186, 0, 96);

	public BasicRunecraftingAction(final Runecrafting rune) {
		this.rune = rune;
	}

	private final Runecrafting rune;

	@Override
	public boolean start() {
		if ((player.getSkills().getLevel(SkillConstants.RUNECRAFTING)) < rune.getLevel()) {
			player.sendMessage("You need at least " + rune.getLevel() + " Runecrafting to runecraft " + rune.toString().replace("_", " ").toLowerCase() + "s.");
			return false;
		}
		if (rune.getEssenceType() == 2 && !player.getInventory().containsItem(7936, 1) && !player.getInventory().containsItem(ItemId.DAEYALT_ESSENCE, 1)) {
			player.sendMessage("You need some pure essence or daeyalt essence to runecraft " + ItemDefinitions.get(rune.getRuneId()).getName().toLowerCase() + "s.");
			return false;
		} else if (rune.getEssenceType() == 1 && !player.getInventory().containsItem(1436, 1) && !player.getInventory().containsItem(7936, 1) && !player.getInventory().containsItem(ItemId.DAEYALT_ESSENCE, 1)) {
			player.sendMessage("You need some rune or pure essence or daeyalt essence to runecraft " + rune.toString().toLowerCase().replace("_", " ") + "s.");
			return false;
		} else if (rune.getEssenceType() == 3 && !player.getInventory().containsItem(7938, 1)) {
			player.sendMessage("You need some dark essence fragments to runecraft " + rune.toString().toLowerCase().replace("_", " ") + "s.");
			return false;
		}
		player.setAnimation(RUNECRAFTING_ANIM);
		player.setGraphics(RUNECRAFTING_GFX);
		return true;
	}

	@Override
	public boolean process() {
		return true;
	}

	@Override
	public int processWithDelay() {
		boolean useDaeyalt = player.getInventory().containsItem(ItemId.DAEYALT_ESSENCE, 1);
		int amtDaeyalt = player.getInventory().getAmountOf(ItemId.DAEYALT_ESSENCE);
		int runes = useDaeyalt ? amtDaeyalt : rune.getEssenceType() == 2 ? (player.getInventory().getAmountOf(7936)) : (player.getInventory().getAmountOf(7936) + player.getInventory().getAmountOf(1436));

		if(useDaeyalt) {
			player.getInventory().deleteItem(ItemId.DAEYALT_ESSENCE, runes);
		}
		if (rune.getEssenceType() == 1 && !useDaeyalt) {
			player.getInventory().deleteItem(1436, runes);
		}
		if ((rune.getEssenceType() == 1 || rune.getEssenceType() == 2) && !useDaeyalt) {
			player.getInventory().deleteItem(7936, runes);
		} else if (rune.getEssenceType() == 3) {
			final OptionalInt slot = ChipDarkEssenceBlockItemAction.findFragmentsSlot(player);
			final int slotId = slot.orElseThrow(RuntimeException::new);
			final Item item = player.getInventory().getItem(slotId);
			Preconditions.checkArgument(item.getId() == 7938);
			runes = item.getCharges();
			player.getInventory().deleteItem(slotId, item);
		}
		int multiplier = rune.getDoubleRunes() == -1 ? 0 : (int) Math.floor((double) player.getSkills().getLevel(SkillConstants.RUNECRAFTING) / rune.getDoubleRunes());
		multiplier += 1;

		int extractMultiplier = 1;
		int warpedExtractCount = Math.min(runes, player.getInventory().getAmountOf(ItemId.WARPED_EXTRACT));
		int twistedExtractCount = Math.min(runes, player.getInventory().getAmountOf(ItemId.TWISTED_EXTRACT));
		int mangledExtractCount = Math.min(runes, player.getInventory().getAmountOf(ItemId.MANGLED_EXTRACT));
		int scarredExtractCount = Math.min(runes, player.getInventory().getAmountOf(ItemId.SCARRED_EXTRACT));

		if (player.getInventory().containsItem(ItemId.WARPED_EXTRACT, warpedExtractCount)) {
			if (rune == Runecrafting.AIR_RUNE || rune == Runecrafting.MIND_RUNE || rune == Runecrafting.WATER_RUNE || rune == Runecrafting.EARTH_RUNE || rune == Runecrafting.FIRE_RUNE || rune == Runecrafting.BODY_RUNE) {
				if (player.getInventory().deleteItem(ItemId.WARPED_EXTRACT, warpedExtractCount).getResult() == RequestResult.SUCCESS) {
					extractMultiplier = 250 * warpedExtractCount;
				}
			}
		}

		if (player.getInventory().containsItem(ItemId.TWISTED_EXTRACT, twistedExtractCount)) {
			if (rune == Runecrafting.COSMIC_RUNE || rune == Runecrafting.CHAOS_RUNE) {
				if (player.getInventory().deleteItem(ItemId.TWISTED_EXTRACT, twistedExtractCount).getResult() == RequestResult.SUCCESS) {
					extractMultiplier = 60 * twistedExtractCount;
				}
			}
		}

		if (player.getInventory().containsItem(ItemId.MANGLED_EXTRACT, mangledExtractCount)) {
			if (rune == Runecrafting.NATURE_RUNE || rune == Runecrafting.LAW_RUNE || rune == Runecrafting.ASTRAL_RUNE || rune == Runecrafting.DEATH_RUNE) {
				if (player.getInventory().deleteItem(ItemId.MANGLED_EXTRACT, mangledExtractCount).getResult() == RequestResult.SUCCESS) {
					extractMultiplier = 60 * mangledExtractCount;
				}
			}
		}

		if (player.getInventory().containsItem(ItemId.SCARRED_EXTRACT, scarredExtractCount)) {
			if (rune == Runecrafting.BLOOD_RUNE_REAL || rune == Runecrafting.BLOOD_RUNE || rune == Runecrafting.SOUL_RUNE || rune == Runecrafting.WRATH_RUNE) {
				if (player.getInventory().deleteItem(ItemId.SCARRED_EXTRACT, scarredExtractCount).getResult() == RequestResult.SUCCESS) {
					extractMultiplier = 60 * scarredExtractCount;
				}
			}
		}

		int amount = (runes * multiplier) + extractMultiplier;

		if (DiaryUtil.eligibleFor(DiaryReward.RADAS_BLESSING4, player) && rune.equals(Runecrafting.BLOOD_RUNE)) {
			amount *= 1.1;
		}
		if (rune.equals(Runecrafting.BLOOD_RUNE) || rune.equals(Runecrafting.BLOOD_RUNE_REAL)) {
			final Item item = player.getInventory().getItemById(ItemId.BLOOD_ESSENCE_ACTIVE);
			if (item != null) {
				final int extra = Utils.random(Math.min(item.getCharges(), runes));
				amount += extra;
				player.getChargesManager().removeCharges(item, extra, player.getInventory().getContainer(), player.getInventory().getContainer().getSlot(item));
				player.sendMessage("You manage to extract power from the Blood Essence and craft " + extra + " extra runes.");
			}
		}
		final double experience = rune.getExperience() * runes;
		if (rune.equals(Runecrafting.COSMIC_RUNE)) {
			SherlockTask.CRAFT_MULTIPLE_COSMIC_RUNES.progress(player);
		}
		if (amount >= 140 && rune.equals(Runecrafting.MIND_RUNE)) {
			player.getAchievementDiaries().update(FaladorDiary.CRAFT_MIND_RUNES);
		} else if (amount >= 252 && rune.equals(Runecrafting.AIR_RUNE)) {
			player.getAchievementDiaries().update(FaladorDiary.CRAFT_AIR_RUNES);
		} else if (rune.equals(Runecrafting.COSMIC_RUNE)) {
			if (amount >= 56) {
				player.getAchievementDiaries().update(LumbridgeDiary.CRAFT_COSMIC_RUNES);
			}
			player.getDailyChallengeManager().update(SkillingChallenge.CRAFT_COSMIC_RUNES, amount * 2);
		} else if (amount >= 56 && rune.equals(Runecrafting.ASTRAL_RUNE)) {
			player.getAchievementDiaries().update(FremennikDiary.CRAFT_ASTRAL_RUNES);
		} else if (rune.equals(Runecrafting.DEATH_RUNE)) {
			player.getDailyChallengeManager().update(SkillingChallenge.CRAFT_DEATH_RUNES, amount * 2);
			player.getAchievementDiaries().update(ArdougneDiary.CRAFT_DEATH_RUNES);
		} else if (rune.equals(Runecrafting.NATURE_RUNE)) {
			if (amount >= 56) {
				player.getAchievementDiaries().update(KaramjaDiary.CRAFT_56_NATURE_RUNES);
			}
			SherlockTask.CRAFT_A_NATURE_RUNE.progress(player);
			player.getAchievementDiaries().update(KaramjaDiary.CRAFT_NATURE_RUNES);
			player.getDailyChallengeManager().update(SkillingChallenge.CRAFT_NATURE_RUNES, amount * 2);
		} else if (rune.equals(Runecrafting.EARTH_RUNE)) {
			if (amount >= 100) {
				player.getAchievementDiaries().update(VarrockDiary.CRAFT_100_EARTH_RUNES);
			}
			player.getAchievementDiaries().update(VarrockDiary.CRAFT_EARTH_RUNES);
		} else if (rune.equals(Runecrafting.WATER_RUNE)) {
			if (amount >= 140) {
				player.getAchievementDiaries().update(LumbridgeDiary.CRAFT_140_WATER_RUNES);
			}
			player.getAchievementDiaries().update(LumbridgeDiary.CRAFT_WATER_RUNES);
			player.getDailyChallengeManager().update(SkillingChallenge.CRAFT_WATER_RUNES, amount * 2);
		} else if (rune.equals(Runecrafting.FIRE_RUNE)) {
			player.getDailyChallengeManager().update(SkillingChallenge.CRAFT_FIRE_RUNES, amount * 2);
		} else if (rune.equals(Runecrafting.LAW_RUNE)) {
			player.getDailyChallengeManager().update(SkillingChallenge.CRAFT_LAW_RUNES, amount * 2);
		} else if (rune.equals(Runecrafting.SOUL_RUNE)) {
			player.getDailyChallengeManager().update(SkillingChallenge.CRAFT_SOUL_RUNES, amount * 2);
		} else if (rune.equals(Runecrafting.WRATH_RUNE)) {
			player.getDailyChallengeManager().update(SkillingChallenge.CRAFT_WRATH_RUNES, amount * 2);
		} else if (rune.equals(Runecrafting.BLOOD_RUNE)) {
			player.getAchievementDiaries().update(KourendDiary.CRAFT_ONE_OR_MORE_BLOOD_RUNES);
			AdventCalendarManager.increaseChallengeProgress(player, 2022, 12, amount * 2);
		}
		player.getSkills().addXp(SkillConstants.RUNECRAFTING, useDaeyalt ? experience * 1.5 : experience);
		player.getInventory().addItem(new Item(rune.getRuneId(), amount));
		player.sendFilteredMessage("You bind the Temple's power into " + rune.toString().replace("_", " ").toLowerCase() + "s.");
		player.sendFilteredMessage("The gods bless your efforts and grant you extra runes.");
		if (player.getFollower() != null) {
			final Pet pet = player.getFollower().getPet();
			if (SkillingPet.isRiftGuardian(pet) && !player.getBooleanAttribute("rift_guardian_colour_lock")) {
				player.setPetId(rune.getPet().getPetId());
				player.getFollower().setTransformation(rune.getPet().getPetId());
			}
		}
		final double chance = experience > 2500000 ? 0.5F : (experience / 5000000);
		rune.getPet().roll(player, (int) (1.0F / chance) - 1);
		return -1;
	}

}
