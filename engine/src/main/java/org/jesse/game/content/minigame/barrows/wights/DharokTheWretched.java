package org.jesse.game.content.minigame.barrows.wights;

import org.jesse.game.content.minigame.barrows.BarrowsWightNPC;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;

import static org.jesse.game.npc.ids.NpcId.DI_DHAROK_THE_WRETCHED;

/**
 * @author Kris | 29. sept 2018 : 04:50:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class DharokTheWretched extends BarrowsWightNPC implements Spawnable, CombatScript {
	public DharokTheWretched(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public int attack(final Entity target) {
		final int maxHealth = getMaxHitpoints();
		final int health = getHitpoints();
		final int max = (int) (29.0F + (29.0F * ((float) (maxHealth - health) / maxHealth)));
		animate();
		executeMeleeHit(target, max);
		return combatDefinitions.getAttackSpeed();
	}

	@Override
	public boolean validate(final int id, final String name) {
        return switch (id) {
            case 1673, 16053, DI_DHAROK_THE_WRETCHED -> true;
            default -> false;
        };
	}

}
