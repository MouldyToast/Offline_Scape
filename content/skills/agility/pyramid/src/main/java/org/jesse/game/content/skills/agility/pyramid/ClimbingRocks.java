package org.jesse.game.content.skills.agility.pyramid;

import org.jesse.game.content.achievementdiary.diaries.DesertDiary;
import org.jesse.game.content.skills.agility.AgilityCourseObstacle;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.Inventory;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.ItemChat;

public final class ClimbingRocks extends AgilityCourseObstacle {
    private static final Animation climbingAnim = new Animation(3063);

    public ClimbingRocks() {
        super(AgilityPyramid.class, 6);
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.setAnimation(climbingAnim);
        player.sendSound(new SoundEffect(2454));
        WorldTasksManager.schedule(() -> {
            if (player.getVarManager().getBitValue(AgilityPyramid.HIDE_PYRAMID_VARBIT) == 0) {
                final Inventory inventory = player.getInventory();
                if (inventory.hasFreeSlots()) {
                    WorldTasksManager.schedule(() -> {
                        final Item reward = new Item(ItemId.PYRAMID_TOP);
                        player.getDialogueManager().start(new ItemChat(player, reward, "You find a golden pyramid!"));
                        player.getVarManager().sendBit(AgilityPyramid.HIDE_PYRAMID_VARBIT, true);
                        inventory.addOrDrop(reward);
                        player.getAchievementDiaries().update(DesertDiary.CLIMB_AGILITY_PYRAMID);
                    });
                } else {
                    player.sendMessage("You don\'t have enough inventory space to pick up this item.");
                }
            } else {
                player.sendMessage("You find nothing on top of the pyramid.");
            }
        });
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 3;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 0;
    }

    @Override
    public int getLevel(WorldObject object) {
        return 30;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {10851};
    }
}
