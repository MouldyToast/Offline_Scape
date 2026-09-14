package org.jesse.game.content.minigame.pestcontrol.npc;

import org.jesse.game.content.minigame.pestcontrol.PestControlInstance;
import org.jesse.game.content.minigame.pestcontrol.PestNPC;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.combat.CombatScript;

/**
 * @author Kris | 29. juuni 2018 : 21:47:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class DefilerNPC extends PestNPC implements CombatScript {

	private static final SoundEffect ATTACK_SOUND_EFFECT = new SoundEffect(392, 5);
	private static final Projectile PROJECTILE = new Projectile(657, 200, 120, 20, 25, 10, 0, 5);

	public DefilerNPC(final PestControlInstance instance, final PestPortalNPC portal, final int id, final Location tile) {
		super(instance, portal, id, tile);
		forceAggressive = true;
		attackDistance = 10;
	}

	@Override
	public int attack(final Entity target) {
		World.sendSoundEffect(this, ATTACK_SOUND_EFFECT);
		setAnimation(combatDefinitions.getAttackAnim());
		delayHit(World.sendProjectile(this, target, PROJECTILE), target, new Hit(this, this.getRandomMaxHit(this, combatDefinitions.getMaxHit(), RANGED, target), HitType.MAGIC));
		return combatDefinitions.getAttackSpeed();
	}

}
