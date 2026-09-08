package org.jesse.game.content.rottenpotato.handler;

import org.jesse.game.content.rottenpotato.RottenPotatoActionType;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Christopher
 * @since 3/23/2020
 */
public interface PlayerRottenPotatoActionHandler extends RottenPotatoActionHandler {
    void execute(final Player user, final Player target);

    @Override
    default RottenPotatoActionType type() {
        return RottenPotatoActionType.ITEM_ON_PLAYER;
    }
}
