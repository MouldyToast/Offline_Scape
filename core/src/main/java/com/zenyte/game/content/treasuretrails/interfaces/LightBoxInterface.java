package com.zenyte.game.content.treasuretrails.interfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.treasuretrails.clues.LightBoxKeys;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.net.packet.PacketDispatcher;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 20/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LightBoxInterface extends Interface {
    @Override
    protected void attach() {
        put(8, "A");
        put(9, "B");
        put(10, "C");
        put(11, "D");
        put(12, "E");
        put(13, "F");
        put(14, "G");
        put(15, "H");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        getReverseKeyset().forEach(name -> dispatcher.sendComponentSettings(getInterface(), getComponent(name), -1, 0, AccessMask.CLICK_OP1));
    }

    @Override
    protected void build() {
        bind("A", player -> LightBoxKeys.lightBox(player).press(0));
        bind("B", player -> LightBoxKeys.lightBox(player).press(1));
        bind("C", player -> LightBoxKeys.lightBox(player).press(2));
        bind("D", player -> LightBoxKeys.lightBox(player).press(3));
        bind("E", player -> LightBoxKeys.lightBox(player).press(4));
        bind("F", player -> LightBoxKeys.lightBox(player).press(5));
        bind("G", player -> LightBoxKeys.lightBox(player).press(6));
        bind("H", player -> LightBoxKeys.lightBox(player).press(7));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.LIGHT_BOX;
    }
}
