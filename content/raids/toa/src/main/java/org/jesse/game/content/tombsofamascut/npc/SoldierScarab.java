package org.jesse.game.content.tombsofamascut.npc;

import org.jesse.game.content.tombsofamascut.encounter.KephriEncounter;
import org.jesse.game.content.tombsofamascut.raid.EncounterStage;
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
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.npc.combatdefs.AttackType;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Savions.
 */
public class SoldierScarab extends TOANPC implements CombatScript {

	private static final int ID = 11724;
	private static final Animation SPAWN_ANIM = new Animation(9589);
	private static final Animation ATTACK_ANIM = new Animation(9587);
	private static final Projectile HEAL_PROJECTILE = new Projectile(2150, 100, 248, 0, 2, 30, 30, 0);
	private static final Location SPAWN_LOC = new Location(3556, 5407);
	private final KephriEncounter encounter;
	private int healTicks = 13;

	public SoldierScarab(KephriEncounter encounter) {
		super(ID, encounter.getLocation(SPAWN_LOC), Direction.WEST, 0, encounter);
		this.encounter = encounter;
		setRandomTarget();
		setMaxDistance(48);
		setAggressionDistance(48);
		getCombat().setCombatDelay(getCombat().getCombatDelay() + 4);
	}

	@Override public NPC spawn() {
		setAnimation(SPAWN_ANIM);
		return super.spawn();
	}

	@Override public void processNPC() {
		super.processNPC();
		if (!isDying() && !isFinished() && EncounterStage.STARTED.equals(encounter.getStage())
				&& encounter.getKephri() != null && !encounter.getKephri().isDying() && !encounter.getKephri().isFinished() && healTicks > 0) {
			if (getCombat().getTarget() == null || getCombat().getTarget().isDying() || getCombat().getTarget().isFinished() || !encounter.insideChallengeArea(getCombat().getTarget())) {
				setRandomTarget();
			}
			if (--healTicks == 1) {
				World.sendProjectile(this, encounter.getKephri(), HEAL_PROJECTILE);
			} else if (healTicks <= 0) {
				healTicks = 6;
				encounter.getKephri().healBoss(this, getHitpoints() * 1F / getMaxHitpoints(), .05F, .07F);
			}
		}
	}

	private void setRandomTarget() {
		final Player[] players = encounter.getChallengePlayers();
		if (players.length > 0) {
			getCombat().setTarget(players[Utils.random(players.length - 1)]);
		}
	}

	@Override public void autoRetaliate(Entity source) {}

	@Override public void setRespawnTask() { }

	@Override public boolean isIntelligent() { return true; }

	@Override public float getPointMultiplier() {
		return .5F;
	}

	@Override public int attack(Entity target) {
		setAnimation(ATTACK_ANIM);
		delayHit(0, target, new Hit(this, getRandomMaxHit(this, encounter.getKephri().getMaxHit(17), AttackType.SLASH, target), HitType.MELEE));
		return 6;
	}
}
