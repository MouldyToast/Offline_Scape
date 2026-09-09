package org.jesse.game.content.minigame.barrows.wights;

import org.jesse.game.content.minigame.barrows.BarrowsWightNPC;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.SkillConstants;


/**
 * @author Kris | 29. sept 2018 : 06:11:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class KarilTheTainted extends BarrowsWightNPC implements Spawnable, CombatScript {

	private static final Graphics GFX = new Graphics(401, 0, 96);
	private static final Projectile PROJ = new Projectile(27, 42, 30, 40, 15, 3, 64, 5);

	public KarilTheTainted(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public int attack(final Entity target) {
		animate();
        this.delayHit(World.sendProjectile(this, target, PROJ), target, ranged(target, combatDefinitions.getMaxHit()).onLand(hit -> {
            if (hit.getDamage() > 0) {
                if (Utils.random(3) == 0) {
                    target.setGraphics(GFX);
                    target.drainSkill(SkillConstants.AGILITY, 20F);
                }
            }
        }));
		return combatDefinitions.getAttackSpeed();
	}

	@Override
	public boolean validate(final int id, final String name) {
		switch (id) {
			case 1675: {
				return true;
			}
			default:
				return false;
		}
	}

}
