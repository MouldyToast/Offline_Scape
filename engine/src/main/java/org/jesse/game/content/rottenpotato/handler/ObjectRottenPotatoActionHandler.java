package org.jesse.game.content.rottenpotato.handler;

import org.jesse.game.content.rottenpotato.RottenPotatoActionType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-12-03
 */
public interface ObjectRottenPotatoActionHandler extends RottenPotatoActionHandler {
    void execute(final Player user, final WorldObject target);

    @Override
    default RottenPotatoActionType type() {
        return RottenPotatoActionType.ITEM_ON_OBJECT;
    }
}
