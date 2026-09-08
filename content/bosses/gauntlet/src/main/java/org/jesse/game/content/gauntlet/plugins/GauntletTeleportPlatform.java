package org.jesse.game.content.gauntlet.plugins;

import org.jesse.game.content.gauntlet.GauntletPlayerAttributesKt;
import org.jesse.game.content.gauntlet.Gauntlet;
import org.jesse.game.content.gauntlet.rewards.GauntletRewardType;
import org.jesse.game.content.gauntlet.map.GauntletMap;
import org.jesse.game.model.ui.InterfacePosition;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Andys1814.
 * @since 2/7/2022.
 */
public final class GauntletTeleportPlatform implements ObjectAction {

    private static final int TELEPORT_PLATFORM_IN_GAME = 36062;

    private static final int TELEPORT_PLATFORM_IN_GAME_CORRUPTED = 35965;

    private static final int TELEPORT_PLATFORM_LOBBY = 36082;

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (object.getId() == TELEPORT_PLATFORM_IN_GAME || object.getId() == TELEPORT_PLATFORM_IN_GAME_CORRUPTED) {
            if (!(player.getArea() instanceof GauntletMap)) {
                return;
            }

            boolean quickExit = optionId == 2;

            if (quickExit) {
                exit(player);
            } else {
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        plain("Are you sure you wish to exit the Gauntlet? All of your progress will be lost and you will start again upon re-entering.");
                        options("Exit the Gauntlet?", new DialogueOption("Yes, let me out.", () -> exit(player)), new DialogueOption("No."));
                    }
                });
            }
        } else if (object.getId() == TELEPORT_PLATFORM_LOBBY) {
            player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 641);
            player.getPacketDispatcher().sendClientScript(2921);

            WorldTasksManager.schedule(() -> {
                player.setLocation(new Location(3111 + Utils.random(0, 1), 3489, 0));
                player.getPacketDispatcher().sendClientScript(2922);
            }, 1);
        }
    }

    private void exit(Player player) {
        final Gauntlet gauntlet = GauntletPlayerAttributesKt.getGauntlet(player);
        if (gauntlet == null) {
            player.sendMessage("You are not currently in a Gauntlet dungeon!");
            return;
        }
        gauntlet.end(GauntletRewardType.NONE, true, false);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { TELEPORT_PLATFORM_IN_GAME, TELEPORT_PLATFORM_IN_GAME_CORRUPTED, TELEPORT_PLATFORM_LOBBY };
    }

}
