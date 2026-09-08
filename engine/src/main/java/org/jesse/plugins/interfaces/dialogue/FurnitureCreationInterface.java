package org.jesse.plugins.interfaces.dialogue;

import org.jesse.game.GameInterface;
import org.jesse.game.content.chambersofxeric.storageunit.StorageUnit;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.Player;

import java.util.Optional;

/**
 * @author Kris | 12. juuli 2018 : 14:22:31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FurnitureCreationInterface extends Interface {

    @Override
    protected void attach() {
        put(2, "Build furniture");
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        player.getTemporaryAttributes().remove("raidsStorageUnit");
        player.getPacketDispatcher().sendClientScript(2158);
    }

    @Override
    protected void build() {
        bind("Build furniture", (player, slotId, itemId, option) -> {
            if (player.getTemporaryAttributes().containsKey("raidsStorageUnit")) {
                StorageUnit.build(player, slotId - 1);
            } else {
                player.getConstruction().buildFurniture(slotId - 1);
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.FURNITURE_CREATION;
    }
}
