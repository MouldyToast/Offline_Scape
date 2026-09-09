package org.jesse.plugins.object;

import org.jesse.game.content.achievementdiary.diaries.*;
import org.jesse.game.content.skills.prayer.Prayer;
import org.jesse.game.content.skills.prayer.actions.Bones;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Colour;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentUtils;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

import java.util.ArrayList;

/**
 * @author Kris | 28. apr 2018 : 16:21.37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class AltarOPlugin implements ObjectAction, ItemOnObjectAction {

    public static final Animation PRAY_ANIM = new Animation(645);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Pray-at") || option.equals("Pray")) {
            if (player.getPrayerManager().getPrayerPoints() >= player.getSkills().getLevelForXp(SkillConstants.PRAYER)) {
                player.sendMessage("You already have full prayer points.");
                return;
            }
            final int toRestore = player.getSkills().getLevelForXp(SkillConstants.PRAYER) - player.getPrayerManager().getPrayerPoints();
            if (EquipmentUtils.containsFullInitiate(player) && object.getId() == 410) {
                player.getAchievementDiaries().update(FaladorDiary.PRAY_ALTAR_OF_GUTHIX);
            } else if (EquipmentUtils.containsFullProselyte(player)) {
                player.getAchievementDiaries().update(FaladorDiary.RECHARGE_PRAYER);
            } else if (object.getId() == 10389) {
                player.getAchievementDiaries().update(DesertDiary.PRAY_AT_ELIDINIS_STATUETTE);
            } else if (toRestore >= 85 && object.getId() == 20377) {
                player.getAchievementDiaries().update(DesertDiary.RESTORE_85_PRAYER_POINTS);
            } else if (player.getPrayerManager().isActive(Prayer.SMITE)) {
                player.getAchievementDiaries().update(VarrockDiary.PRAY_AT_VARROCK_ALTAR);
                player.getAchievementDiaries().update(LumbridgeDiary.RECHARGE_PRAYER);
            }
            player.getAchievementDiaries().update(KourendDiary.PRAY_AT_ALTAR);
            player.getAchievementDiaries().update(WildernessDiary.PRAY_AT_CHAOS_ALTAR);
            player.getAchievementDiaries().update(ArdougneDiary.USE_EAST_ARDOUGNE_ALTAR);
            player.lock();
            player.sendMessage("You pray to the gods...");
            player.setAnimation(PRAY_ANIM);
            player.sendSound(2674);
            WorldTasksManager.schedule(() -> {
                player.getPrayerManager().restorePrayerPoints(99);
                player.sendMessage("... and recharge your prayer.");
                player.unlock();
            });
        }
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        player.sendMessage("Nothing interesting happens.");
    }

    @Override
    public Object[] getItems() {
        final ArrayList<Object> list = new ArrayList<>(Bones.VALUES.length);
        for (final Bones bone : Bones.VALUES) {
            for (final Item b : bone.getItems()) {
                list.add(b.getId());
            }
        }
        return list.toArray(new Object[0]);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { "Altar of Guthix", "Chaos altar", "Altar", ObjectId.ALTAR_20377, 18258, ObjectId.ALTAR};
    }
}
