package org.jesse.game.content.colosseum;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.WorldThread;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;

public class LaserOrb extends NPC {

	private static final int PROJECTILE_DELAY_MOD = 2;
	private static final int END_ATTACK_GFX = 2697;
	private static final Animation SPAWN_ANIM = new Animation(10798);
	private static final Animation CHARGE_ANIM = new Animation(10801);
	private static final Animation ATTACK_ANIM = new Animation(10802);

	private final ColosseumInstance instance;
	private final Player target;
	private final SpawnData spawnData;
	private final Location[] walkEdges;
	private int walkPos = 0;
	private int fireLaunchTick = -1;

	public LaserOrb(ColosseumInstance instance, SpawnData spawnData) {
		super(12824, resolveSpawnLocation(instance, spawnData), Direction.SOUTH, 0, true);
		this.maxDistance = 64;
		this.instance = instance;
		this.target = instance.getPlayer();
		this.spawnData = spawnData;
		this.walkEdges = resolveWalkEdges(instance, spawnData);
		this.setCrawling(true);
	}

	@Override
	public NPC spawn() {
		setAnimation(SPAWN_ANIM);
		lock(4);
		Location walkStart = walkEdges[walkPos];
		addWalkSteps(walkStart.getX(), walkStart.getY());
		return super.spawn();
	}

	@Override
	public void processNPC() {
		if (!isLocked() && getLocation().matches(walkEdges[walkPos])) {
			walkPos++;
			if (walkPos >= walkEdges.length) {
				walkPos = 0;
			}
			Location walkStart = walkEdges[walkPos];
			addWalkSteps(walkStart.getX(), walkStart.getY());
		}

		super.processNPC();
	}

	@Override
	public void processMovement() {
		super.processMovement();

		if (fireLaunchTick == WorldThread.getCurrentCycle()) {
			fire();
		}
	}

	public void queueFire() {
		fireLaunchTick = (int) WorldThread.getCurrentCycle() + ((crawlInterval & 0x1) == 1 ? 1 : 2);
		lock();//Don't process any more movements for now!
	}

	private void fire() {
		lock(5);
		setCrawling(true);//Reset crawling interval so it immediately starts crawling again after lock ends
		Location startLocation = getLocation().transform(spawnData.shootDirection);
		instance.fillLine(startLocation.getX(), startLocation.getY(), spawnData.shootDirection, 13, (tile, n) -> World.sendGraphics(new Graphics(spawnData.chargeGraphics, (n * PROJECTILE_DELAY_MOD), 128), tile), null);
		setAnimation(CHARGE_ANIM);
		WorldTasksManager.schedule(() -> {
			instance.fillLine(startLocation.getX(), startLocation.getY(), spawnData.shootDirection, 13, (tile, n) -> {
				World.sendGraphics(new Graphics(spawnData.attackGraphics, (n * PROJECTILE_DELAY_MOD), 128), tile);
				if (target.getLocation().matches(tile)) {
					target.applyHit(new Hit(this, 60 + Utils.random(20), HitType.REGULAR));
				}
			}, (tile, n) -> World.sendGraphics(new Graphics(END_ATTACK_GFX, (n * PROJECTILE_DELAY_MOD), 80), tile));
			setAnimation(ATTACK_ANIM);
		}, 3);
	}

	@Override
	public boolean isEntityClipped() {
		return false;
	}

	private static Location resolveSpawnLocation(ColosseumInstance instance, SpawnData spawnData) {
		Location sw = instance.getArenaSw();
		Location ne = instance.getArenaNe();
		return switch (spawnData) {
			case NORTH -> new Location(sw.getX() + 1, ne.getY());//spawns on the north west pillar east side
			case EAST -> new Location(ne.getX(), sw.getY() + 2);//spawns 2 tiles above the south east pillar
			case SOUTH -> new Location(sw.getX() + 1, sw.getY());//spawns on the south west pillar east side
			case WEST -> new Location(sw.getX(), sw.getY() + 1);//spawns on the south west pillar west side
		};
	}

	private static Location[] resolveWalkEdges(ColosseumInstance instance, SpawnData spawnData) {
		Location sw = instance.getArenaSw();
		Location ne = instance.getArenaNe();
		return switch (spawnData) {
			case NORTH ->
					new Location[]{new Location(ne.getX() - 1, ne.getY()), new Location(sw.getX() + 1, ne.getY())};
			case EAST -> new Location[]{new Location(ne.getX(), ne.getY() - 1), new Location(ne.getX(), sw.getY() + 1)};
			case SOUTH ->
					new Location[]{new Location(ne.getX() - 1, sw.getY()), new Location(sw.getX() + 1, sw.getY())};
			case WEST -> new Location[]{new Location(sw.getX(), ne.getY() - 1), new Location(sw.getX(), sw.getY() + 1)};
		};
	}

	public enum SpawnData {
		NORTH(Direction.SOUTH, 2689, 2693),
		EAST(Direction.WEST, 2690, 2694),
		SOUTH(Direction.NORTH, 2691, 2695),
		WEST(Direction.EAST, 2692, 2696);

		public static SpawnData[] values = values();

		private final Direction shootDirection;
		private final int chargeGraphics, attackGraphics;

		SpawnData(Direction shootDirection, int chargeGraphics, int attackGraphics) {
			this.shootDirection = shootDirection;
			this.chargeGraphics = chargeGraphics;
			this.attackGraphics = attackGraphics;
		}

	}

}
