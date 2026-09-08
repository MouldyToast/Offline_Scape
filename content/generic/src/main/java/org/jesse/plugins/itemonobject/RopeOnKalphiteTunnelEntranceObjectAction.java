package org.jesse.plugins.itemonobject;

import org.jesse.game.content.achievementdiary.diaries.DesertDiary;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.Setting;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Tommeh | 7 apr. 2018 | 22:03:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class RopeOnKalphiteTunnelEntranceObjectAction implements ItemOnObjectAction {

    private static final Location INSIDE_BOSS_LOCATION = new Location(3508, 9494, 0);

    private static final Location INSIDE_LAIR_LOCATION = new Location(3484, 9510, 2);

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        final Setting setting = object.getId() == 3827 ? Setting.KALPHITE_LAIR_TUNNEL_ENTRANCE : Setting.KALPHITE_QUEEN_TUNNEL_ENTRANCE;
        final Location location = setting.equals(Setting.KALPHITE_LAIR_TUNNEL_ENTRANCE) ? INSIDE_LAIR_LOCATION : INSIDE_BOSS_LOCATION;
        if (player.getVarManager().getBitValue(setting.getId()) == 0) {
            player.lock(2);
            player.getInventory().deleteItem(item);
            player.getSettings().setSetting(setting, 1);
            WorldTasksManager.schedule(() -> {
                player.getAchievementDiaries().update(DesertDiary.ENTER_KALPHITE_HIVE);
                player.useStairs(828, location, 1, 2);
            });
        } else {
            player.sendMessage("There is already a rope tied to this tunnel entrance!");
        }
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 954 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 3827, 23609 };
    }
}
