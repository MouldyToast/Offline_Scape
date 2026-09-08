package org.jesse.game.content.rottenpotato.handler.player;

import org.jesse.game.content.rottenpotato.handler.PlayerRottenPotatoActionHandler;
import org.jesse.game.content.skills.farming.contract.FarmingContract;
import org.jesse.game.util.Colour;
import org.jesse.game.world.entity.player.LogLevel;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;

public class RemoveFarmContract implements PlayerRottenPotatoActionHandler {

    @Override
    public void execute(Player user, Player target) {
        target.getAttributes().remove(FarmingContract.CONTRACT_ATTR);
        target.getAttributes().remove(FarmingContract.CONTRACT_DIFFICULTY_ATTR);
        target.putBooleanAttribute(FarmingContract.COMPLETED_ATTR, false);
        target.log(LogLevel.INFO, "Farming contracted cleared by " + user.getName() + ".");
        user.sendMessage("Successfully cleared farming contract for " + Colour.RED.wrap(target.getUsername()) + ".");
    }

    @Override
    public String option() {
        return "Clear farming contract";
    }

    @Override
    public PlayerPrivilege getPrivilege() {
        return PlayerPrivilege.ADMINISTRATOR;
    }
}
