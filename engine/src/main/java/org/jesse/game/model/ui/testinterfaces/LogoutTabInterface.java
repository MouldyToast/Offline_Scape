package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.LogoutType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.RegionArea;
import org.jesse.game.world.region.area.plugins.LogoutRestrictionPlugin;

/**
 * @author Tommeh | 1-2-2019 | 20:17
 * @author Jire
 */
public final class LogoutTabInterface extends Interface {

    @Override
    protected void attach() {
        put(8, "Logout");
        put(3, "World switcher");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    protected void build() {
        bind("Logout", player -> player.clickedLogoutButton(null));
        bind("World switcher", GameInterface.WORLD_SWITCHER::open);
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.LOGOUT;
    }

}
