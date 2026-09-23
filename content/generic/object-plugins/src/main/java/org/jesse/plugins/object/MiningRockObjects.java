package org.jesse.plugins.object;

import org.jesse.game.content.minigame.castlewars.CastlewarsRockPatch;
import org.jesse.game.content.skills.mining.MiningDefinitions;
import org.jesse.game.content.skills.mining.actions.Mining;
import org.jesse.game.content.skills.mining.actions.Prospect;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 10. nov 2017 : 22:12.13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class MiningRockObjects implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Prospect")) {
            player.getActionManager().setAction(new Prospect(object));
        } else if (MiningDefinitions.ores.keySet().contains(object.getId())) {
            /* Check behavior before executing action for cwars wall */
            if (object.getId() == ObjectId.CAVE_WALL) {
                final CastlewarsRockPatch wallData = CastlewarsRockPatch.getData(object);
                if (wallData == null)
                    return;
                if (World.getObjectOfSlot(wallData.getPatch(), 10) == null) {
                    player.getActionManager().setAction(new Mining(object));
                } else {
                    player.sendMessage("This tunnel has already collapsed.");
                }
                return;
            }
            player.getActionManager().setAction(new Mining(object));
        } else if (object.getId() == 7468 || object.getId() == 7469 || object.getId() == 30373 || object.getId() == 33253)
            player.sendMessage("There is currently no ore available in this rock.");
    }

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public Object[] getObjects() {
        final IntArrayList list = new IntArrayList(MiningDefinitions.ores.keySet());
        list.add(7468);
        list.add(7469);
        list.add(30373);
        list.add(33253);
        list.rem(ObjectId.SOFT_CLAY_ROCKS_36210); // soft-clay handled by trahaern mine rocks plugin
        return list.toArray();
    }
}
