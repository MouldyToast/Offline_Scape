package org.jesse.game.content.boss.phantommuspah;

import org.jesse.game.world.entity.HitBar;

/**
 * @author Savions.
 */
public class ShieldHitBar extends HitBar {

	static final int MAX_HITPOINTS = 75;
	private int hitPoints = MAX_HITPOINTS;
	private boolean setRemove;

	@Override public int getType() {
		return 48;
	}

	@Override public int getPercentage() {
		return (int) Math.floor((float) 80 * hitPoints / (float) MAX_HITPOINTS);
	}

	int damage(int damage) {
		final int damageDone = Math.min(hitPoints, damage);
		hitPoints -= damageDone;
		return damageDone;
	}

	int getHitPoints() { return hitPoints; }
}