package org.jesse.game.content.minigame.pestcontrol;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.RegionArea;

/**
 * @author Kris | 13/12/2018 19:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PestControlGameInterface extends Interface {
    @Override
    protected void attach() {
        put(25, "Western shield");
        put(31, "South-Western shield");
        put(29, "South-Eastern shield");
        put(27, "Eastern shield");
        put(21, "Western health");
        put(24, "South-Western health");
        put(23, "South-Eastern health");
        put(22, "Eastern health");
        put(6, "Time remaining");
        put(7, "Void knight health");
        put(10, "Damage dealt");
    }

    @Override
    public void open(Player player) {
        final RegionArea area = player.getArea();
        if (!(area instanceof PestControlInstance)) {
            player.sendMessage("You cannot open the pest control overlay outside of pest control instances.");
            return;
        }
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    protected void build() {
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.PEST_CONTROL_GAME_OVERLAY;
    }
}
