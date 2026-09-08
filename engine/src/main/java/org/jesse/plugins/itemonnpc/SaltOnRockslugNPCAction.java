package org.jesse.plugins.itemonnpc;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.impl.slayer.Rockslug;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 11. mai 2018 : 00:56:44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SaltOnRockslugNPCAction implements ItemOnNPCAction {

	private static final Graphics GFX = new Graphics(327);
	private static final Animation ANIM = new Animation(1574);
	private static final SoundEffect saltSound = new SoundEffect(2719);
	
	@Override
	public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
		player.setGraphics(GFX);
		player.setAnimation(ANIM);
		player.lock(2);
		World.sendSoundEffect(player.getLocation(), saltSound);
		if (npc.getHitpoints() <= 5) {
			npc.sendDeath();
		} else {
			player.sendMessage("The rockslug isn't weak enough to be affected by the salt.");
		}
	}

	@Override
	public Object[] getItems() {
		return new Object[] { Rockslug.BAG_OF_SALT.getId() };
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { "Rockslug", "Giant rockslug" };
	}

}
