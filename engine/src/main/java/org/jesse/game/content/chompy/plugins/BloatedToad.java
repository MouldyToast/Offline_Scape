package org.jesse.game.content.chompy.plugins;

import org.jesse.game.content.achievementdiary.DiaryReward;
import org.jesse.game.content.achievementdiary.DiaryUtil;
import org.jesse.game.content.chompy.Chompy;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.HintArrow;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Utils;
import org.jesse.game.util.WorldUtil;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.RandomLocation;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.plugins.item.ReleaseableItem;
import mgi.types.config.npcs.NPCDefinitions;

import java.util.Optional;

public class BloatedToad extends ItemPlugin {
    public static final int BLOATED_TOAD_NPC_ID = 1474;
    private static final RSPolygon polygon = new RSPolygon(new int[][] {{2471, 3010}, {2471, 2938}, {2660, 2938}, {2660, 3010}, {2321, 3010}, {2321, 3071}, {2327, 3071}, {2327, 3078}, {2374, 3078}, {2383, 3069}, {2434, 3069}, {2435, 3010}});

    @Override
    public void handle() {
        bind("Drop", (player, item, slotId) -> {
            if (!polygon.contains(player.getLocation())) {
                player.sendMessage("You can only do this at Feldip Hills.");
                return;
            }
            player.setAnimation(Animation.LADDER_DOWN);
            player.getInventory().deleteItem(ItemId.BLOATED_TOAD, 1);
            player.sendMessage("You carefully place the bloated toad bait.");
            final BloatedToadNPC bloatedToad = new BloatedToadNPC(new Location(player.getLocation()));
            bloatedToad.spawn();
            if (Utils.random(2) != 0) {
                WorldTasksManager.schedule(() -> spawnChompy(player, bloatedToad.getLocation(), bloatedToad), Utils.random(6));
            }
        });
        bind("Release", ReleaseableItem::releaseOne);
        bind("Release all", (player, item, slotId) -> ReleaseableItem.releaseAll(player, item));
    }

    private static void spawnChompy(final Player player, final Location dropLocation, final BloatedToadNPC toad) {
        final int amount = Utils.random(getExtraChompyRate(player)) == 0 ? 2 : 1;
        final Location chompyBaseSpawn = RandomLocation.random(dropLocation, 10);
        final int size = NPCDefinitions.getOrThrow(Chompy.CHOMPY_ID).getSize();
        for (int i = 0; i < amount; i++) {
            WorldUtil.findEmptySquare(chompyBaseSpawn, 10, size, Optional.empty()).ifPresent(spawn -> {
                final Chompy chompy = new Chompy(player.getName(), Chompy.CHOMPY_ID, spawn, true, toad);
                chompy.spawn();
                chompy.calcFollow(dropLocation, 1, true, false, false);
                player.getPacketDispatcher().sendHintArrow(new HintArrow(chompy));
            });
        }
    }

    private static int getExtraChompyRate(final Player player) {
        return DiaryUtil.eligibleFor(DiaryReward.WESTERN_BANNER4, player) ? 0 : DiaryUtil.eligibleFor(DiaryReward.WESTERN_BANNER3, player) ? 2 : 4;
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.BLOATED_TOAD};
    }
}
