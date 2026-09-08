package org.jesse.plugins.renewednpc;

import org.jesse.game.GameInterface;
import org.jesse.game.content.donation.DonationToggle;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.impl.NPCChat;
import org.jesse.plugins.dialogue.WiseOldManD;

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
