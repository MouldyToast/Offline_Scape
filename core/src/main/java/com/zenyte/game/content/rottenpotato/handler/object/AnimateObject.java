package com.zenyte.game.content.rottenpotato.handler.object;

import com.zenyte.game.content.rottenpotato.handler.ObjectRottenPotatoActionHandler;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.game.world.object.WorldObject;

import java.util.Objects;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-12-03
 */
public class AnimateObject implements ObjectRottenPotatoActionHandler {
    @Override
    public void execute(Player user, WorldObject target) {
        user.sendInputInt("Enter the Animation ID", animId -> {
            var def = target.getDefinitions();
            if (Objects.isNull(def)) {
                user.sendMessage("No definitions for this object.");
                return;
            }
            user.sendMessage("Animating object: %d with seq: %d".formatted(target.getId(), animId));
            def.setAnimationId(animId);
        });
    }

    @Override
    public String option() {
        return "Animate World Objects";
    }

    @Override
    public PlayerPrivilege getPrivilege() {
        return PlayerPrivilege.ADMINISTRATOR;
    }
}
