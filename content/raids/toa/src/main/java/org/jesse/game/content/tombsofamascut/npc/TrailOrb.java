package org.jesse.game.content.tombsofamascut.npc;

import org.jesse.game.content.tombsofamascut.encounter.AkkhaEncounter;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.jesse.game.content.tombsofamascut.encounter.AkkhaEncounter.QUADRANT_GFX_IDS;

/**
 * @author Savions.
 */
public class TrailOrb extends NPC {

	private final AkkhaEncounter encounter;
	private boolean finish;
	private int aliveTicks = 8;

	public TrailOrb(int id, Location tile, AkkhaEncounter encounter) {
		super(id, tile, true);
		this.encounter = encounter;
		setRadius(0);
		setForceAggressive(false);
	}

	@Override public void processNPC() {
		if (--aliveTicks <= 0 || finish) {
			finish();
			return;
		}
		final Player[] players = encounter.getChallengePlayers();
		Arrays.stream(players)
			.filter(Objects::nonNull)
			.filter(p -> getLocation().equals(p.getLocation()))
			.forEach(p -> {
				final var playersHit = new ArrayList<Player>();
				if (getLocation().equals(p.getLocation())) {
					playersHit.add(p);
					finish = true;
				}
				final var partyDamage = Math.floor(encounter.getParty().getDamageMultiplier() * 13);
				final var playerHit = Math.min(2, playersHit.size());
				final var baseDamage = (int) (partyDamage / playerHit) + Utils.random(2);

				p.applyHit(new Hit(encounter.getAkkha(), baseDamage, HitType.DEFAULT));
				p.getTemporaryAttributes().put("mark_akkha_trail_orb_explosion_" + id, true);
				finish = true;
			});
	}

	@Override protected void onFinish(Entity source) {
		super.onFinish(source);
		encounter.removeTrailOrb(this);
	}

	@Override public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) { return false; }

	@Override public void setRespawnTask() {}

	@Override public boolean canAttack(Player source) {
		return false;
	}

	@Override public boolean isEntityClipped() {
		return false;
	}
}
