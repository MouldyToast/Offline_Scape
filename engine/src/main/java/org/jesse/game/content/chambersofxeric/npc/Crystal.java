package org.jesse.game.content.chambersofxeric.npc;

import org.jesse.game.world.entity.TargetSwitchCause;
import org.jesse.game.content.chambersofxeric.Raid;
import org.jesse.game.content.chambersofxeric.room.VasaNistirioRoom;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.NPCCombat;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 10. jaan 2018 : 23:56.51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class Crystal extends RaidNPC<VasaNistirioRoom> {
	public Crystal(final Raid raid, final VasaNistirioRoom room, final int id, final Location tile, final int index) {
		super(raid, room, id, tile);
		combat = new CrystalCombatHandler(this);
		this.index = index;
	}

	private final int index;

	@Override
	public void sendDeath() {
		finish();
		final WorldObject crystal = room.getCrystalObjects()[index];
		World.spawnObject(room.getCrystalObjects()[index] = new WorldObject(29775, crystal.getType(), crystal.getRotation(), crystal));
	}

	@Override
	public void processNPC() {
	}

	@Override
	public void applyHit(final Hit hit) {
		super.applyHit(hit);
		if (room.getVasa().cannotHit(index)) {
			hit.setDamage(0);
			if (hit.getSource() == null) {
				return;
			}
			if (hit.getSource().getEntityType() == EntityType.PLAYER) {
				((Player) hit.getSource()).sendMessage("The crystal is currently invulerable to damage.");
			}
		}
		if (hit.getHitType() != HitType.MELEE) {
			hit.setDamage(0);
		}
	}

	@Override
	public float getXpModifier(final Hit hit) {
		if (room.getVasa().cannotHit(index) || hit.getHitType() != HitType.MELEE) {
			return 0;
		}
		return 1;
	}

	@Override
	public boolean checkProjectileClip(final Player player, boolean melee) {
		return false;
	}


	private static final class CrystalCombatHandler extends NPCCombat {
		CrystalCombatHandler(final NPC npc) {
			super(npc);
		}

		@Override
		public void setTarget(final Entity target, TargetSwitchCause cause) {
		}
	}
}
