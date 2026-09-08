package org.jesse.game.content.skills.magic.spells.lunar;

import org.jesse.game.content.minigame.castlewars.CastleWarsArea;
import org.jesse.game.content.skills.magic.Spellbook;
import org.jesse.game.content.skills.magic.spells.DefaultSpell;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.Toxins.ToxinType;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 16. veebr 2018 : 20:36.24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CureMe implements DefaultSpell {

	private static final Animation ANIM = new Animation(4411);
	private static final Graphics GFX = new Graphics(742, 0, 92);
	private static final SoundEffect sound = new SoundEffect(2884);

	@Override
	public int getDelay() {
		return 600;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
        if (!hasDefenceRequirement(player)) {
            return false;
        }
		if (!player.getToxins().isPoisoned() && !player.getToxins().isVenomed()) {
			player.sendMessage("You aren't poisoned.");
			return false;
		}
        if (player.inArea(CastleWarsArea.class)) {
            player.sendMessage("You cannot cast cure me in Castle-Wars.");
            return false;
        }
		this.addXp(player, 69);
		player.sendSound(sound);
		player.setAnimation(ANIM);
		player.setGraphics(GFX);
		player.getToxins().cureToxin(ToxinType.POISON);
		return true;
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}

}
