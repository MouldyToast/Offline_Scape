package org.jesse.game.content.minigame.pestcontrol.plugins;

import org.jesse.game.content.minigame.pestcontrol.PestControlInstance;
import org.jesse.game.content.minigame.pestcontrol.PestControlUtilities;
import org.jesse.game.item.Item;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.Inventory;
import org.jesse.game.world.object.DoubleDoor;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.RegionArea;

/**
 * @author Kris | 27. juuni 2018 : 17:22:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class PestControlGatePlugin implements ObjectAction {

    private static final Animation HAMMERING = new Animation(3971);

    private static final Item LOGS = new Item(1511);

    private static final Item HAMMER = new Item(2347);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Open") || option.equals("Close")) {
            DoubleDoor.handleDoubleDoor(player, object);
        } else if (option.equals("Repair")) {
            final RegionArea area = player.getArea();
            if (!(area instanceof PestControlInstance)) {
                return;
            }
            final PestControlInstance instance = (PestControlInstance) area;
            final Inventory inventory = player.getInventory();
            if (!inventory.containsItem(LOGS)) {
                player.sendMessage("You need some logs to repair the door.");
                return;
            }
            if (!inventory.containsItem(HAMMER)) {
                player.sendMessage("You need a hammer to repair the door.");
                return;
            }
            player.lock(2);
            player.setAnimation(HAMMERING);
            inventory.deleteItem(LOGS);
            instance.addActivity(player, PestControlUtilities.MODERATE_ACTIVITY_POINTS);
            final int id = object.getId();
            World.removeObject(object);
            final int offset = (id - 14233) % 4;
            World.spawnObject(new WorldObject(14233 + offset, 0, object.getRotation(), object));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.GATE_14233, ObjectId.GATE_14234, ObjectId.GATE_14235, ObjectId.GATE_14236, ObjectId.GATE_14237, ObjectId.GATE_14238, ObjectId.GATE_14239, ObjectId.GATE_14240, ObjectId.GATE_14241, ObjectId.GATE_14242, ObjectId.GATE_14243, ObjectId.GATE_14244, ObjectId.GATE_14245, ObjectId.GATE_14246, ObjectId.GATE_14247, ObjectId.GATE_14248 };
    }
}
