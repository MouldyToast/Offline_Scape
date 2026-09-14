package org.jesse.game.world.entity.npc.impl.slayer;

import org.jesse.game.content.achievementdiary.diaries.DesertDiary;
import org.jesse.game.content.skills.slayer.SlayerEquipment;
import org.jesse.game.util.Direction;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;

/**
 * @author Tommeh | 9 dec. 2017 : 23:10:39
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DustDevil extends NPC implements CombatScript, Spawnable {
	private static final Projectile PROJECTILE = new Projectile(73, 120, 140, 108, 10);

	public DustDevil(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public int attack(final Entity target) {
		if (!(target instanceof Player)) {
			return 0;
		}
		final Player player = (Player) target;
		attackSound();
		setAnimation(getCombatDefinitions().getAttackAnim());
		if (!SlayerEquipment.FACE_MASK.isWielding(player)) {
			getCombatDefinitions().setAttackStyle("Magic");
			delayHit(this, World.sendProjectile(this, target, PROJECTILE), player, new Hit(this, 15, HitType.REGULAR).onLand(hit -> {
				player.getSkills().setLevel(SkillConstants.ATTACK, 0);
				player.getSkills().setLevel(SkillConstants.STRENGTH, 0);
				player.getSkills().setLevel(SkillConstants.RANGED, 0);
				player.getSkills().setLevel(SkillConstants.MAGIC, 0);
				player.getSkills().setLevel(SkillConstants.PRAYER, (int) (player.getSkills().getLevel(SkillConstants.PRAYER) * 0.5064935064935066));
				player.getSkills().setLevel(SkillConstants.DEFENCE, (int) (player.getSkills().getLevel(SkillConstants.DEFENCE) * 0.5064935064935066));
				player.getSkills().setLevel(SkillConstants.AGILITY, (int) (player.getSkills().getLevel(SkillConstants.AGILITY) * 0.5064935064935066));
			}));
		} else {
			getCombatDefinitions().setAttackStyle("Melee");
			delayHit(this, 0, player, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
		}
		return getCombatDefinitions().getAttackSpeed();
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		if (source instanceof Player) {
			final Player player = (Player) source;
			if (SlayerEquipment.SLAYER_HELM.isWielding(player)) {
				player.getAchievementDiaries().update(DesertDiary.SLAY_DUST_DEVIL);
			}
		}
	}

	@Override
	public boolean validate(final int id, final String name) {
		return name.equals("dust devil");
	}
}
