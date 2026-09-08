package org.jesse.game.content.gauntlet.plugins;

import org.jesse.game.content.commands.DeveloperCommands;
import org.jesse.game.model.ui.InterfacePosition;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;

public final class GauntletPortal implements ObjectAction {

    private static final int GAUNTLET_PORTAL = 36081;

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {

        if (!DeveloperCommands.INSTANCE.getEnabledGauntlet()){
            player.getDialogueManager().start(new PlainChat(player, "Gauntlet is currently disabled."));
            return;
        }

        player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 641);
        player.getPacketDispatcher().sendClientScript(2921);

        WorldTasksManager.schedule(() -> {
            player.setLocation(new Location(3030, 6128, 1));
            player.getPacketDispatcher().sendClientScript(2922);
        }, 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { GAUNTLET_PORTAL };
    }

}
