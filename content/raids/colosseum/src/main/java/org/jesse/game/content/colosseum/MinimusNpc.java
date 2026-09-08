package org.jesse.game.content.colosseum;

import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.region.RegionArea;

public class MinimusNpc extends NPCPlugin {

    @Override
    public void handle() {
        bind("Start-wave", (player, npc) -> {
            RegionArea area = player.getArea();
            if (!(area instanceof ColosseumInstance instance)) {
                return;
            }
            instance.openIntermission();
        });

        bind("Leave", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Are you sure you wish to leave?",
                            new DialogueOption("Yes.", () ->
                                    new FadeScreen(player, () ->
                                            player.setLocation(ColosseumInstance.SPAWN_LOCATION)).fade(3)),
                            new DialogueOption("No.")
                    );
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[]{NpcId.MINIMUS_12808};
    }
}