package com.zenyte.game.content.chambersofxeric;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class RaidHelperOverlay extends Interface {
    @Override
    protected void attach() {
        put(2, "Overlay component");
    }

    static void setVisibility(@NotNull final Player player, final boolean hidden) {
        GameInterface.RAID_HELPER_OVERLAY.getPlugin().ifPresent(plugin -> player.getPacketDispatcher().sendComponentVisibility(plugin.getInterface(), plugin.getComponent("Overlay component"), hidden));
    }

    @Override
    protected void build() {}

    @Override
    public GameInterface getInterface() {
        return GameInterface.RAID_HELPER_OVERLAY;
    }
}
