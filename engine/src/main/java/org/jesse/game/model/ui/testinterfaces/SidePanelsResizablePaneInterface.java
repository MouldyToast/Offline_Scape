package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.content.gravestone.GravestoneExt;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.Setting;
import org.jesse.game.world.entity.player.var.VarCollection;
import org.jesse.logger.NearRealityLogger;
import org.slf4j.Logger;

/**
 * @author Tommeh | 18-1-2019 | 15:21
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SidePanelsResizablePaneInterface extends Interface {

    private final Logger logger = NearRealityLogger.getLogger(SidePanelsResizablePaneInterface.class);

    @Override
    protected void attach() {
        put(12, "helper_cox");
        put(20, "Gravestone info");
        put(38, "Open Game Noticeboard");
        put(53, "Character Summary");
        put(57, "Toggle prayer filtering");
        put(58, "Toggle spell filtering");
    }

    @Override
    public void open(Player player) {
        throw new IllegalStateException("Panes cannot be opened as interfaces.");
    }

    @Override
    protected void build() {
        bind("Character Summary", (player, slotId, itemId, option) -> VarCollection.COMP_PROGRESS.updateSingle(player));
        bind("Toggle spell filtering", (player, slotId, itemId, option) -> {
            if (option == 2)
                player.getSettings().toggleSetting(Setting.SPELL_FILTERING_DISABLED);

            else if (option == 3) {
                var previousDestination = player.getTeleportsManager().getPreviousDestination();
                if (previousDestination != null)
                    player.getTeleportsManager().attemptTeleport(previousDestination);
            }
            logger.info("Magic Tab | Option: {}", option);
        });
        bind("Toggle prayer filtering", (player, slotId, itemId, option) -> {
            if (option == 2) {
                player.getSettings().toggleSetting(Setting.PRAYER_FILTER_DISABLE);
            }
        });
        bind("Gravestone info", GravestoneExt.INSTANCE::informGravestone);
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.SIDE_PANELS_RESIZABLE_PANE;
    }
}
