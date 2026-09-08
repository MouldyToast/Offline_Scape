package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 24/04/2019 01:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ExperienceDropsWindow extends Interface {

    @Override
    protected void attach() {

    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    protected void build() {

    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.EXPERIENCE_DROPS_WINDOW;
    }
}
