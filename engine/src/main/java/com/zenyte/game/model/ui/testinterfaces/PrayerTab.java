package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;

/**
 * @author Kris | 16/04/2019 16:59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PrayerTab extends Interface {
    @Override
    protected void attach() {
        put(42, "spell filter");
    }

    @Override
    public DefaultClickHandler getDefaultHandler() {
        return (player, componentId, slotId, itemId, optionId) -> {
            var prayer = Prayer.getComponent(componentId);
            if (prayer != null) {
                player.getPrayerManager().togglePrayer(prayer, false);
            } else if (componentId == getComponent("spell filter")) {
                if (slotId == 0) {
                    player.getSettings().toggleSetting(Setting.PRAYER_FILTER_LOW_TIER);
                    if (player.getVarManager().getBitValue(Setting.PRAYER_FILTER_LOW_TIER.getId()) == 0) {
                        player.getVarManager().sendBit(Setting.PRAYER_FILTER_MULT_SKILL.getId(), 0);
                    }
                } else if (slotId == 1) {
                    if (player.getVarManager().getBitValue(Setting.PRAYER_FILTER_LOW_TIER.getId()) == 0) {
                        return;
                    }
                    player.getSettings().toggleSetting(Setting.PRAYER_FILTER_MULT_SKILL);
                } else if (slotId == 2) {
                    player.getSettings().toggleSetting(Setting.PRAYER_FILTER_RAPID_HEAL);
                } else if (slotId == 3) {
                    player.getSettings().toggleSetting(Setting.PRAYER_FILTER_LACK_LEVEL);
                } else if (slotId == 4) {
                    player.getSettings().toggleSetting(Setting.PRAYER_FILTER_LACK_REQ);
                }
            }
        };
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);

        player.getPacketDispatcher().sendComponentSettings(getInterface(), 42, 0, 4, AccessMask.CLICK_OP1);
    }

    @Override
    protected void build() {

    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.PRAYER_TAB_INTERFACE;
    }
}
