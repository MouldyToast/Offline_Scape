package org.jesse.game.content.pyramidplunder;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Christopher
 * @since 4/1/2020
 */
public class PyramidPlunderOverlay extends Interface {
    @Override
    public void attach() {
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    public void build() {
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.PYRAMID_PLUNDER;
    }
}
