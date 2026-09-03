package com.zenyte.game.content.skills.thieving.actions;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.content.achievementdiary.diaries.ArdougneDiary;
import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary;
import com.zenyte.game.content.achievementdiary.diaries.LumbridgeDiary;
import com.zenyte.game.content.achievementdiary.diaries.WesternProvincesDiary;
import com.zenyte.game.content.skills.magic.spells.arceuus.ShadowVeilKt;
import com.zenyte.game.content.skills.thieving.CoinPouch;
import com.zenyte.game.content.skills.thieving.PocketData;
import com.zenyte.game.content.skills.thieving.Thieving;
import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ImmutableItem;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;

/**
 * @author Kris | 21. okt 2017 : 12:41.29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public class Pickpocket extends Action {
	private static final Item ROGUE_BOOTS = new Item(ItemId.ROGUE_BOOTS);
	private static final Item ROGUE_GLOVES = new Item(ItemId.ROGUE_GLOVES);
	private static final Item ROGUE_MASK = new Item(ItemId.ROGUE_MASK);
	private static final Item ROGUE_TOP = new Item(ItemId.ROGUE_TOP);
	private static final Item ROGUE_TROUSERS = new Item(ItemId.ROGUE_TROUSERS);
	private static final Animation STUN_ANIM = new Animation(422);
	private static final Animation PICKPOCKET_ANIM = new Animation(881);
	private static final Animation BLOCK_ANIM = new Animation(424);
	public static final Graphics STUN_GFX = new Graphics(80, 5, 60);
	private final PocketData data;
	private final NPC npc;

	@Override
	protected void onInterruption() {
	}

	/**
	 * Checks if the player is wearing the Rogue equipment, which increases the chance of player
	 * receiving double loot when pickpocketing. The chance of receiving double loot is 100% when
	 * wearing the full set.
	 * @return whether or not to double the loot.
	 */
	private boolean isDoubleLoot() {
		int count = 0;
		final Equipment equipment = player.getEquipment();
		if (equipment.getId(EquipmentSlot.HELMET) == 5554) count++;
		if (equipment.getId(EquipmentSlot.PLATE) == 5553) count++;
		if (equipment.getId(EquipmentSlot.HANDS) == 5556) count++;
		if (equipment.getId(EquipmentSlot.LEGS) == 5555) count++;
		if (equipment.getId(EquipmentSlot.BOOTS) == 5557) count++;
		return Utils.random(99) <= (count * 20);
	}

	@Override
	public boolean start() {
		if (player.isUnderCombat(0)) {
			player.sendMessage("You can't do this while in combat.");
			return false;
		}
		if (data == null) {
			player.sendMessage("You cannot pickpocket that NPC.");
			return false;
		}
		if ((player.getSkills().getLevel(SkillConstants.THIEVING)) < data.getLevel()) {
			player.sendMessage("You need a Thieving level of at least " + data.getLevel() + " to pickpocket " + npc.getName(player).toLowerCase() + ".");
			return false;
		}
		else if (!player.getInventory().hasFreeSlots()) {
			final ImmutableItem[] loot = data.getLoot();
			// If the pockets only contain coins and the player already has coin pouch(es), we allow them to pickpocket with "full" inventory.
			if (!(loot == null || (loot.length == 1 && loot[0] != null && CoinPouch.ITEMS.keySet().contains(loot[0].getId()) && player.getInventory().containsItem(loot[0].getId(), 1)))) {
				player.sendMessage("You need some free inventory space to pickpocket the " + npc.getName(player).toLowerCase() + ".");
				return false;
			}
		}
		if (npc.isDead() || npc.isFinished()) {
			player.sendMessage("Too late, " + npc.getDefinitions().getName().toLowerCase() + " is already dead.");
			return false;
		}
		if (data.getCoinPouch() != null && player.getInventory().getAmountOf(data.getCoinPouch().getItemId()) >= CoinPouch.MAX_POUCHES_PER_STACK) {
			player.sendMessage("You need to empty your coin pouches before you can continue pickpocketing.");
			return false;
		}
		attemptPickPocket();
		delay(1);
		player.lock();
		return true;
	}

	@Override
	public boolean process() {
		return !npc.isDead();
	}

	public Pickpocket(PocketData data, NPC npc) {
		this.data = data;
		this.npc = npc;
	}

	private void attemptPickPocket() {
		player.faceEntity(npc);
		player.setAnimation(PICKPOCKET_ANIM);
		player.sendFilteredMessage("You attempt to pick the " + npc.getName(player).toLowerCase() + "'s pocket.");
	}

	private void stunPlayer() {
		player.setGraphics(STUN_GFX);
		player.sendFilteredMessage("You have been stunned.");
		player.applyHit(new Hit(npc, Utils.random(data.getMinDamage(), data.getMaxDamage()), HitType.TYPELESS));
		player.stun(data.getStunTime());
		WorldTasksManager.schedule(() -> player.setGraphics(Graphics.RESET), data.getStunTime() - 1);
	}

	private void processDodgyNecklace() {
		final int uses = player.getNumericAttribute("dodgy necklace uses").intValue() + 1;
		player.addAttribute("dodgy necklace uses", uses % 10);
		if (uses == 10) {
			player.getEquipment().set(EquipmentSlot.AMULET, null);
			player.sendMessage("Your dodgy necklace protects you. " + Colour.RED.wrap("It then crumbles to dust."));
		}
		else
			player.sendFilteredMessage("Your dodgy necklace protects you. %s"
				.formatted(Colour.RED.wrap("It has " + (10 - uses) + " charge" + (uses == 9 ? "" : "s") + " left.")));
	}

	private void handleFailedAttempt() {
		player.sendFilteredMessage("You fail to pick the " + npc.getName(player).toLowerCase() + "'s pocket.");
		if (ShadowVeilKt.getShadowVeilEffect(player) && Utils.random(99) < 15) {
			player.sendFilteredMessage("Your attempt to steal goes unnoticed.");
			stop();
			return;
		}
		npc.setAnimation(STUN_ANIM);
		npc.faceEntity(player);
		player.setAnimation(BLOCK_ANIM);
		final int necklace = player.getEquipment().getId(EquipmentSlot.AMULET);
		if ((necklace == ItemId.DODGY_NECKLACE && Utils.random(3) == 0)) {
			processDodgyNecklace();
			return;
		}
		stunPlayer();
		npc.setForceTalk(new ForceTalk(data.getPossibleMessages()[Utils.random(data.getPossibleMessages().length - 1)]));
		stop();
	}

	private void distributeOutfit() {
		boolean containsBoots = player.containsItem(ROGUE_BOOTS);
		boolean containsGloves = player.containsItem(ROGUE_GLOVES);
		boolean containsMask = player.containsItem(ROGUE_MASK);
		boolean containsTrousers = player.containsItem(ROGUE_TROUSERS);
		boolean containsTop = player.containsItem(ROGUE_TOP);
		int count = 0;
		if (containsBoots)
			count++;
		if (containsGloves)
			count++;
		if (containsMask)
			count++;
		if (containsTrousers)
			count++;
		if (containsTop)
			count++;

		Item item = null;
		if (!containsBoots)
			item = ROGUE_BOOTS;
		else if (!containsGloves)
			item = ROGUE_GLOVES;
		else if (!containsMask)
			item = ROGUE_MASK;
		else if (!containsTrousers)
			item = ROGUE_TROUSERS;
		else if (!containsTop)
			item = ROGUE_TOP;

		if (item != null) {
			player.sendMessage("You managed to find a %s. You have collected %d/5 pieces of the outfit."
				.formatted(item.getName(), count));
			player.getInventory().addOrDrop(item);
			player.getCollectionLog().add(item);
		}
	}

	private void handleSuccessfulPickpocket() {
		final var inventory = player.getInventory();
		final var isBot = PlayerAttributesKt.getFlaggedAsBot(player);

		player.sendFilteredMessage("You pick the " + npc.getName(player).toLowerCase() + "'s pocket.");
		switch (data) {
			case HERO -> player.getAchievementDiaries().update(ArdougneDiary.PICKPOCKET_A_HERO);
			case GUARD -> player.getAchievementDiaries().update(FaladorDiary.PICKPOCKET_FALADOR_GUARD);
			case MASTER_FARMER -> {
				if (npc.getName(player).contains("Martin"))
					player.getAchievementDiaries().update(LumbridgeDiary.PICKPOCKET_MASTER_GARDENER);
				player.getDailyChallengeManager().update(SkillingChallenge.PICKPOCKET_MASTER_FARMERS);
				player.getAchievementDiaries().update(ArdougneDiary.PICKPOCKET_MASTER_FARMER);
			}
			case MAN -> player.getAchievementDiaries().update(LumbridgeDiary.PICKPOCKET_A_MAN);
			case GNOME -> player.getAchievementDiaries().update(WesternProvincesDiary.PICKPOCKET_A_GNOME);
			case ELF -> {
				player.getDailyChallengeManager().update(SkillingChallenge.PICKPOCKET_ELVES);
				player.getAchievementDiaries().update(WesternProvincesDiary.PICKPOCKET_ELF);
				SherlockTask.PICKPOCKET_AN_ELF.progress(player);
			}
			case FEMALE_HAM_MEMBER, MALE_HAM_MEMBER -> player.getDailyChallengeManager().update(SkillingChallenge.PICKPOCKET_HAM_MEMBERS);
		}
		player.getSkills().addXp(SkillConstants.THIEVING, data.getExperience());
		if (Utils.random(isBot ? 69_420_000 : 200) == 0)
			distributeOutfit();

		data.generateRandomLoot(isDoubleLoot()).forEach(item -> {
			inventory.addOrDrop(item);
			player.getCollectionLog().add(item);
		});
	}

	@Override
	public int processWithDelay() {
		attemptPickPocket();
		final Inventory inventory = player.getInventory();
		if (!Thieving.success(player, data.getSuccessLevel())) {
			handleFailedAttempt();
			return -1;
		}
		else
			handleSuccessfulPickpocket();

		if((data.getCoinPouch() != null && inventory.getAmountOf(data.getCoinPouch().getItemId()) == 28) || !inventory.hasFreeSlots()) {
			player.sendMessage("You should empty your pockets before trying to thieve any more loot.");
			return -1;
		}
		return -1;
	}

	@Override
	public void stop() {
		npc.setFaceEntity(null);
		delay(3);
		player.unlock();
	}
}
