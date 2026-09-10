package org.jesse.game.content.skills.magic.spells.regular;

import org.jesse.game.content.skills.magic.Spellbook;
import org.jesse.game.content.skills.magic.actions.Teleother;
import org.jesse.game.content.skills.magic.spells.NPCSpell;
import org.jesse.game.content.skills.magic.spells.PlayerSpell;
import org.jesse.game.content.skills.magic.spells.teleports.TeleportCollection;
import org.jesse.game.model.ui.InterfacePosition;
import org.jesse.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 16. juuli 2018 : 00:47:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class TeleotherFalador implements PlayerSpell, NPCSpell {

	private static final Animation ANIM = new Animation(1818);
	private static final Graphics GFX = new Graphics(343);
	
	@Override
	public int getDelay() {
		return 2000;
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.NORMAL;
	}

	@Override
	public boolean spellEffect(final Player player, final Player target) {
		if (target.isDead() || target.isFinished() || target.isLocked()) {
			return false;
		}
		if (target.getVarManager().getBitValue(SettingVariables.ACCEPT_AID_VARBIT_ID) != 1) {
			player.sendMessage("The other player isn't accepting aid.");
			return false;
		}
		if (target.getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL)
				|| target.getInterfaceHandler().containsInterface(InterfacePosition.CHATBOX)) {
			player.sendMessage("The other player is busy.");
			return false;
		}
		
		player.setAnimation(ANIM);
		player.setGraphics(GFX);
		player.faceEntity(target);
		this.addXp(player, 92);
		Teleother.request(player, target, TeleportCollection.TELEOTHER_FALADOR);
		return true;
	}

    @Override
    public boolean spellEffect(final Player player, final NPC npc) {
        player.sendMessage("You can only cast that on other players.");
        return false;
    }
}
