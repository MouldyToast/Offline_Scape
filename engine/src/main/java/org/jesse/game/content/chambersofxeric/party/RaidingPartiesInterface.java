package org.jesse.game.content.chambersofxeric.party;

import org.jesse.game.GameConstants;
import org.jesse.game.GameInterface;
import org.jesse.game.content.clans.ClanChannel;
import org.jesse.game.content.clans.ClanManager;
import org.jesse.game.content.clans.ClanRank;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.net.packet.PacketDispatcher;
import org.jesse.game.util.Colour;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;
import org.jesse.utils.TimeUnit;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import static org.jesse.game.content.chambersofxeric.party.RaidParty.advertisedParties;

/**
 * @author Kris | 08/07/2019 00:29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RaidingPartiesInterface extends Interface {
    /**
     * Refreshes the raiding parties interface where all the advertised parties are being displayed, with what's currently being advertised.
     *
     * @param player the player who is viewing the interface.
     */
    public static final void refresh(@NotNull final Player player) {
        final ClanChannel channel = player.getSettings().getChannel();
        player.getVarManager().sendVar(1427, channel == null || channel.getRaidParty() == null ? -1 : channel.getRaidParty().getUid());
        GameInterface.RAIDING_PARTIES.open(player);
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        for (int i = 0; i < 40; i++) {
            if (i >= advertisedParties.size()) {
                dispatcher.sendClientScript(1566, i, "");
                continue;
            }
            final RaidParty party = advertisedParties.get(i);
            if (party == null) {
                dispatcher.sendClientScript(1566, i, "");
                continue;
            }
            final String name = Colour.WHITE.wrap(StringFormatUtil.formatString(StringFormatUtil.formatString(party.getPlayer())));
            final int amount = party.getChannel().getMembers().size();
            final int time = (int) TimeUnit.MILLISECONDS.toTicks(Utils.currentTimeMillis() - party.getMilliseconds());
            dispatcher.sendClientScript(1566, i,
                name + "|" +
                    amount + "|" +
                    party.getPreferredPartySize() + "|" +
                    party.getPreferredCombatLevel() + "|" +
                    party.getPreferredSkillTotal() + "|" +
                    party.getRaid().getCurrentPlayerScale() + "|" +
                    (party.isChallengeMode() ? 1 : 0) + "|" + time);
        }
    }

    @Override
    protected void attach() {
        put(3, 0, "Refresh");
        put(3, 1, "Make party");
        put(15, "View party");
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    protected void build() {
        bind("Refresh", RaidingPartiesInterface::refresh);
        bind("Make party", player -> {
            final ClanChannel channel = player.getSettings().getChannel();
            if (channel == null) {
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        plain("You must be in a chat-channel to create a party.").executeAction(() -> RaidingPartiesInterface.refresh(player));
                    }
                });
                return;
            }
            if (GameConstants.SERVER_CHANNEL_NAME.equalsIgnoreCase(channel.getOwner()) && !player.getPrivilege().inherits(PlayerPrivilege.ADMINISTRATOR)) {
                player.sendMessage("You cannot create a raiding party out of the main " + GameConstants.SERVER_CHANNEL_NAME + " channel.");
                return;
            }
            if (channel.getRaidParty() != null) {
                channel.getRaidParty().refresh(player);
                return;
            }
            if (ClanManager.getRank(player, channel).ordinal() < ClanRank.RECRUIT.ordinal()) {
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        plain("Only players with the rank of Recruit can create a party.");
                    }
                });
                return;
            }
            if (channel.getRaidParty() == null) {
                channel.setRaidParty(new RaidParty(player, channel));
            }
            channel.getRaidParty().refresh(player);
        });
        bind("View party", (player, slotId, itemId, option) -> {
            final RaidParty party = advertisedParties.get(slotId);
            if (party == null) {
                return;
            }
            player.getTemporaryAttributes().put("viewingRaidParty", party);
            party.refresh(player);
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.RAIDING_PARTIES;
    }
}
