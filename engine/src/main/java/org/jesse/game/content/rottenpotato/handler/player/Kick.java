package org.jesse.game.content.rottenpotato.handler.player;

import org.jesse.game.content.rottenpotato.handler.PlayerRottenPotatoActionHandler;
import org.jesse.game.world.entity.player.LogLevel;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;

public class Kick implements PlayerRottenPotatoActionHandler {
    @Override
    public void execute(Player user, Player target) {
        target.log(LogLevel.INFO, "Forcefully kicked by " + user.getName() + ".");
        target.logout(true);
        user.sendMessage("Successfully kicked <col=C22731>" + target.getUsername() + "</col>!");
    }

    @Override
    public String option() {
        return "Kick player";
    }

    @Override
    public PlayerPrivilege getPrivilege() {
        return PlayerPrivilege.SUPPORT;
    }
}
