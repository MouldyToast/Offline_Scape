package org.jesse.game.content.minigame.duelarena;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.Container;
import org.jesse.game.world.entity.player.container.ContainerPolicy;
import org.jesse.game.world.entity.player.container.impl.ContainerType;

import java.util.Optional;

/**
 * @author Tommeh | 28-11-2018 | 22:21
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DuelContainer extends Container {

    private final Player player;

    public DuelContainer(ContainerPolicy policy, ContainerType type, Optional<Player> player) {
        super(policy, type, player);
        this.player = player.get();
    }
}
