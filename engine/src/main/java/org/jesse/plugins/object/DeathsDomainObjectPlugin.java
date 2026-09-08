package org.jesse.plugins.object;

import org.jesse.game.GameInterface;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Analytics;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.DynamicArea;
import org.jesse.game.world.region.dynamicregion.AllocatedArea;
import org.jesse.game.world.region.dynamicregion.MapBuilder;
import org.jesse.game.world.region.dynamicregion.OutOfSpaceException;
import org.jesse.logger.NearRealityLogger;
import org.slf4j.Logger;

import static org.jesse.game.obj.ids.ObjectId.*;

/**
 * @author Kris | 14/06/2022
 */
@SuppressWarnings("unused")
public class DeathsDomainObjectPlugin implements ObjectAction {
    private static final Logger log = NearRealityLogger.getLogger(DeathsDomainObjectPlugin.class);
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final Location onLogin = player.getLocation().copy();
        try {
            final AllocatedArea area = MapBuilder.findEmptyChunk(4, 2);
            final DynamicArea dynamicArea = new DynamicArea(area, 395, 715) {
                @Override
                public void enter(Player player) {
                }

                @Override
                public void leave(Player player, boolean logout) {
                }

                @Override
                public String name() {
                    return "Deaths' Domain";
                }

                @Override
                public Location onLoginLocation() {
                    return onLogin;
                }

                @Override
                public void constructed() {
                    Analytics.flagInteraction(player, Analytics.InteractionType.DEATHS_DOMAIN);
                    GameInterface.DEATHS_OFFICE_ENTER_OVERLAY.open(player);
                    player.setAnimation(new Animation(827, 20));
                    player.sendSound(3952);
                    final NPC death = new NPC(9855, getLocation(3179, 5726, 0), true);
                    death.setRadius(0);
                    death.spawn();
                    player.getPacketDispatcher().sendClientScript(2893, 41549825, 41549826, 39504, 4128927, -1, -1);
                    WorldTasksManager.schedule(() -> {
                        player.setAnimation(Animation.STOP);
                        player.getPacketDispatcher().sendClientScript(2894, 41549825, 41549826, -1, -1);
                        player.setLocation(getLocation(3171, 5726, 0));
                        WorldTasksManager.schedule(() -> {
                            player.lock(1);
                            player.getInterfaceHandler().closeInterface(GameInterface.DEATHS_OFFICE_ENTER_OVERLAY);
                        });
                    }, 0);
                }
            };
            dynamicArea.constructRegion();
        } catch (OutOfSpaceException e) {
            log.error("", e);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
            DEATHS_DOMAIN,
            DEATHS_DOMAIN_39637,
            DEATHS_DOMAIN_39546,
            DEATHS_DOMAIN_39547,
            DEATHS_DOMAIN_39535,
            DEATHS_DOMAIN_39548,
        };
    }
}
