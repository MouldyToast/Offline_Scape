package org.jesse.game.content.rottenpotato.handler.player;

import org.jesse.api.service.sanction.SanctionPlayerExtKt;
import org.jesse.game.content.rottenpotato.handler.PlayerRottenPotatoActionHandler;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;

/**
 * @author Christopher
 * @since 3/23/2020
 */
public class Ban implements PlayerRottenPotatoActionHandler {
    @Override
    public void execute(Player reporter, Player offender) {
        SanctionPlayerExtKt.submitInfiniteAccountBan(reporter, offender, "Rotten Potato");
    }

    @Override
    public String option() {
        return "Ban";
    }

    @Override
    public PlayerPrivilege getPrivilege() {
        return PlayerPrivilege.MODERATOR;
    }
}
