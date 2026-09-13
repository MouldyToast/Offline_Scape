package org.jesse.game.content.chambersofxeric.plugins.object;

import org.jesse.game.content.chambersofxeric.Raid;
import org.jesse.game.content.chambersofxeric.room.DeathlyRoom;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.calog.CAType;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

import static org.jesse.game.content.chambersofxeric.room.DeathlyRoom.keystoneCrystal;
import static org.jesse.game.content.chambersofxeric.room.DeathlyRoom.placingKeystoneAnimation;

@SuppressWarnings("unused")
public class ShimmeringBarrier implements ObjectAction, ItemOnObjectAction {

    private static final SoundEffect sound = new SoundEffect(1657, 10, 0);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        dispel(player, object);
    }

    private void dispel(final Player player, final WorldObject object) {
        player.getRaid().ifPresent(raid -> raid.ifInRoom(player, DeathlyRoom.class, room -> {
            if (!player.getInventory().containsItem(keystoneCrystal)) {
                player.sendMessage("You're going to need a magical keystone to dispel this barrier.");
                return;
            }
            player.setAnimation(placingKeystoneAnimation);
            World.sendSoundEffect(player.getLocation(), sound);
            player.sendMessage("Your keystone glows as it is absorbed into the barrier, which disperses.");
            player.getInventory().deleteItem(keystoneCrystal);
            World.removeObject(object);
            Raid.incrementPoints(player, 2000);

            boolean anyNpcsKilled = false;
            for (NPC npc : room.getNpcs()) {
                if (npc.isDead() || npc.isFinished()) {
                    anyNpcsKilled = true;
                    break;
                }
            }

            if (!anyNpcsKilled) {
                player.getCombatAchievements().complete(CAType.NO_TIME_FOR_DEATH);
            }
            room.getNpcs().forEach(NPC::sendDeath);
        }));
    }

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
        dispel(player, object);
    }

    @Override
    public Object[] getItems() {
        return new Object[] { keystoneCrystal.getId() };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SHIMMERING_BARRIER };
    }
}
