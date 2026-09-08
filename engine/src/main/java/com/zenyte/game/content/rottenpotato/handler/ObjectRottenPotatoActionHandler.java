package com.zenyte.game.content.rottenpotato.handler;

import com.zenyte.game.content.rottenpotato.RottenPotatoActionType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

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
