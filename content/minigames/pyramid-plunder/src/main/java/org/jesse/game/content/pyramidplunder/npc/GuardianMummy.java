package org.jesse.game.content.pyramidplunder.npc;

import org.jesse.game.content.pyramidplunder.PlunderRoom;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Christopher
 * @since 4/1/2020
 */
public class GuardianMummy extends NPCPlugin {
    public static void startGame(@NotNull final Player player) {
        PlunderRoom.reset(player);
        player.setLocation(PlunderRoom.FIRST_ROOM.getStart());
        player.lock(1);
    }

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new GuardianMummyDialogue(player, npc, false)));
        bind("Start-minigame", (player, npc) -> startGame(player));
    }

    @Override
    public int[] getNPCs() {
        return new int[]{NpcId.GUARDIAN_MUMMY};
    }
}
