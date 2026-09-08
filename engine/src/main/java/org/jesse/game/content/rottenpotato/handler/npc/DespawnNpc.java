package org.jesse.game.content.rottenpotato.handler.npc;

import org.jesse.game.content.rottenpotato.handler.NpcRottenPotatoActionHandler;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;

public class DespawnNpc implements NpcRottenPotatoActionHandler {
    @Override
    public void execute(Player user, NPC target) {
        target.finish();
    }

    @Override
    public String option() {
        return "Despawn NPC";
    }

    @Override
    public PlayerPrivilege getPrivilege() {
        return PlayerPrivilege.ADMINISTRATOR;
    }
}
