package org.jesse.game.content.boss.nightmare;

import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.Player;

public class NightmareHuskRangedNPC extends NightmareHuskNPC {

	public static final int ID = NpcId.HUSK;
	public static final int ID_PHOSANIS = NpcId.HUSK_9466;
	private static final Animation ATTACK_ANIMATION = new Animation(8564);
	private static final Projectile PROJECTILE = new Projectile(1778, 30, 30, 20, 1, 30, 0, 0);

	public NightmareHuskRangedNPC(int id, Location tile, Player spawnedFor, BaseNightmareNPC boss) {
		super(id, tile, spawnedFor, boss);
		this.getCombat().setCombatDelay(2);
	}

	@Override
	public int attack(Entity target) {
		setAnimation(ATTACK_ANIMATION);
		delayHit(this, World.sendProjectile(this, target, PROJECTILE), target, ranged(target, getCombatDefinitions().getMaxHit()));
		return getCombatDefinitions().getAttackSpeed();
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		//TODO add check for crush later on.
		if (id == ID_PHOSANIS && hit.getHitType() == HitType.MELEE) {
			hit.setDamage(getHitpoints());
		} else {
			getBoss().setCrushHour(false);
		}

		super.handleIngoingHit(hit);
	}

}
