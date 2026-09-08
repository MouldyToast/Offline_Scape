package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.model.ui.PaneType;
import org.jesse.game.model.ui.testinterfaces.advancedsettings.SettingsInterface;
import org.jesse.game.util.AccessMask;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.Setting;

/**
 * @author Tommeh | 28-10-2018 | 16:14
 * @author Jire
 */
public class WorldMapInterface extends Interface {

    @Override
    protected void attach() {
        put(38, "Close");
        put(4, "Esc Close");
        put(24, "Menu");
    }

    @Override
    public void open(Player player) {
        player.getSettings().refreshSetting(Setting.WORLD_MAP_GUIDE);

        player.getWorldMap().setVisible(true);
        player.getWorldMap().updateLocation();

        player.getVarManager().sendBitInstant(SettingsInterface.SETTINGS_SEARCH_LEFT_VARBIT, 0);
        player.getVarManager().sendBitInstant(SettingsInterface.SETTINGS_SEARCH_RIGHT_VARBIT, 0);

        if (player.getWorldMap().isFullScreen()) {
            final PaneType previousPane = player.getInterfaceHandler().getPane();
            player.getWorldMap().setPreviousPane(previousPane);
            player.getInterfaceHandler().sendPane(previousPane, PaneType.FULL_SCREEN);

            player.getInterfaceHandler().sendInterface(getInterface());
        } else {
            player.getPacketDispatcher().sendComponentSettings(getInterface(), 21, 0, 4, AccessMask.CLICK_OP1);

            player.getInterfaceHandler().sendInterface(getInterface());
        }
    }

    @Override
    protected void build() {
        bind("Close", player -> {
            if (player.isLocked()) {
                return;
            }
            player.getWorldMap().close();
        });
        bind("Esc Close", player -> {
            if (player.isLocked()) {
                return;
            }
            player.getWorldMap().close();
        });
        bind("Menu", player -> player.getSettings().toggleSetting(Setting.WORLD_MAP_GUIDE));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.WORLD_MAP;
    }
}
