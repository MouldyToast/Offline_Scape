package com.zenyte.game.world.entity.player;

import com.zenyte.game.content.gravestones.GravestoneKeys;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.world.entity.Location;

public class WorldMap {

    private final Player player;

    private PaneType previousPane;

    private boolean visible, fullScreen;

    public WorldMap(final Player player) {
        this.player = player;
    }

    public void updateLocation() {
        updateLocation(player.getLocation());
    }

    public void updateLocation(Location location) {
        player.getPacketDispatcher().sendClientScript(
                1749,
                location.getPositionHash(),
                -1,
                GravestoneKeys.gravestone(player).getGravestoneLocationBitpacked()
        );
    }

    public void close() {
        visible = false;
        fullScreen = false;
        if (player.getInterfaceHandler().getPane().equals(PaneType.FULL_SCREEN)) {
            player.getInterfaceHandler().sendPane(PaneType.FULL_SCREEN, player.getWorldMap().getPreviousPane());
        }
        player.getInterfaceHandler().closeInterface(InterfacePosition.WORLD_MAP);
    }

    public PaneType getPreviousPane() {
        return previousPane;
    }

    public void setPreviousPane(PaneType previousPane) {
        this.previousPane = previousPane;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

}
