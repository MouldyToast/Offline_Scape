package org.jesse.game.world.region;

import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;

import java.util.List;

/**
 * @author Kris | 28. juuli 2018 : 02:53:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface MapSquare {

	List<NPC> getNPCs();
	
	List<Player> getPlayers();
	
}
