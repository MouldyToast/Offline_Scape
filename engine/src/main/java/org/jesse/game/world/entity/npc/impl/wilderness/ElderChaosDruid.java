package org.jesse.game.world.entity.npc.impl.wilderness;

import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.ForceTalk;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;
import org.jesse.game.world.entity.player.action.combat.magic.CombatSpell;

public class ElderChaosDruid extends NPC implements CombatScript, Spawnable {
	private static final Graphics SPLASH = new Graphics(85, 0, 92);
	private static final ForceTalk TELEPORT_FORCETALK = new ForceTalk("You dare run from us!");
	private static final byte[][] OFFSETS = new byte[][] {new byte[] {1, 0}, new byte[] {-1, 0}, new byte[] {0, 1}, new byte[] {0, -1}};

	public ElderChaosDruid(int id, Location tile, Direction facing, int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (!getCombat().underCombat()) {
			return;
		}
		if (Utils.random(15) == 0) {
			final Entity target = getCombat().getTarget();
			if (target == null || !target.hasWalkSteps() || getLocation().withinDistance(target.getLocation(), 5) || target instanceof Player && ((Player) target).inArea("Wilderness: Chaos Temple")) {
				return;
			}
			final Location location = new Location(getLocation());
			Location teleportLocation = null;
			for (final byte[] offset : OFFSETS) {
				if (World.isFloorFree(teleportLocation = location.transform(offset[0], offset[1], 0), target.getSize())) {
					break;
				}
			}
			target.setLocation(teleportLocation);
			setForceTalk(TELEPORT_FORCETALK);
		}
	}

	@Override
	public int attack(Entity target) {
		final Projectile projectile = CombatSpell.WIND_WAVE.getProjectile();
		setAnimation(CombatSpell.WIND_WAVE.getAnimation());
		setGraphics(CombatSpell.WIND_WAVE.getCastGfx());
		CombatUtilities.delayHit(this, World.sendProjectile(this, target, projectile), target, new Hit(this, CombatUtilities.getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MAGIC, target), HitType.MAGIC).onLand(hit -> target.setGraphics(hit.getDamage() == 0 ? SPLASH : CombatSpell.WIND_WAVE.getHitGfx())));
		return getCombatDefinitions().getAttackSpeed();
	}

	@Override
	public boolean validate(int id, String name) {
		return name.equals("elder chaos druid");
	}
}
