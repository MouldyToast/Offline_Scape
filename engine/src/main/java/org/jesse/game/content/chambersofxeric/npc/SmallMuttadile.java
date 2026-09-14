package org.jesse.game.content.chambersofxeric.npc;

import org.jesse.game.content.chambersofxeric.room.MuttadileRoom;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.npc.NPCCombat;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.npc.combatdefs.SpawnDefinitions;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 12. jaan 2018 : 19:30.31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class SmallMuttadile extends RaidNPC<MuttadileRoom> implements CombatScript {
	private static final Animation ANIM = new Animation(7420);

	public SmallMuttadile(final MuttadileRoom room, final Location tile) {
		super(room.getRaid(), room, 7562, tile);
		this.room = room;
		combat = new SmallMuttadileCombatHandler(this);
		setForceMultiArea(true);
		setForceAggressive(true);
		setAggressionDistance(20);
		setAttackDistance(6);
		radius = 32;
	}

	private final MuttadileRoom room;
	private boolean feeding;
	private int count = 3;
	private int feedCount = 3;
	private int bites = -1;
	private int ticks = 5;
	private int safespotTicks;
	private int safespotDelay;

	@Override
	public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) {
		if (isFrozen()) {
			return false;
		}
		return super.addWalkStep(nextX, nextY, lastX, lastY, check);
	}

	@Override
	public float getXpModifier(final Hit hit) {
		return feeding ? 0 : 1;
	}

	@Override
	public void processNPC() {
		safespotDelay--;
		if (feedCount > 0 && !feeding && !room.getTree().isDead() && !room.getTree().isFinished() && count > 0 && getHitpoints() < getMaxHitpoints() / 2 && Utils.random(2) == 0) {
			feeding = true;
			room.setHasFed();
			combat.removeTarget();
			setFaceEntity(room.getTree());
			if (!isFrozen()) {
				calcFollow(room.getTree(), -1, true, true, true);
			}
			count--;
		} else if (feeding) {
			if (!isFrozen()) {
				calcFollow(room.getTree(), -1, true, true, true);
			}
			if (!Utils.isOnRange(getX(), getY(), getSize(), room.getTree().getX(), room.getTree().getY(), room.getTree().getSize(), 0)) {
				if (!hasWalkSteps() && safespotDelay <= 0) {
					feeding = false;
					return;
				}
				return;
			}
			if (bites == 0) {
				bites = -1;
				stopFeeding();
				safespotDelay = 0;
				return;
			}
			if (ticks-- > 0) {
				return;
			}
			if (!hasWalkSteps()) {
				if (bites == -1) {
					feedCount--;
					bites = getHitpoints() < (getMaxHitpoints() * 0.4F) ? 3 : 2;
				}
				bites--;
				setAnimation(ANIM);
				ticks = 3;
				final int amount = (int) (getMaxHitpoints() * 0.1F);
				heal(amount);
				safespotDelay = ticks;
			}
		} else {
			super.processNPC();
			if (safespotTicks++ % 3 == 0) {
				final Entity target = combat.getTarget();
				if (target == null || hasWalkSteps() || !isProjectileClipped(target, false)) {
					return;
				}
				resetWalkSteps();
				final Location dest = getFaceLocation(target, 15, 1024);
				calcFollow(dest, 5, true, false, false);
				safespotDelay = randomWalkDelay = 5;
				combat.removeTarget();
				WorldTasksManager.schedule(() -> {
					if (combat.getTarget() == null) {
						combat.setTarget(target);
					}
				}, safespotDelay - 1);
			}
		}
	}

	@Override
	public boolean isAcceptableTarget(final Entity entity) {
		return safespotDelay <= 0;
	}

	private void stopFeeding() {
		feeding = false;
		setFaceEntity(null);
		checkAggressivity();
	}

	@Override
	public void sendDeath() {
		final Player source = getMostDamagePlayerCheckIronman();
		onDeath(source);
		resetWalkSteps();
		combat.removeTarget();
		setAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			private int loop;
			@Override
			public void run() {
				if (loop == 0) {
					final SpawnDefinitions spawnDefinitions = combatDefinitions.getSpawnDefinitions();
					setAnimation(spawnDefinitions.getDeathAnimation());
					final SoundEffect sound = spawnDefinitions.getDeathSound();
					if (sound != null && source != null) {
						source.sendSound(new SoundEffect(sound.getId(), 10, 0));
					}
				} else if (loop == 1) {
					drop(getMiddleLocation());
					room.unleashLargeMuttadile();
					reset();
					finish();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	@Override
	public void onDrop(@NotNull final Player killer) {
        super.onDrop(killer);
		room.getRaid().sendGlobalMessage("As the Muttadile is slain, supplies are dropped for " + killer.getName() + ".");
	}

	@Override
	public void autoRetaliate(final Entity source) {
		if (safespotDelay > 0) {
			return;
		}
		super.autoRetaliate(source);
	}

	private static final Projectile proj = new Projectile(1291, 92, 100, 15, 5, 28, 0, 5);
	private static final Animation attackAnim = new Animation(7421);
	private static final Animation meleeAnim = new Animation(7420);
	private static final SoundEffect rangedSound = new SoundEffect(385, 5, -1);
	private static final SoundEffect meleeSound = new SoundEffect(2548, 5, 0);

	@Override
	public double getRangedPrayerMultiplier() {
		return 0.33;
	}

	@Override
	public int attack(final Entity target) {
		final boolean meleeDist = isWithinMeleeDistance(this, target);
		if (meleeDist && Utils.random(7) >= 2) {
			setAnimation(meleeAnim);
			World.sendSoundEffect(getMiddleLocation(), meleeSound);
			delayHit(this, 0, target, melee(target, (int) (47 * (room.getRaid().isChallengeMode() ? 1.5F : 1.0F))));
			return combatDefinitions.getAttackSpeed();
		}
		if (!meleeDist && Utils.random(5) > 3) {
			WorldTasksManager.schedule(() -> calcFollow(target, Utils.random(1, 3), true, false, true), 1);
		}
		setAnimation(attackAnim);
		World.sendSoundEffect(target.getLocation(), new SoundEffect(rangedSound.getId(), rangedSound.getRadius(), proj.getProjectileDuration(this.getLocation(), target.getLocation())));
		delayHit(this, World.sendProjectile(this, target, proj), target, ranged(target, (int) (30 * (room.getRaid().isChallengeMode() ? 1.5F : 1.0F))));
		return combatDefinitions.getAttackSpeed();
	}


	private static final class SmallMuttadileCombatHandler extends NPCCombat {
		SmallMuttadileCombatHandler(final SmallMuttadile npc) {
			super(npc);
		}

		@Override
		public boolean checkAll() {
			if (((SmallMuttadile) npc).feeding) {
				return false;
			}
			return super.checkAll();
		}
	}
}
