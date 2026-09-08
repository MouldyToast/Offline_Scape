package org.jesse.game.content.skills.construction.objects.garden;

import org.jesse.game.content.skills.construction.Construction;
import org.jesse.game.content.skills.construction.ObjectInteraction;
import org.jesse.game.content.skills.construction.RoomReference;
import org.jesse.game.content.skills.construction.dialogue.DungeonEnterD;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.RoomRemovingD;

/**
 * @author Kris | 22. nov 2017 : 6:29.20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class DungeonEntrance implements ObjectInteraction {

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DUNGEON_ENTRANCE };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        if (optionId == 1) {
            for (RoomReference room : player.getConstruction().getReferences()) {
                if (reference.getX() == room.getX() && reference.getY() == room.getY() && room.getPlane() == 0) {
                    player.setLocation(new Location(player.getX(), player.getY(), 0));
                    return;
                }
            }
            player.getDialogueManager().start(new DungeonEnterD(player, reference, object));
        } else if (optionId == 4) {
            final int roomX = player.getLocation().getChunkX() - construction.getChunkX() + construction.getYardOffset();
            final int roomY = player.getLocation().getChunkY() - construction.getChunkY() + construction.getYardOffset();
            final RoomReference ref = construction.getReference(roomX, roomY, 0);
            if (ref != null)
                player.getDialogueManager().start(new RoomRemovingD(player, ref));
            else
                player.sendMessage("You haven't built anything under here!");
        }
    }
}
