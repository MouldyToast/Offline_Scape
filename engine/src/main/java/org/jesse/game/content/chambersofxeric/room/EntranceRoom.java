package org.jesse.game.content.chambersofxeric.room;

import org.jesse.game.content.chambersofxeric.Raid;
import org.jesse.game.content.chambersofxeric.map.RaidArea;
import org.jesse.game.content.chambersofxeric.map.RaidRoom;
import org.jesse.game.model.ui.GameTab;
import org.jesse.game.util.Colour;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;

/**
 * Handles the very first room of raids.
 *
 * @author Kris | 16. nov 2017 : 2:11.18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class EntranceRoom extends RaidArea {

    /**
     * The walking steps sound effect that is played when you walk into the raid.
     */
    private static final SoundEffect sound = new SoundEffect(2277);

    public EntranceRoom(final RaidRoom type, final Raid raid, final int rotation, final int size, final int regionX, final int regionY, final int chunkX, final int chunkY, final int fromPlane, final int toPlane) {
        super(type, raid, rotation, size, regionX, regionY, chunkX, chunkY, fromPlane, toPlane);
    }

    @Override
    public boolean canPass(final Player player, final WorldObject object) {
        if (raid.getStage() == 0) {
            if (raid.getParty().getPlayer().equals(player.getUsername())) {
                player.getDialogueManager().start(new PlainChat(player, "Use the controls on the " + Colour.RS_RED.wrap("side-panel") + "" +
                        "<br><br>when you are ready to begin the raid."));
                player.sendSound(sound);
                player.getInterfaceHandler().openGameTab(GameTab.CLAN_CHAT_TAB);
            } else {
                player.sendMessage("The party leader must start the party before you may pass.");
            }
            return false;
        }
        return true;
    }

    @Override
    public String name() {
        return "Chambers of Xeric: Entrance";
    }

}
