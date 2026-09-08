package org.jesse.game.world.entity.npc.impl.slayer.superior.impl;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/05/2019 02:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see org.jesse.game.world.entity.npc.impl.slayer.Jelly for combat script.
 */
public class VitreousJelly extends SuperiorNPC {
    public VitreousJelly(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7399, tile);
    }

    @Override
    protected String notificationName(@NotNull final Player player) {
        return "jellie";
    }
}
