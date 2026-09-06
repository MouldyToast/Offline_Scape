package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.DefaultSpell;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 15. veebr 2018 : 22:30.56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Geomancy implements DefaultSpell {

	//private static final List<FarmingPatch> LIST = Arrays.asList(FarmingPatch.VALUES);
	private static final Animation ANIM = new Animation(7118);
	private static final Graphics GFX = new Graphics(1285, 30, 0);
	private static final SoundEffect SOUND = new SoundEffect(2891, 3, 50);
	
	@Override
	public int getDelay() {
		return 5000;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
		player.lock();
        World.sendSoundEffect(player, SOUND);
		player.setAnimation(ANIM);
		player.setGraphics(GFX);
		this.addXp(player, 60);
		WorldTasksManager.schedule(() -> {
			player.unlock();
			//sendInterface(player);
		}, 3);
		return true;
	}
	
	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}

}
