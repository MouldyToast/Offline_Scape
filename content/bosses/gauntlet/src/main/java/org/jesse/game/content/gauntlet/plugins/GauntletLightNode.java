package org.jesse.game.content.gauntlet.plugins;

import org.jesse.game.content.gauntlet.GauntletPlayerAttributesKt;
import org.jesse.game.content.gauntlet.GauntletConstants;
import org.jesse.game.content.gauntlet.map.GauntletMap;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.RegionArea;

import java.util.Optional;

/**
 * Plugin that handles functionality for lighting nodes in a Gauntlet instance.
 *
 * @author Andys1814.
 * @since 1/19/2022.
 */
public final class GauntletLightNode implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final RegionArea area = player.getArea();
        if (!(area instanceof GauntletMap))
            return;
        Optional.ofNullable(GauntletPlayerAttributesKt.getGauntlet(player))
                .ifPresent(gauntlet -> gauntlet.lightRoom(player, object));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                GauntletConstants.UNLIT_NODE_RIGHT,
                GauntletConstants.UNLIT_NODE_LEFT,
                35998, 35999
        };
    }


}
