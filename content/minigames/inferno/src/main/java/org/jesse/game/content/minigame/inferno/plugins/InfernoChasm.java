package org.jesse.game.content.minigame.inferno.plugins;

import org.jesse.game.content.minigame.inferno.instance.Inferno;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent;
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.cutscene.actions.FadeScreenAction;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.ObjectHandler;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.dynamicregion.AllocatedArea;
import org.jesse.game.world.region.dynamicregion.MapBuilder;
import org.jesse.game.world.region.dynamicregion.OutOfSpaceException;
import org.jesse.plugins.dialogue.PlainChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris | 14. apr 2018 : 17:27.15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class InfernoChasm implements ObjectAction {

    private static final Logger log = LoggerFactory.getLogger(InfernoChasm.class);

    private static final Location start = new Location(2496, 5115, 0);

    private static final Animation jump = new Animation(6723);

    private static final Location pit = new Location(2496, 5125, 0);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setAnimation(jump);
        player.setFaceLocation(pit);
        player.lock();
        WorldTasksManager.schedule(new WorldTask() {

            Inferno inferno;

            int ticks;

            @Override
            public void run() {
                switch(ticks++) {
                    case 0:
                        player.autoForceMovement(pit, 0, 180);
                        return;
                    case 3:
                        new FadeScreenAction(player, 12).run();
                        player.getDialogueManager().start(new PlainChat(player, "You jump into the fiery cauldron of The Inferno; your heart is pulsating.", false));
                        return;
                    case 6:
                        player.getAppearance().setInvisible(true);
                        return;
                    case 9:
                        player.getDialogueManager().start(new PlainChat(player, "You fall and fall and feel the temperature rising.", false));
                        return;
                    case 12:
                        player.getDialogueManager().start(new PlainChat(player, "Your heart is in your throat.", false));
                        return;
                    case 15:
                        try {
                            final AllocatedArea area = MapBuilder.findEmptyChunk(11, 10);
                            inferno = new Inferno(player, area, false);
                            inferno.constructRegion();
                        } catch (OutOfSpaceException e) {
                            log.error("", e);
                        }
                        stop();
                }
            }
        }, 1, 0);
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(start), () -> {
            player.stopAll();
            player.faceObject(object);
            if (!ObjectHandler.handleOptionClick(player, optionId, object)) {
                return;
            }
            handleObjectAction(player, object, name, optionId, option);
        }));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 30352 };
    }
}
