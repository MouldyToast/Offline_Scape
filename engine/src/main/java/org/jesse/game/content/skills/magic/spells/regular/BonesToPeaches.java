package org.jesse.game.content.skills.magic.spells.regular;

import org.jesse.game.content.achievementdiary.diaries.LumbridgeDiary;
import org.jesse.game.content.skills.magic.Spellbook;
import org.jesse.game.content.skills.magic.spells.DefaultSpell;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.PlainChat;
import org.jesse.plugins.item.BonesTablet;

/**
 * @author Kris | 8. jaan 2018 : 2:32.24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BonesToPeaches implements DefaultSpell {
	@Override
	public int getDelay() {
		return 1000;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
		final int amount = BonesTablet.convertBones(player, BonesTablet.PEACHES);
		if (amount > 0) {
			player.getAchievementDiaries().update(LumbridgeDiary.CAST_BONES_TO_PEACHES);
			player.setAnimation(BonesTablet.animation);
			player.setGraphics(BonesTablet.graphics);
			player.sendSound(BonesTablet.sound);
			this.addXp(player, 35.5);
			player.getInventory().refreshAll();
			player.sendMessage("You convert " + amount + " x bones to peaches.");
		} else {
			player.getDialogueManager().start(new PlainChat(player, "You aren't holding any bones!"));
			return false;
		}
		return true;
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.NORMAL;
	}
}
