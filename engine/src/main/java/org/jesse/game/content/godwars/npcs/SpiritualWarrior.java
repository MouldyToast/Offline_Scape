package org.jesse.game.content.godwars.npcs;

import org.jesse.game.content.achievementdiary.diaries.WildernessDiary;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.CombatScriptsHandler;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Christopher
 * @since 3/9/2020
 */
public class SpiritualWarrior extends SpawnableKillcountNPC implements CombatScript {

	private static final Animation specialAnimation = new Animation(1132);
	private static final Graphics specialGraphic = new Graphics(1103);

	protected SpiritualWarrior(int id, Location tile, Direction facing, int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public int attack(Entity target) {
		if (id == NpcId.SPIRITUAL_WARRIOR_11290 && Utils.randomBoolean(8)) {
			setAnimation(specialAnimation);
			special(target, target.getLocation().copy(), 2);
			return 10;
		}

		return CombatScriptsHandler.DEFAULT_SCRIPT.attack(this, target);
	}

	private void special(Entity entity, Location location, int dist) {
		if (dist <= 0) {
			World.sendGraphics(specialGraphic, location);
			if (entity.getLocation().matches(location)) {
				entity.scheduleHit(this, new Hit(Utils.random(40), HitType.REGULAR), -1);
			}
			return;
		}

		Location southWest = location.transform(Direction.SOUTH_WEST, dist);
		Location northWest = location.transform(Direction.NORTH_WEST, dist);
		Location northEast = location.transform(Direction.NORTH_EAST, dist);
		Location southEast = location.transform(Direction.SOUTH_EAST, dist);
		World.sendGraphics(specialGraphic, southWest);
		World.sendGraphics(specialGraphic, northWest);
		World.sendGraphics(specialGraphic, northEast);
		World.sendGraphics(specialGraphic, southEast);
		WorldTasksManager.schedule(() -> special(entity, location, dist - 1), 1);
	}

	@Override
	public void onDeath(Entity source) {
		super.onDeath(source);
		if (source instanceof final Player player) {
			player.getAchievementDiaries().update(WildernessDiary.KILL_A_SPIRITUAL_WARRIOR);
		}
	}

	@Override
	public boolean canBeMulticannoned(@NotNull Player player) {
		return false;
	}

	@Override
	public boolean validate(int id, String name) {
		return id == NpcId.SPIRITUAL_WARRIOR ||
				id == NpcId.SPIRITUAL_WARRIOR_2243 ||
				id == NpcId.SPIRITUAL_WARRIOR_3159 ||
				id == NpcId.SPIRITUAL_WARRIOR_3166 ||
				id == NpcId.SPIRITUAL_WARRIOR_11290;
	}

}
