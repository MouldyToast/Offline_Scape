package org.jesse.game.content.rottenpotato.handler;

import org.jesse.game.content.rottenpotato.RottenPotatoActionType;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;

/**
 * @author Christopher
 * @since 3/23/2020
 */
public interface RottenPotatoActionHandler {
    String option();

    PlayerPrivilege getPrivilege();

    RottenPotatoActionType type();
}
