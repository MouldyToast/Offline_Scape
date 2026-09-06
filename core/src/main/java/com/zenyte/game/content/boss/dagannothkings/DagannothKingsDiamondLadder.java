package com.zenyte.game.content.boss.dagannothkings;

import com.zenyte.game.content.skills.slayer.SlayerKeys;
import com.near_reality.game.content.slayer.Assignment;
import com.near_reality.game.content.slayer.BossTask;
import com.near_reality.game.content.slayer.RegularTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Leviticus | 04-28-2025 | 09:26
 * @see <a href="https://rune-server.org/members/leviticus.180707/">Rune-Server profile</a>}
 */
public class DagannothKingsDiamondLadder implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Climb")) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options(TITLE, new DialogueOption("Standard", () -> {
                        player.useStairs(828, new Location(2900, 4449, 0), 1, 2);
                    }), new DialogueOption("Slayer", () -> {
                        final Assignment assignment = SlayerKeys.slayer(player).getAssignment();
                        if (assignment == null || (assignment.getTask() != RegularTask.DAGANNOTH && assignment.getTask() != BossTask.DAGANNOTH_KINGS)) {
                            player.getDialogueManager().start(new PlainChat(player, "You need to be on a dagannoths slayer task to access the slayer-only dungeon."));
                            return;
                        }
                        player.useStairs(828, new Location(2899, 4385, 0), 1, 2);
                    }), new DialogueOption("Peek", () -> {
                        player.sendMessage("You peek through the crack...");
                        WorldTasksManager.schedule(() -> {
                            final int playerCount = GlobalAreaManager.get("Dagannoth Kings Lair")
                                    .getPlayers()
                                    .size();
                            final int slayerPlayerCount = GlobalAreaManager.get("Dagannoth Kings Slayer-Only Lair")
                                    .getPlayers()
                                    .size();
                            player.sendMessage("Standard cave: " + (playerCount == 0 ? "No adventurers." : playerCount + (playerCount == 1 ? " adventurer." : " adventurers.")));
                            player.sendMessage("Slayer cave: " + (slayerPlayerCount == 0 ? "No adventurers." : slayerPlayerCount + (slayerPlayerCount == 1 ? " adventurer." : " adventurers.")));
                        }, 2);
                    }), new DialogueOption("Cancel"));
                }
            });
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {4383};
    }
}
