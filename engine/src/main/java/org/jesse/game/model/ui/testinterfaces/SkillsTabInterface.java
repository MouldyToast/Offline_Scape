package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.Player;

public class SkillsTabInterface extends Interface {

    @Override
    public DefaultClickHandler getDefaultHandler() {
        return (player, componentId, slotId, itemId, optionId) -> {
            if (optionId == 1) { // if player is on mobile and clicking skill to see current exp
                return;
            }
            if (player.isLocked()) {
                return;
            }
            if (player.isUnderCombat()) {
                player.sendMessage("You can't do this while in combat.");
                return;
            }
            player.getSkills().sendSkillMenu(componentId, 0);
        };
    }

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
        return GameInterface.SKILLS_TAB;
    }
}
