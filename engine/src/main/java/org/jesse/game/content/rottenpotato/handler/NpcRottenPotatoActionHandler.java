package org.jesse.game.content.rottenpotato.handler;

import org.jesse.game.content.rottenpotato.RottenPotatoActionType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Christopher
 * @since 3/27/2020
 */
public interface NpcRottenPotatoActionHandler extends RottenPotatoActionHandler {
    void execute(final Player user, final NPC target);

    @Override
    default RottenPotatoActionType type() {
        return RottenPotatoActionType.ITEM_ON_NPC;
    }
}
