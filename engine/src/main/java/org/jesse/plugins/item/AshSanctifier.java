package org.jesse.plugins.item;

import org.jesse.game.content.achievementdiary.DiaryReward;
import org.jesse.game.content.achievementdiary.DiaryUtil;
import org.jesse.game.content.skills.prayer.actions.Bones;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;

/**
 * Handles the item interactions of the Ash sanctifier.
 */
public final class AshSanctifier extends ItemPlugin {

    @Override
    public void handle() {
        bind("check",  (player, item, slotId) ->
                player.sendMessage("Your Ash sanctifier has " + item.getCharges() + " charges left."));

        bind("uncharge", (player, item, slotId) -> {
            final int charges = item.getCharges();
            if (charges <= 0) {
                player.sendMessage("Your Ash sanctifier is already out of charges.");
                return;
            }

            player.getInventory().addOrDrop(new Item(ItemId.DEATH_RUNE, (int) Math.floor((double)charges / 10)));
            item.setCharges(0);
            player.sendMessage("You uncharge your Ash sanctifier.");
        });
    }

    /**
     * Invoked when a NPC drops an ash typed bone.
     *
     * @param player    the {@link  Player} receiving the drop.
     * @param bone      a {@link  Bones ash-typed bone}.
     */
    public static boolean handleAshDrop(Player player, Bones bone) {
        final Item ashPacifier = player.getInventory().getAny(ItemId.ASH_SANCTIFIER);
        if (ashPacifier == null) {
            return false;
        }

        final int charges = ashPacifier.getCharges();
        if (charges <= 0) {
            return false;
        }

        final int newCharges = charges - 1;
        ashPacifier.setCharges(newCharges);

        final double xp = bone.getXp();
        double experienceGained = DiaryUtil.eligibleFor(DiaryReward.RADAS_BLESSING4, player) ? xp : xp / 2;
        player.getSkills().addXp(SkillConstants.PRAYER, experienceGained);

        player.sendMessage("Your Ash sanctifier automatically scatters the ashes leave " + newCharges + " charges left.");
        return true;
    }

    @Override
    public int[] getItems() {
        return new int[]{ ItemId.ASH_SANCTIFIER };
    }
}
