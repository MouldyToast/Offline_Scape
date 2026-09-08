package org.jesse.game.content.skills.agility;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

public interface Irreversible {
    boolean checkForReverse(final Player player, final WorldObject object);
    boolean failOnReverse();
    default void onReverse(final Player player, final WorldObject object){}
}
