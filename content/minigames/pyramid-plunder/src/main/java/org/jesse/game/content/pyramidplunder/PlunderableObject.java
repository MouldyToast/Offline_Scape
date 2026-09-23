package org.jesse.game.content.pyramidplunder;

import org.jesse.game.content.pyramidplunder.object.PlunderDoor;
import org.jesse.game.content.pyramidplunder.object.PlunderRoomExit;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ImmutableItem;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.broadcasts.BroadcastType;
import org.jesse.game.world.broadcasts.WorldBroadcasts;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.ContainerResult;
import org.jesse.game.world.entity.player.container.RequestResult;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.object.WorldObjectUtils;

import java.util.Collection;
import java.util.List;

/**
 * @author Christopher
 * @since 4/3/2020
 */
public interface PlunderableObject {
    ImmutableItem sceptre = new ImmutableItem(ItemId.PHARAOHS_SCEPTRE);

    default void reward(final Player player) {
        final double floorBoost = 1 + player.getVarManager().getBitValue(PlunderDoor.ROOM_VARBIT) * 0.05;
        final int max = getRewardTiers().size() - 1;
        final int randomIndex = Math.min(max, Utils.random((int) ((max + 1) * floorBoost)));
        final PlunderRewardTier tier = getRewardTiers().get(randomIndex);
        final Collection<PlunderReward> possibleRewards = PlunderReward.get(tier);
        final PlunderReward reward = Utils.getRandomCollectionElement(possibleRewards);
        player.getInventory().addOrDrop(reward.getItemId(), 1);
    }

    default boolean rollForSceptre(final Player player, final WorldObject object) {
        if (Utils.random(749) == 0) {
            PlunderRoomExit.leave(player);
            player.lock(1);
            giveSceptre(player);
            WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, sceptre.generateResult(), WorldObjectUtils.getObjectNameOfPlayer(object, player));
            return true;
        }
        return false;
    }

    default void giveSceptre(final Player player) {
        final Item sceptreItem = sceptre.generateResult();
        final ContainerResult result = player.getInventory().addItem(sceptreItem);
        if (result.getResult() == RequestResult.NOT_ENOUGH_SPACE) {
            World.spawnFloorItem(sceptreItem, PyramidPlunderConstants.OUTSIDE_PYRAMID, player, 300, 200);
        }
        player.getDialogueManager().start(new Dialogue(player, NpcId.GUARDIAN_MUMMY) {
            @Override
            public void buildDialogue() {
                if (result.getResult() == RequestResult.NOT_ENOUGH_SPACE) {
                    item(sceptreItem, "You find a golden sceptre but don\'t have space to carry the sceptre. Clear some space in your backpack and pick the sceptre up, if you want it...");
                } else {
                    item(sceptreItem, "You have found the Pharaoh\'s Sceptre.");
                }
                npc("You have found my Master\'s Sceptre - You have been here long enough. Begone.");
            }
        });
    }

    List<PlunderRewardTier> getRewardTiers();
}
