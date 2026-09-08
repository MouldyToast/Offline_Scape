package org.jesse.game.content.minigame.pestcontrol;

import org.jesse.game.content.minigame.pestcontrol.npc.PestPortalNPC;
import org.jesse.game.content.minigame.pestcontrol.npc.VoidKnightNPC;
import org.jesse.game.util.Direction;
import org.jesse.game.util.ProjectileUtils;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;

import java.util.List;

/**
 * @author Kris | 26. juuni 2018 : 18:39:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public abstract class PestNPC extends NPC {
	public PestNPC(final PestControlInstance instance, final PestPortalNPC portal, final int id, final Location tile) {
		super(id, tile, Direction.SOUTH, 50);
		this.instance = instance;
		this.portal = portal;
		instance.addNPC(this);
		this.maxDistance = 64;
		aggressionDistance = 15;
		forceCheckAggression = true;
		this.supplyCache = false;
	}

	protected final PestControlInstance instance;
	protected final PestPortalNPC portal;

	@Override
	public void finish() {
		super.finish();
		instance.removeNPC(this);
	}

	@Override
	public void processHit(final Hit hit) {
		super.processHit(hit);
		if (hit.getSource() instanceof Player) {
			instance.addDamageDealt((Player) hit.getSource(), this, hit.getDamage());
		}
	}

	@Override
	public void setRespawnTask() {
	}

	@Override
	public boolean isTolerable() {
		return false;
	}

	@Override
	public List<Entity> getPossibleTargets(final EntityType type) {
		final VoidKnightNPC knight = instance.getVoidKnight();
		if (getLocation().withinDistance(knight, aggressionDistance) && !ProjectileUtils.isProjectileClipped(null, null, this, knight, combatDefinitions.isMelee())) {
			possibleTargets.clear();
			possibleTargets.add(knight);
			return possibleTargets;
		}
		return super.getPossibleTargets(type);
	}
}
