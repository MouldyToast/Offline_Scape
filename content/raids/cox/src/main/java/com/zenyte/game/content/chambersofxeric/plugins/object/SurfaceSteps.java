package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.content.chambersofxeric.RaidAccess;
import com.zenyte.game.content.chambersofxeric.dialogue.LeaveRaidD;
import com.zenyte.game.content.chambersofxeric.dialogue.ReloadRaidD;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.PlainMessage;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;


import static com.zenyte.game.content.chambersofxeric.Raid.outsideTile;

/**
 * @author Kris | 06/07/2019 03:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SurfaceSteps implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name,
                                   final int optionId, final String option) {
        final var optionalRaid = RaidAccess.raid(player);
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
