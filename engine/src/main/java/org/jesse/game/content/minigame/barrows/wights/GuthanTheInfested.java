package org.jesse.game.content.minigame.barrows.wights;

import org.jesse.game.content.minigame.barrows.BarrowsWightNPC;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;


/**
 * @author Kris | 29. sept 2018 : 05:29:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class GuthanTheInfested extends BarrowsWightNPC implements Spawnable, CombatScript {

	private static final Graphics GUTHANS_GFX = new Graphics(398);

	public GuthanTheInfested(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public int attack(final Entity target) {
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

	@Override
	public boolean validate(final int id, final String name) {
		switch (id) {
			case 1674:
			case 16054: {
				return true;
			}
			default:
				return false;
		}
	}

}
