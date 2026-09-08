package org.jesse.game.content.tombsofamascut.npc;

import org.jesse.game.content.tombsofamascut.encounter.WardenEncounter;
import org.jesse.game.content.tombsofamascut.lobby.TOALobbyParty;
import org.jesse.game.content.tombsofamascut.raid.TOARaidArea;
import org.jesse.game.content.tombsofamascut.raid.TOARaidParty;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Savions
 */
public class WardensOsmumtenAction extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> dialogue(player));
        bind("Begin", (player, npc) -> handle(player));
    }

    private void dialogue(final Player player) {
        //TODO
    }

    private void handle(final Player player) {
        TOARaidParty party = (TOARaidParty) player.getTOAManager().getRaidParty();
        if (party == null) {
            return;
        }
        final TOARaidArea current = party.getCurrentRaidArea();
        if (current instanceof final WardenEncounter wardenEncounter) {
            wardenEncounter.setReady(player);
        }
    }

    @Override
    public int[] getNPCs() {
        return new int[] {WardenEncounter.OSMUMTEN_NPC_ID};
    }
}
