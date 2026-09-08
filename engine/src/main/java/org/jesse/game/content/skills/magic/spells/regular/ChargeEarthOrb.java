package org.jesse.game.content.skills.magic.spells.regular;

import org.jesse.game.content.skills.magic.Spellbook;
import org.jesse.game.content.skills.magic.actions.ChargeOrbSpell.ChargeOrbSpellData;
import org.jesse.game.content.skills.magic.spells.ObjectSpell;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.skills.ChargeOrbSpellD;

/**
 * @author Kris | 8. juuli 2018 : 20:25:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ChargeEarthOrb implements ObjectSpell {

	@Override
	public int getDelay() {
		return 1000;
	}

	@Override
	public boolean spellEffect(final Player player, final WorldObject object) {
        if (!object.getName().equals("Obelisk of Earth")) {
            player.sendMessage("Nothing interesting happens.");
            return false;
        }
		player.getDialogueManager().start(new ChargeOrbSpellD(player, ChargeOrbSpellData.CHARGE_EARTH_ORB, this));
		return false;
	}
	
	@Override
	public Spellbook getSpellbook() {
		return Spellbook.NORMAL;
	}

}
