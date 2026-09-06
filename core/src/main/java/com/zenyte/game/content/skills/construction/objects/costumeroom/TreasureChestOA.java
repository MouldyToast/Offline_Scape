package com.zenyte.game.content.skills.construction.objects.costumeroom;

import com.zenyte.game.content.skills.construction.ConstructionKeys;
import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.content.skills.construction.costume.TreasureChestData;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.OptionsMenuD;

/**
 * @author Kris | 6. march 2018 : 2:35.12
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class TreasureChestOA implements ObjectInteraction, ItemOnObjectAction {

    private static final Animation ANIM = new Animation(834);

    @Override
    public Object[] getObjects() {
        return new Object[] { 18804, ObjectId.TREASURE_CHEST_18805, 18806, ObjectId.TREASURE_CHEST_18807, ObjectId.TREASURE_CHEST_18808, ObjectId.TREASURE_CHEST_18809 };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        if (option.equals("open")) {
            World.spawnObject(new WorldObject(object.getId() + 1, object.getType(), object.getRotation(), object));
        } else if (option.equals("close")) {
            World.spawnObject(new WorldObject(object.getId() - 1, object.getType(), object.getRotation(), object));
        } else if (option.equals("search")) {
            if (player.getCurrentHouse() != construction) {
                player.sendMessage("Only the owner of the house can use the treasure chest.");
                return;
            }
            final String[] tierNames;
            final int[] tierEnums;
            final int id = object.getId();
            if (id == 18805) {
                tierNames = new String[]{"Beginner rewards", "Easy rewards"};
                tierEnums = new int[]{3293, 3294};
            } else if (id == 18807) {
                tierNames = new String[]{"Beginner rewards", "Easy rewards", "Medium rewards"};
                tierEnums = new int[]{3293, 3294, 3295};
            } else {
                tierNames = new String[]{"Beginner rewards", "Easy rewards", "Medium rewards", "Hard rewards", "Elite rewards", "Master rewards"};
                tierEnums = new int[]{3293, 3294, 3295, 3296, 3297, 3298};
            }
            player.getDialogueManager().start(new OptionsMenuD(player, "Which rewards would you like to view?", tierNames) {
                @Override
                public void handleClick(final int slotId) {
                    construction.getTreasureChest().open(tierEnums[slotId]);
                }
            });
        }
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (TreasureChestData.MAP.get(item.getId()) == null) {
            player.sendMessage("You can't put this in the treasure chest.");
            return;
        }
        final int id = object.getId();
        if (id % 2 == 0) {
            player.sendMessage("You need to open the treasure chest first.");
            return;
        }
        if (ConstructionKeys.construction(player).getTreasureChest().addSet(item.getId())) {
            player.setAnimation(ANIM);
            return;
        }
    }

    @Override
    public Object[] getItems() {
        return null;
    }
}
