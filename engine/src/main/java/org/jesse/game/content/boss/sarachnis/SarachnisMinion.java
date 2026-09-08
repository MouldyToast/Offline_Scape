package org.jesse.game.content.boss.sarachnis;

import org.jesse.game.util.AnimationUtil;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.combatdefs.NPCCombatDefinitions;

public class SarachnisMinion extends NPC {

	private final Sarachnis sarachnis;

	public SarachnisMinion(int id, Location tile, Direction facing, int radius, Sarachnis sarachnis) {
		super(id, tile, facing, radius);
		setSpawned(false);
		setForceAggressive(true);
		this.sarachnis = sarachnis;
	}

	@Override
	public NPCCombatDefinitions getCombatDefinitions() {
		NPCCombatDefinitions defs = super.getCombatDefinitions();
		defs.getSpawnDefinitions().setDeathAnimation(new Animation(8318));
		final Animation death = defs.getSpawnDefinitions().getDeathAnimation();
		if (death != null) {
			deathDelay = Math.max(Math.min((int) Math.ceil(AnimationUtil.getDuration(death) / 1200.0F), 10), 1);
		}
		return defs;
	}

	@Override
	public void setRespawnTask() {

	}

	@Override
	protected void onDeath(Entity source) {
		super.onDeath(source);

		sarachnis.getSpawns().remove(this);
	}

}
