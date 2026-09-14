package org.jesse.game.content.boss.nex;

import org.jesse.game.content.boss.nex.npc.BloodReaver;
import org.jesse.game.util.Direction;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.Player;

public final class NexBloodReaver extends BloodReaver {

	public static Projectile soulSplitProjectile = new Projectile(2009, 120, 120, 0, 6, 60, 0, 0);
	public static final int ID = NpcId.BLOOD_REAVER_11294;

	private final NexNPC nex;

	public NexBloodReaver(Location tile, Direction facing, int radius, NexNPC nex) {
		super(ID, tile, facing, radius);
		this.nex = nex;
		this.spawned = true;
	}

	public int healNex() {
		if (nex == null || isDead() || isFinished())
			return 0;
		sendDeath();
		soulSplitProjectile.build(this, nex);
		return getHitpoints();
	}

	/**
	 * Override this to ignore protective items.
	 */
	@Override
	public boolean isAcceptableTarget(Entity target) {
		return target instanceof Player;
	}
}
