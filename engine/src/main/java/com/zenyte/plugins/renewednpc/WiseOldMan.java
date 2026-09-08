package com.zenyte.plugins.renewednpc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.donation.DonationToggle;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.npc.ids.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.plugins.dialogue.WiseOldManD;

/**
 * @author Kris | 25/11/2018 16:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WiseOldMan extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {

            @Override
            public void handle(final Player player, final NPC npc) {
                player.getDialogueManager().start(new WiseOldManD(player, npc));
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });

        bind("Toggles", (player, npc) -> {
            if(player.isMember()) {
                DonationToggle.openInterface(player);
            } else {
                player.getDialogueManager().start(new NPCChat(player, npc.getId(), "You must be a member to do this."));
            }
        });

        bind("Titles", (player, npc) -> GameInterface.LOYALTY_TITLES.open(player));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.WISE_OLD_MAN };
    }
}
