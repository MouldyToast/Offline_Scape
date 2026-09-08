package org.jesse.game.content.godwars.objects;

import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent;
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * @author Tommeh | 24-3-2019 | 13:46
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GodwarsArmadylGrappleObject implements ObjectAction {

    private static final Animation ANIMATION = new Animation(6067, 11);

    private static final Animation SOUTH_TO_NORTH_ANIM = new Animation(6071, 11);

    private static final Animation NORTH_TO_SOUTH_ANIM = new Animation(6072, 11);

    private static final Graphics GRAPHICS = new Graphics(1036, 11, 92);

    private static final Location NORTH_TO_SOUTH_LOCATION = new Location(2872, 5269, 2);

    private static final Location SOUTH_TO_NORTH_LOCATION = new Location(2872, 5279, 2);

    private static final Location PILLAR_LOCATION = new Location(2872, 5270, 2);

    @Override
    public void handle(Player player, WorldObject object, String name, int optionId, String option) {
        final Location location = player.getY() >= 5279 ? SOUTH_TO_NORTH_LOCATION : NORTH_TO_SOUTH_LOCATION;
        player.setRouteEvent(new TileEvent(player, new TileStrategy(location), getRunnable(player, object, name, optionId, option), getDelay()));
    }

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final WorldObject pillar = World.getObjectWithType(PILLAR_LOCATION, 10);
        if (player.getSkills().getLevel(SkillConstants.RANGED) < 70) {
            player.sendMessage("You need a Ranged level of at least 70 to do grapple the pillar.");
            return;
        }
        final Location location = player.getY() >= 5279 ? NORTH_TO_SOUTH_LOCATION : SOUTH_TO_NORTH_LOCATION;
        final Animation animation = player.getY() >= 5279 ? NORTH_TO_SOUTH_ANIM : SOUTH_TO_NORTH_ANIM;
        player.lock(5);
        player.faceObject(object);
        player.setAnimation(ANIMATION);
        player.setGraphics(GRAPHICS);
        player.sendMessage("You fire something you found nearby at the pillar...");
        World.sendObjectAnimation(pillar, animation);
        player.setTeleportForceMovement(location, OptionalInt.empty(), 4, 1, Optional.empty(), Optional.empty(), Optional.of(tile -> player.sendMessage("...and swing safely to the other side.")));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.PILLAR_26380 };
    }
}
