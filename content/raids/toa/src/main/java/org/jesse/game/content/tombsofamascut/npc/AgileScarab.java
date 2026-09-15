package org.jesse.game.content.tombsofamascut.npc;

import org.jesse.game.content.tombsofamascut.encounter.KephriEncounter;
import org.jesse.game.content.tombsofamascut.raid.EncounterStage;
import org.jesse.game.util.Direction;
import org.jesse.game.util.ProjectileUtils;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.npc.combatdefs.AttackType;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Savions.
 */
public class AgileScarab extends TOANPC implements CombatScript {

	private static final int AGILE_SCARAB_NPC_ID = 11727;
	private static final Animation ATTACK_ANIM = new Animation(9594);
	private static final Projectile ATTACK_PROJECTILE = new Projectile(2152, 40, 124, 60, 16, 2, 64, 5);
	private final KephriEncounter encounter;
	private int switchTargetTicks = 20;
	private Location lastTargetLocation;

	public AgileScarab(Location tile, KephriEncounter encounter) {
		super(AGILE_SCARAB_NPC_ID, tile, Direction.SOUTH, 64, encounter);
		this.encounter = encounter;
		setForceAggressive(true);
		setAttackDistance(6);
		setMaxDistance(48);
		setDeathDelay(3);
		setRun(true);
		forceCheckAggression = true;
	}

	@Override public boolean checkAggressivity() {
		if (EncounterStage.STARTED.equals(encounter.getStage())) {
			if (switchTargetTicks > 0 && combat.getTarget() != null) {
				switchTargetTicks--;
				if (combat.getTarget() != null && !combat.getTarget().isDead() && !combat.getTarget().isFinished()
						&& encounter.insideChallengeArea(combat.getTarget())) {
					return true;
				}
			} else if (combat.getTarget() == null) {
				final Player[] players = encounter.getChallengePlayers();
				if (players.length > 0) {
					getCombat().setTarget(players[Utils.random(players.length - 1)]);
				}
				return true;
			}
		}
		return false;
	}

	@Override protected void onFinish(Entity source) {
		super.onFinish(source);
		if (encounter.getKephri() != null) {
			encounter.getKephri().removeAgileScarab(this);
		}
	}

	@Override public void autoRetaliate(Entity source) {}

	@Override public void processNPC() {
		super.processNPC();
		checkAggressivity();
		if (combat.getTarget() != null) {
			if (lastTargetLocation != null && combat.getTarget().getLocation().equals(lastTargetLocation) && !hasWalkSteps()) {
				final int currentDistance = getLocation().getTileDistance(lastTargetLocation);
				if (currentDistance <= 4) {
					final int dx = getX() - getCombat().getTarget().getX();
					final int dy = getY() - getCombat().getTarget().getY();
					if (dx != 0 || dy != 0) {
						final boolean biggerDx = Math.abs(dx) > Math.abs(dy);
						final Direction direction = Direction.getDirection(biggerDx ? dx : 0, biggerDx ? 0 : dy);
						for (int i = -1; i < 2; i++) {
							final Direction testDirection = Direction.values[(direction.ordinal() + Direction.values.length + i) % Direction.values.length];
							if (addWalkStepsInteract(getX() + testDirection.getOffsetX() * 2, getY() + testDirection.getOffsetY() * 2, 3, 0, true)
									&& ProjectileUtils.isProjectileClipped(null, null, getLocation().transform(getX() + testDirection.getOffsetX() * 2, getY() + testDirection.getOffsetY() * 2),
									lastTargetLocation, false)) {
								break;
							}
							resetWalkSteps();
						}
					}
				} else if (!ProjectileUtils.isProjectileClipped(null, null, this, lastTargetLocation, false)) {
					switchTargetTicks = 0;
				}
			}
			lastTargetLocation = combat.getTarget().getLocation();
		}
	}

	@Override public float getPointMultiplier() {
		return .5F;
	}

	@Override public int attack(Entity target) {
		setAnimation(ATTACK_ANIM);
		World.sendProjectile(this, target, ATTACK_PROJECTILE);
		delayHit(-1, target, new Hit(this, getRandomMaxHit(this, getMaxHit(5), AttackType.RANGED, target), HitType.RANGED));
		return 4;
	}
}
