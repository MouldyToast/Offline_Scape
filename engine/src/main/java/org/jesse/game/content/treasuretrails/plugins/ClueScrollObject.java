package org.jesse.game.content.treasuretrails.plugins;

import org.jesse.game.content.treasuretrails.TreasureTrail;
import org.jesse.game.content.treasuretrails.TreasureTrailType;
import org.jesse.game.content.treasuretrails.clues.CrypticClue;
import org.jesse.game.content.treasuretrails.clues.MapClue;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.object.ClosedChestObject;
import org.jesse.plugins.object.DrawersObject;
import org.jesse.plugins.object.WardrobeObject;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

/**
 * @author Kris | 07/04/2019 16:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ClueScrollObject implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!TreasureTrail.search(player, object, option) && !TreasureTrail.searchWithkey(player, object, option)) {
            player.sendMessage("You find nothing.");
        }
    }

    @Override
    public Object[] getObjects() {
        TreasureTrailType.values();
        MapClue.values();
        final IntOpenHashSet set = new IntOpenHashSet(CrypticClue.objectMap.keySet());
        set.removeAll(DrawersObject.map.keySet());
        set.removeAll(DrawersObject.map.values());
        set.removeAll(WardrobeObject.map.keySet());
        set.removeAll(WardrobeObject.map.values());
        set.removeAll(ClosedChestObject.map.keySet());
        set.removeAll(ClosedChestObject.map.values());
        set.remove(357); // used by motherload crate, which handles the clue behaviour too
        return set.toArray();
    }
}
