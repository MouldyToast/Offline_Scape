package org.jesse.game.content.treasuretrails.npcs;

import org.jesse.game.world.entity.ForceTalk;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 12/04/2019 18:20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BandosianGuard extends TreasureGuardian {

    private static final ForceTalk forceTalk = new ForceTalk("Ggggggrrrrrr!!");

    public BandosianGuard(@NotNull final Player owner, @NotNull final Location tile) {
        super(owner, tile, 6588);
        setForceTalk(forceTalk);
    }

}
