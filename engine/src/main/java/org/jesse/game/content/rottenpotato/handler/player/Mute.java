package org.jesse.game.content.rottenpotato.handler.player;

import org.jesse.api.service.sanction.SanctionPlayerExtKt;
import org.jesse.game.content.rottenpotato.handler.PlayerRottenPotatoActionHandler;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;

/**
 * @author Christopher
 * @since 3/23/2020
 */
public class Mute implements PlayerRottenPotatoActionHandler {
    @Override
    public void execute(Player user, Player target) {
        SanctionPlayerExtKt.submitInfiniteAccountMuteFor(user, target, "Rotten Potato");
    }

    @Override
    public String option() {
        return "Mute";
    }

    @Override
    public PlayerPrivilege getPrivilege() {
        return PlayerPrivilege.SUPPORT;
    }
}
