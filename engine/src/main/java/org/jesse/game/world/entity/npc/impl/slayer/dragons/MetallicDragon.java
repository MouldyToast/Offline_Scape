package org.jesse.game.world.entity.npc.impl.slayer.dragons;

import org.jesse.game.content.achievementdiary.diaries.KandarinDiary;
import org.jesse.game.content.achievementdiary.diaries.KaramjaDiary;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.PlayerCombat;

/**
 * @author Kris | 12. veebr 2018 : 12:18.50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>
 */
public final class MetallicDragon extends NPC implements CombatScript, Spawnable {
	private static final Projectile DRAGONFIRE_PROJ = new Projectile(54, 120, 120, 38, 10, 28, 0, 5);
	private static final Animation ATTACK_ANIM = new Animation(80);
	private static final Animation SECONDARY_ATTACK_ANIM = new Animation(91);
	private static final Animation DRAGONFIRE_ANIM = new Animation(81);

	public MetallicDragon(int id, Location tile, Direction facing, int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		if (source instanceof Player) {
			final Player player = (Player) source;
			final String name = getName(player);
			if (name.contains("Mithril")) {
				player.getAchievementDiaries().update(KandarinDiary.KILL_A_MITHRIL_DRAGON);
			} else {
				player.getAchievementDiaries().update(KaramjaDiary.KILL_A_METAL_DRAGON);
			}
		}
	}

	@Override
	public int attack(final Entity target) {
		final int style = Utils.random(isWithinMeleeDistance(this, target) ? 2 : 0);
		if (style == 0 && target instanceof Player) {
			setAnimation(DRAGONFIRE_ANIM);
			final Player player = (Player) target;
			final Dragonfire dragonfire = new Dragonfire(DragonfireType.CHROMATIC_DRAGONFIRE, 50, DragonfireProtection.getProtection(this, player));
			delayHit(World.sendProjectile(this, target, DRAGONFIRE_PROJ), target, new Hit(this, Utils.random(dragonfire.getDamage()), HitType.REGULAR).onLand(hit -> {
				PlayerCombat.appendDragonfireShieldCharges(player);
				player.sendFilteredMessage(String.format(dragonfire.getMessage(), "dragon's fiery breath"));
			}));
		} else {
			setAnimation(Utils.random(1) == 0 ? ATTACK_ANIM : SECONDARY_ATTACK_ANIM);
			delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
		}
		return getCombatDefinitions().getAttackSpeed();
	}

	@Override
	public boolean validate(int id, String name) {
		return name.equals("steel dragon") || name.equals("bronze dragon") || name.equals("iron dragon") || name.equals("mithril dragon");
	}
}
