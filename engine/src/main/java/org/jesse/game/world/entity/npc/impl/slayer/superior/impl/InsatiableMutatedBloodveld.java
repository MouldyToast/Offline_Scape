package org.jesse.game.world.entity.npc.impl.slayer.superior.impl;

import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.calog.CAType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/05/2019 02:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see org.jesse.game.content.godwars.npcs.combat.BloodveldCombat for combat script.
 */
public class InsatiableMutatedBloodveld extends SuperiorNPC {
    public InsatiableMutatedBloodveld(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7398, tile);
    }

    @Override protected void onDeath(Entity source) {
        super.onDeath(source);
        if (source instanceof Player) {
            ((Player) source).getCombatAchievements().complete(CAType.THE_DEMONIC_PUNCHING_BAG);
        }
    }
}
