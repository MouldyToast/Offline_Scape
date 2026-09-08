package org.jesse.plugins.object;

import org.jesse.game.content.treasuretrails.stash.StashUnit;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

/**
 * @author Kris | 27. nov 2017 : 2:29.47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class StashUnitObject implements ObjectAction {
	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		if (option.equals("Build")) {
			player.getStash().build(object);
		} else if (option.equalsIgnoreCase("Search")) {
			player.getStash().search(object);
		}
	}

	@Override
	public Object[] getObjects() {
		final IntOpenHashSet set = new IntOpenHashSet(StashUnit.getMap().keySet());
		set.remove(-1);
		return set.toArray();
	}
}
