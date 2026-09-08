package org.jesse.game.content.skills.construction.objects.throneroom;

import org.jesse.game.content.skills.construction.Construction;
import org.jesse.game.content.skills.construction.ObjectInteraction;
import org.jesse.game.content.skills.construction.RoomReference;
import org.jesse.game.content.skills.construction.constants.RoomType;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 26. veebr 2018 : 19:00.14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Trapdoor implements ObjectInteraction {

    private static final Animation ANIM = new Animation(827);

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TRAPDOOR_13675, ObjectId.TRAPDOOR_13676, ObjectId.TRAPDOOR_13677, ObjectId.TRAPDOOR_13678, ObjectId.TRAPDOOR_13679, ObjectId.TRAPDOOR_13680 };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        if (option.equals("open") || option.equals("close")) {
            player.lock(1);
            object.setLocked(true);
            player.setAnimation(ANIM);
            WorldTasksManager.schedule(new WorldTask() {

                @Override
                public void run() {
                    World.spawnObject(new WorldObject(object.getId() + (object.getId() > 13677 ? -3 : 3), object.getType(), object.getRotation(), object));
                }
            });
        } else if (option.equals("go-down")) {
            final WorldObject ladder = World.getObjectWithType(new Location(object.getX(), object.getY(), object.getPlane() - 1), 10);
            if (ladder == null || !(ladder.getId() >= 13328 && ladder.getId() <= 13330)) {
                player.sendMessage("You can't go down there, there's no ladder.");
                return;
            }
            player.lock(1);
            player.setAnimation(ANIM);
            WorldTasksManager.schedule(new WorldTask() {

                @Override
                public void run() {
                    player.setLocation(new Location(player.getX(), player.getY(), player.getPlane() - 1));
                }
            });
        } else if (option.equals("remove-room")) {
            if (!construction.isBuildingMode()) {
                player.sendMessage("You can only do this in building mode.");
                return;
            }
            final RoomReference room = construction.getReference(reference.getX(), reference.getY(), 0);
            if (room == null || room.getRoom() != RoomType.OUBLIETTE) {
                player.sendMessage(room == null ? "There's no room down there." : "You can't remove the room down there from here.");
                return;
            }
            construction.getReferences().remove(room);
            construction.enterHouse(true, construction.getRelationalSpawnTile());
        }
    }
}
