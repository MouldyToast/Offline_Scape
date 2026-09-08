package org.jesse.game.content.skills.magic.spells.lunar;

import org.jesse.game.content.skills.magic.Spellbook;
import org.jesse.game.content.skills.magic.spells.DefaultSpell;
import org.jesse.game.item.Item;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.ContainerResult;
import org.jesse.game.world.entity.player.container.RequestResult;

/**
 * @author Kris | 16. veebr 2018 : 20:40.58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class HunterKit implements DefaultSpell {
	private static final Animation ANIM = new Animation(6303);
	private static final Graphics GFX = new Graphics(1074, 0, 62);
	private static final SoundEffect sound = new SoundEffect(3615);

	@Override
	public int getDelay() {
		return 4000;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
		if (player.getInventory().containsItem(11159, 1)) {
			player.sendMessage("You can only carry one Hunter Kit at a time.");
			return false;
		}
		player.setAnimation(ANIM);
		player.setGraphics(GFX);
		player.sendSound(sound);
		player.lock();
		WorldTasksManager.schedule(() -> {
			player.unlock();
			this.addXp(player, 70);
			final ContainerResult result = player.getInventory().addItem(new Item(11159, 1));
			result.onFailure(item -> World.spawnFloorItem(item, player));
			player.sendMessage(result.getResult() == RequestResult.SUCCESS ? "A hunter kit was added to your inventory." : "A hunter kit was dropped under you.");
		}, 6);
		return true;
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}
}
