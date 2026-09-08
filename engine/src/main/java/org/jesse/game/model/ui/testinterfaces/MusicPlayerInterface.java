package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.util.AccessMask;
import org.jesse.game.world.entity.player.MusicHandler;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.Setting;

/**
 * @author Kris | 24/10/2018 15:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MusicPlayerInterface extends Interface {


    @Override
    protected void attach() {
        put(6, "Play song");
        put(9, "Song name");
        put(10, "Auto");
        put(13, "Manual");
        put(16, "Loop"); // This is random now
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Play song"), 0,
                MusicHandler.MUSIC_SLOT_NAME_ENUM.getSize() * 2, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2);

        player.getMusic().playRandomTrack();
    }

    @Override
    protected void build() {
        bind("Auto", player -> {
            player.getSettings().setSetting(Setting.AUTO_MUSIC, 1);
            player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Song name"), "AUTO");
        });
        bind("Manual", player -> {
            player.getSettings().setSetting(Setting.AUTO_MUSIC, 0);
            player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Song name"), "MANUAL");
        });
        bind("Loop", player -> {
            player.getSettings().toggleSetting(Setting.LOOP_MUSIC);
            player.sendMessage("Music looping now " + (player.getBooleanSetting(Setting.LOOP_MUSIC) ? "enabled." : "disabled."));
        });
        bind("Play song", (player, slotId, itemId, option) -> {
            if (option == 1) {
                if (!player.getMusic().play(slotId + 1)) {
                    player.sendMessage("You have not unlocked this piece of music yet!");
                }
            } else if (option == 2) {
                player.getMusic().sendUnlockHint(slotId + 1);
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.MUSIC_TAB;
    }
}
