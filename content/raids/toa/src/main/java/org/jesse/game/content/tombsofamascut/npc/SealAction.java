package org.jesse.game.content.tombsofamascut.npc;

import org.jesse.game.content.skills.mining.PickAxeDefinition;
import org.jesse.game.content.skills.mining.MiningDefinitions;
import org.jesse.game.content.tombsofamascut.encounter.HetEncounter;
import org.jesse.game.content.tombsofamascut.raid.EncounterStage;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Action;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.Skills;
import org.jesse.plugins.dialogue.PlainChat;

import java.util.Optional;

/**
 * @author Savions.
 */
public class SealAction extends NPCPlugin {

	private static final SoundEffect HIT_SOUND = new SoundEffect(3600);

	@Override public void handle() {
		bind("Destroy", (player, npc) -> {
			if (player.getArea() instanceof final HetEncounter encounter) {
				if (!EncounterStage.STARTED.equals(encounter.getStage())) {
					player.sendMessage("You don't have to do that right now.");
				} else if (!encounter.canDestroySeal()) {
					player.sendMessage("A strange force is protecting the seal from damage. It must be weakened somehow.");
				} else {
					player.getActionManager().setAction(new Action() {

						private PickAxeDefinition tool;
						private int baseDamage;

						@Override public boolean start() {
							if (!check()) {
								return false;
							}
							player.sendMessage("You swing your pick at the seal.");
							player.setAnimation(tool.getAnim());
							delay(1);
							return true;
						}

						@Override public boolean process() {
							final boolean check = check();
							if (!check) {
								player.setAnimation(Animation.STOP);
							}
							return check;
						}

						@Override public int processWithDelay() {
							if (encounter.getSeal() != null) {
								player.setAnimation(tool.getAnim());
								player.sendSound(HIT_SOUND);
								player.getSkills().addXp(Skills.MINING, 35);
								encounter.getSeal().scheduleHit(player, new Hit(player, baseDamage + Utils.random(2), HitType.SHIELD_CHARGE), 1);
							}
							return 2;
						}

						private boolean check() {
							if (encounter.getSeal() == null || encounter.getSeal().isDead() || encounter.getSeal().isFinished() || !EncounterStage.STARTED.equals(encounter.getStage())) {
								return false;
							}
							if (!encounter.canDestroySeal()) {
								return false;
							}
							final Optional<MiningDefinitions.PickaxeDefinitions.PickaxeResult> axe = MiningDefinitions.PickaxeDefinitions.get(player, true);
							if (axe.isEmpty()) {
								player.getDialogueManager().start(new PlainChat(player, "You need a pickaxe to do that. You do not have a pickaxe which you have the Mining level to use."));
								return false;
							}
							final MiningDefinitions.PickaxeDefinitions.PickaxeResult result = axe.get();
							tool = result.getDefinition();
							final MiningDefinitions.PickaxeDefinitions axetDef = MiningDefinitions.PickaxeDefinitions.getToolDef(result.getItem().getId());
							baseDamage = axetDef != null ? getPlayerBaseDamage(player, axetDef) : 5;
							return true;
						};
					});
				}
			}
		});
	}

	private static final int getPlayerBaseDamage(Player player, MiningDefinitions.PickaxeDefinitions pickaxe) {
		final int level = player.getSkills().getLevel(Skills.MINING);
		switch(pickaxe) {
			case DRAGON, DRAGON_OR, INFERNAL, UNCHARGED_INFERNAL, INACTIVE_CRYSTAL, THIRD_AGE -> {
				if (level >= 100) {
					return 19;
				} else if (level >= 85) {
					return 17;
				} else {
					return 10;
				}
			}
			case RUNE, GILDED -> {
				if (level >= 100) {
					return 17;
				} else if (level >= 85) {
					return 15;
				} else {
					return 8;
				}
			}
			default -> {
				if (level >= 100) {
					return 14;
				} else if (level >= 85) {
					return 13;
				} else {
					return 5;
				}
			}
		}
	}

	@Override public int[] getNPCs() {
		return new int[] {HetSeal.ID, HetSeal.ID + 1};
	}
}
