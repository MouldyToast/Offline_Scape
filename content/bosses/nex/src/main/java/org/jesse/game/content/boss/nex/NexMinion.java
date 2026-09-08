package org.jesse.game.content.boss.nex;

import org.jesse.game.content.godwars.GodType;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.magic.CombatSpell;

public class NexMinion extends NPC implements CombatScript {

	public static final Animation attackAnimation = new Animation(1979);
	private boolean immune = true;
	private final NexNPC nex;

	protected NexMinion(int id, Location tile, Direction facing, NexNPC nex) {
		super(id, tile, facing, 0);
		setSpawned(true);
		this.nex = nex;
	}

	public boolean isImmune() {
		return immune;
	}

	public void removeImmunity() {
		this.immune = false;
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);

		if (immune) {
			hit.setDamage(0);
			Entity source = hit.getSource();
			if (source instanceof Player) {
				((Player) source).sendMessage(getName() + " is currently immune to your attacks.");
			}
		}
	}

	public void enableAggression() {
		setForceAggressive(true);
		setAggressionDistance(64);
		setMaxDistance(64);
	}

	@Override
	public void autoRetaliate(Entity source) {
		if (immune) return;
		super.autoRetaliate(source);
	}

	@Override
	protected boolean isMovableEntity() {
		return false;
	}

	@Override
	public boolean isTolerable() {
		return false;
	}

	public NexNPC getNex() {
		return nex;
	}

	@Override
	public int attack(Entity target) {
		useSpell(getSpell(), target, getCombatDefinitions().getAttackDefinitions().getMaxHit());
		return getCombatDefinitions().getAttackSpeed();
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		if (!(source instanceof final Player player)) {
			return;
		}
		GodType.ANCIENT.addKillcount(player, 3);
	}

	public CombatSpell getSpell() {
		return null;
	}

	@Override
	public boolean checkProjectileClip(final Player player, boolean melee) {
		if (melee) return false;
		return super.checkProjectileClip(player, false);
	}

}
