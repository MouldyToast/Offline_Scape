package com.zenyte.game.content.chambersofxeric.dialogue;

import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.party.RaidParty;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import java.util.Set;

import static com.zenyte.game.content.chambersofxeric.Raid.outsideTile;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-01-03
*/
public class ReloadRaidD extends Dialogue {

    private final Raid raid;
    private final Set<Player> formerPlayers;

    public ReloadRaidD(Player player, Raid raid) {
        super(player);
        this.raid = raid;
        formerPlayers = raid.getPlayers();
    }

    @Override
    public void buildDialogue() {
        final var channel = player.getSettings().getChannel();
        if (channel.getRaidParty().getRaid().getStage() != 0) {
            player.sendMessage("The raid has already been started.");
            return;
        }
        options("Would you like to reload this layout?", "Yes.", "Nevermind.")
            .onOptionOne(() -> {
                // Boot everyone out
                raid.getPlayers().stream()
                    .filter(p -> !raid.getParty().getPlayer().equalsIgnoreCase(p.getUsername()))
                    .forEach(this::removePlayer);
                // force enter the previous team, and await any stragglers
                channel.setRaidParty(new RaidParty(player, channel));
                channel.getRaidParty().setPlayer(player.getUsername());
                formerPlayers.forEach(teamMate -> channel.getRaidParty().getRaid().enterRaid(teamMate));
            })
            .onOptionTwo(() -> plain("Nevermind."));
    }

    private void removePlayer(Player player) {
        player.setLocation(outsideTile);
        player.getVarManager().sendBit(5432, 0);
        player.reset();
        player.setAnimation(null);
        player.setGraphics(null);
        player.lock(3);
        player.blockIncomingHits();
    }
}
