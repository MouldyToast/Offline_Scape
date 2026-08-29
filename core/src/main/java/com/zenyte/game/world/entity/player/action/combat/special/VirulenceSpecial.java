package com.zenyte.game.world.entity.player.action.combat.special;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.action.combat.SpecialAttackScript;

import java.util.Arrays;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-17
 */
public class VirulenceSpecial implements SpecialAttackScript {
    @Override
    public void attack(Player player, PlayerCombat combat, Entity target) {
        if (!player.getToxins().isVenomed() && !player.getToxins().isPoisoned()) {
            return;
        }
        Arrays.stream(Toxins.ToxinType.values()).forEach(toxin -> player.getToxins().cureToxin(toxin));
    }
}
