package org.jesse.game.content.skills.farming.seedvault;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.privilege.GameMode;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

public class SeedVaultObject implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (player.getGameMode() == GameMode.ULTIMATE_IRON_MAN) {
            player.sendMessage("You can not use seed vault as an ultimate iron man.");
            return;
        }
        GameInterface.SEED_VAULT.open(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.SEED_VAULT};
    }
}
