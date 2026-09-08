package org.jesse.game.world.entity.npc.impl.slayer.superior.impl;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/05/2019 01:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CrushingHand extends SuperiorNPC {
    public CrushingHand(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7388, tile);
    }
}
