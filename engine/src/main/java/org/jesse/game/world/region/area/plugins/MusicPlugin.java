package org.jesse.game.world.region.area.plugins;


import org.jesse.game.model.music.Music;
import org.jesse.game.world.entity.player.Player;

import java.util.Set;

/**
 * @author Savions.
 */
public interface MusicPlugin {

    Set<Music> getMusics(Player player);
}
