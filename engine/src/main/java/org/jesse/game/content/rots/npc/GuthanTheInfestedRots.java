package org.jesse.game.content.rots.npc;

import org.jesse.game.content.rots.RotsInstance;
import org.jesse.game.util.Utils;
import org.jesse.game.world.WorldThread;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.npc.combat.CombatScript;

import static org.jesse.game.world.entity.player.action.combat.SpecialAttackScript.SHOVE_GFX;

public class GuthanTheInfestedRots extends RotsBrother implements CombatScript {

	private static final Graphics GUTHANS_GFX = new Graphics(398);
	private long shoveCap;

	public GuthanTheInfestedRots(final Location tile, RotsInstance instance) {
		super(16037, tile, instance);
	}

	@Override
	public int attack(final Entity target) {
		if (WorldThread.getCurrentCycle() >= shoveCap && Utils.randomBoolean(11)) {
			shoveCap = WorldThread.getCurrentCycle() + 10;
			setAnimation(new Animation(1064));
			setGraphics(new Graphics(253, 0, 96));
			setForceTalk("Stay!");

			int x = target.getX();
			int y = target.getY();
			int px = getX();
			int py = getY();
			if (px > target.getX()) {
				x--;
			} else if (px < target.getX()) {
				x++;
			}
			if (py > target.getY()) {
				y--;
			} else if (py < target.getY()) {
				y++;
			}
			setFaceLocation(target.getLocation());
			target.getWalkSteps().clear();
			target.setAnimation(null);
			target.performDefenceAnimation(this);
			target.addWalkSteps(x, y, 1, true);
			target.lock(5);
			target.setGraphics(SHOVE_GFX);
			return combatDefinitions.getAttackSpeed();
		}

		animate();
		this.delayHit(0, target, melee(target, combatDefinitions.getMaxHit()).onLand(hit -> {
			if (Utils.random(3) == 0) {
				setGraphics(GUTHANS_GFX);
				if (hit.getDamage() > 0) {
					heal(hit.getDamage());
				}
			}
		}));
		return combatDefinitions.getAttackSpeed();
	}

}
