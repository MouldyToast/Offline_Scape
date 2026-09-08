package org.jesse.game.content.tombsofamascut.npc;

import org.jesse.game.world.entity.TargetSwitchCause;
import org.jesse.game.content.tombsofamascut.encounter.BabaEncounter;
import org.jesse.game.util.Direction;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.HitEntry;
import org.jesse.game.world.entity.InteractableEntity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPCCombat;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Savions.
 */
public class RollingBoulder extends TOANPC {

	private static final Animation ROLLING_ANIMATION = new Animation(9518);
	private static final float TEAM_HEALTH_FACTOR = .45F;
	private static final int UNCRACKED_ID = 11782;
	private static final int CRACKED_ID = 11783;
	private final BabaEncounter encounter;
	private final int level;
	private boolean finish;
	private final boolean cracked;

	public RollingBoulder(Location tile, BabaEncounter encounter, int level, boolean cracked) {
		super(cracked ? CRACKED_ID : UNCRACKED_ID, tile, Direction.WEST, 0, encounter, 0, false);
		this.encounter = encounter;
		this.level = level;
		this.cracked = cracked;
		this.combat = new NPCCombat(this) {
			@Override
			public void setTarget(final Entity target, TargetSwitchCause cause) { }
			@Override
			public void forceTarget(final Entity target) { }

		};
		setMaxHealth();
	}

	@Override public void sendDeath() {
		final Player source = getMostDamagePlayerCheckIronman();
		onDeath(source);
		onFinish(source);
	}

	@Override
	public void processHit(Hit hit) {
		hit.setDamage(Math.max(1, hit.getDamage()));
		super.processHit(hit);
		if (cracked)
			sendDeath();
	}

	@Override public void setMaxHealth() {
		if (this.id == CRACKED_ID) {
			combatDefinitions.setHitpoints(1);
			setHitpoints(1);
			return;
		}
		int maxHitPoints = 2;
		if (level >= 4)
			maxHitPoints += 4;
		else if (level >= 2)
			maxHitPoints += 2;
		else
			maxHitPoints += 1;

		maxHitPoints *= (1F + (TEAM_HEALTH_FACTOR * (toaRaidArea.getStartTeamSize() - 1)));
		combatDefinitions.setHitpoints(maxHitPoints);
		setHitpoints(maxHitPoints);
	}

	@Override public void processNPC() {
		if (!isDead() && !isFinished())
			encounter.checkRollingBoulderCollision(getLocation());
		setAnimation(ROLLING_ANIMATION);
		super.processNPC();
		final Location nextLocation = getLocation().transform(-1, 0);
		setForceMovement(new ForceMovement(new Location(getLocation()), 0, nextLocation, 30, Direction.WEST.getDirection()));
		setLocation(nextLocation);
		if (!World.isFloorFree(getLocation(), 1))
			finish();
	}

	@Override public boolean isAlwaysTakeMaxHit(HitType type) { return id == CRACKED_ID && HitType.RANGED.equals(type); }

	@Override public boolean isEntityClipped() { return false; }

	@Override public void setTarget(Entity target, TargetSwitchCause cause) {}

	@Override public void setFaceEntity(Entity entity) {}

	@Override public void setFacedInteractableEntity(InteractableEntity facedInteractableEntity) {}

	@Override public void setFaceLocation(Location tile) {}

	@Override public float getPointMultiplier() { return 0; }
}
