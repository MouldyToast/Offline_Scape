package org.jesse.game.content.minigame.fightcaves.npcs;

import org.jesse.game.content.minigame.fightcaves.FightCaves;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.ImmutableLocation;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 8. nov 2017 : 21:14.46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
final class TzKek extends FightCavesNPC implements CombatScript {
	private static final SoundEffect attackSound = new SoundEffect(595);

	TzKek(final TzHaarNPC npc, final Location tile, final FightCaves caves) {
		super(npc, tile, caves);
		for (int i = 0; i < 2; i++) {
			minions.add(new SplitTzKek(tile, caves));
		}
	}

	private final List<SplitTzKek> minions = new ArrayList<>(2);

	@Override
	public final void applyHit(final Hit hit) {
		super.applyHit(hit);
		if (hit.getHitType() != HitType.MELEE || hit.getSource() == null) {
			return;
		}
		CombatUtilities.delayHit(this, -1, hit.getSource(), new Hit(this, 1, HitType.REGULAR));
	}

	@Override
	public void onFinish(final Entity source) {
		super.onFinish(source);
		for (int i = 0; i < minions.size(); i++) {
			final SplitTzKek npc = minions.get(i);
			npc.setRespawnTile(new ImmutableLocation(i == 0 ? getLocation() : shiftLocation(getLocation())));
			npc.spawn();
			npc.faceEntity(caves.getPlayer());
			WorldTasksManager.schedule(() -> npc.getCombat().setTarget(caves.getPlayer()));
		}
	}

	private Location shiftLocation(final Location tile) {
		if (World.isTileFree(tile.getX() + 1, tile.getY(), getPlane(), 1)) {
			tile.moveLocation(1, 0, 0);
		} else if (World.isTileFree(tile.getX() - 1, tile.getY(), getPlane(), 1)) {
			tile.moveLocation(-1, 0, 0);
		} else if (World.isTileFree(tile.getX(), tile.getY() - 1, getPlane(), 1)) {
			tile.moveLocation(0, -1, 0);
		} else if (World.isTileFree(tile.getX(), tile.getY() + 1, getPlane(), 1)) {
			tile.moveLocation(0, 1, 0);
		}
		return tile;
	}

	@Override
	public int attack(Entity target) {
		animate();
		playSound(attackSound);
		delayHit(0, target, melee(target, combatDefinitions.getMaxHit()));
		return combatDefinitions.getAttackSpeed();
	}
}
