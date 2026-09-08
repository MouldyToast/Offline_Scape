package org.jesse.game.world.entity.player.action.combat.ranged;

import org.jesse.game.model.item.degradableitems.DegradeType;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.RangedCombat;
import org.jesse.game.world.region.area.wilderness.WildernessArea;

/**
 * @author Kris | 29. juuli 2018 : 03:52:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class CrawsBowCombat extends RangedCombat {

    public CrawsBowCombat(final Entity target) {
        super(target);
	}

	@Override
	protected void dropAmmunition(final int delay, final boolean destroy) {
	    if (player.getWeapon().getCharges() > 1000) {
            player.getChargesManager().removeCharges(DegradeType.OUTGOING_HIT);
        }
	}

	@Override
	public int getMaxHit(final Player player, final double specialModifier, double activeModifier, final boolean ignorePrayers) {
	    return super.getMaxHit(player,
                specialModifier * (isBoosted() ? 1.8F : 1F), activeModifier, ignorePrayers);
	}

	@Override
	public int getAccuracy(final Player player, final Entity target, final double resultModifier) {
		return super.getAccuracy(player, target, resultModifier * (isBoosted() ? 1.8F : 1F));
	}

	private boolean isBoosted() {
	    return player.getWeapon().getCharges() > 1000 && target instanceof NPC && WildernessArea.isWithinWilderness(target.getX(),
                target.getY());
    }

}
