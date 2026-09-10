package org.jesse.game.world.region.area.wilderness;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.net.packet.PacketDispatcher;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 26/03/2019 17:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WildernessOverlay extends Interface {
    @Override
    protected void attach() {
        put(47, "Red circle");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        dispatcher.sendComponentVisibility(getInterface(), getComponent("Red circle"), true);
    }

    @Override
    protected void build() {
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.WILDERNESS_OVERLAY;
    }
}
