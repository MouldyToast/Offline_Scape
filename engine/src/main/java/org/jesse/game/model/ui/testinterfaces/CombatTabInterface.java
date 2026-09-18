package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 24/10/2018 14:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CombatTabInterface extends Interface {
    @Override
    protected void attach() {
        put(6, "Attack style 1");
        put(10, "Attack style 2");
        put(14, "Attack style 3");
        put(18, "Attack style 4");
        put(23, "Defensive autocast");
        put(28, "Autocast");
        put(32, "Auto retaliate");
        put(39, "Special attack");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getCombatDefinitions().refresh();
    }

    @Override
    public boolean isInterruptedOnLock() {
        return false;
    }

    @Override
    protected void build() {
        bind("Attack style 1", player -> {
            player.getCombatDefinitions().setStyle(0);
            player.getCombatDefinitions().setAutocastSpell(null);
        });
        bind("Attack style 2", player -> {
            player.getCombatDefinitions().setStyle(1);
            player.getCombatDefinitions().setAutocastSpell(null);
        });
        bind("Attack style 3", player -> {
            player.getCombatDefinitions().setStyle(2);
            player.getCombatDefinitions().setAutocastSpell(null);
        });
        bind("Attack style 4", player -> {
            player.getCombatDefinitions().setAutocastSpell(null);
            player.getCombatDefinitions().setStyle(3);
            player.getCombatDefinitions().refresh();
        });
        bind("Defensive autocast", player -> {
            if (player.isLocked()) {
                return;
            }
            player.getCombatDefinitions().setDefensiveAutocast(true);
            GameInterface.AUTOCAST_TAB.open(player);
        });
        bind("Autocast", player -> {
            if (player.isLocked()) {
                return;
            }
            player.getCombatDefinitions().setDefensiveAutocast(false);
            GameInterface.AUTOCAST_TAB.open(player);
        });
        bind("Auto retaliate", player -> {
            if (player.isLocked()) {
                return;
            }
            player.getCombatDefinitions().setAutoRetaliate(!player.getCombatDefinitions().isAutoRetaliate());
        });
        bind("Special attack", player -> {
            if (player.isLocked()) {
                return;
            }
            player.getCombatDefinitions().setSpecial(!player.getCombatDefinitions().isUsingSpecial(), false);
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.COMBAT_TAB;
    }
}
