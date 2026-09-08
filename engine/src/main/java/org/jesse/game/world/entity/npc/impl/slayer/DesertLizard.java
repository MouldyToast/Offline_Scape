package org.jesse.game.world.entity.npc.impl.slayer;

import org.jesse.game.content.achievementdiary.diaries.DesertDiary;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.player.Player;

import static org.jesse.plugins.itemonnpc.IceCoolerOnLizardAction.ANIM;
import static org.jesse.plugins.itemonnpc.IceCoolerOnLizardAction.GFX;

/**
 * @author Tommeh | 7 dec. 2017 : 19:01:36
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DesertLizard extends NPC implements Spawnable {
	public static final Item ICE_COOLER = new Item(6696);

	public DesertLizard(final int id, final Location tile, final Direction direction, final int radius) {
		super(id, tile, direction, radius);
	}

	@Override
	public void sendDeath() {
		final Player source = getMostDamagePlayerCheckIronman();
		if (source == null) {
			super.sendDeath();
			return;
		}
		if (getHitpoints() > 0) {
			return;
		}
		final boolean unlocked = source.getSlayer().isUnlocked("Reptile freezer") && source.getInventory().containsItem(ItemId.ICE_COOLER);
		if (unlocked) {
			setGraphics(GFX);
			source.setAnimation(ANIM);
			source.getInventory().deleteItem(ItemId.ICE_COOLER, 1);
			kill(source);
		} else {
			heal(1);
		}
	}

	public void kill(final Player player) {
		player.getAchievementDiaries().update(DesertDiary.SLAY_DESERT_LIZARD);
		player.sendMessage("The lizard shudders and collapses from the freezing water.");
		super.sendDeath();
	}

	@Override
	public boolean validate(final int id, final String name) {
		return id >= 458 && id <= 463;
	}
}
