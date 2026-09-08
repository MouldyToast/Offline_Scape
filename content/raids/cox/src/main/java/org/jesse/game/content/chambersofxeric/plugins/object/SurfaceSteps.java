package org.jesse.game.content.chambersofxeric.plugins.object;

import org.jesse.game.content.chambersofxeric.dialogue.LeaveRaidD;
import org.jesse.game.content.chambersofxeric.dialogue.ReloadRaidD;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.entity.player.dialogue.PlainMessage;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;


import static org.jesse.game.content.chambersofxeric.Raid.outsideTile;

/**
 * @author Kris | 06/07/2019 03:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SurfaceSteps implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name,
                                   final int optionId, final String option) {
        final var optionalRaid = player.getRaid();
        if (optionalRaid.isEmpty()) {
            player.setLocation(outsideTile);
            return;
        }
        final var raid = optionalRaid.get();
        switch(option) {
            case "Climb" -> sendDialogue(player, new LeaveRaidD(player, raid));
            case "Reload" -> {
                var party = raid.getParty();
                var partyOwner = party.getPlayer().equalsIgnoreCase(player.getUsername());
                // check if the player is not the leader
                if (!partyOwner) {
                    sendDialogue(player, new PlainChat(player, "You need to be the party leader to do that."));
                    return;
                }
                // prompt dialogue for reloading the raid
                sendDialogue(player, new ReloadRaidD(player, raid));
            }
        }
    }

    private void sendDialogue(Player player, Dialogue dialogue) {
        player.getDialogueManager().start(dialogue);
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.STEPS_29778};
    }

}
