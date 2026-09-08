package org.jesse.game.content.rottenpotato.handler.npc;

import org.jesse.game.content.rottenpotato.handler.NpcRottenPotatoActionHandler;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;

/**
 * @author Christopher
 * @since 3/27/2020
 */
public class KillNpc implements NpcRottenPotatoActionHandler {
    @Override
    public void execute(Player user, NPC target) {
        target.forceKilled = true;
        target.applyHit(new Hit(user, target.getMaxHitpoints(), HitType.POISON));
    }

    @Override
    public String option() {
        return "Kill NPC";
    }

    @Override
    public PlayerPrivilege getPrivilege() {
        return PlayerPrivilege.ADMINISTRATOR;
    }
}
