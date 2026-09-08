package org.jesse.game.content.kebos.konar.plugins.objects;

import org.jesse.game.content.kebos.konar.map.KaruulmSlayerDungeon;
import org.jesse.game.content.kebos.konar.map.KaruulmSlayerDungeonLobby;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.DirectionUtil;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.GlobalAreaManager;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * @author Tommeh | 14/10/2019 | 19:48
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class RocksObstacle implements ObjectAction {

    private static final Animation climb = new Animation(839);

    private enum Rocks {

        NORTH(0, (player, object) -> {
            if (player.getY() < object.getY()) {
                // go north
                return player.getLocation().transform(0, 2, 0);
            } else {
                // go south
                return player.getLocation().transform(0, -2, 0);
            }
        }), WEST(3, (player, object) -> {
            if (player.getX() < object.getX()) {
                // go east
                return player.getLocation().transform(2, 0, 0);
            } else {
                // go west
                return player.getLocation().transform(-2, 0, 0);
            }
        }), EAST(1, (player, object) -> {
            if (player.getX() < object.getX()) {
                // go east
                return player.getLocation().transform(2, 0, 0);
            } else {
                // go west
                return player.getLocation().transform(-2, 0, 0);
            }
        });

        private final int rotation;

        private final BiFunction<Player, WorldObject, Location> handler;

        private static final Set<Rocks> all = EnumSet.allOf(Rocks.class);

        private static final Map<Integer, Rocks> rocks = new HashMap<>(all.size());

        static {
            for (final RocksObstacle.Rocks r : all) {
                rocks.put(r.rotation, r);
            }
        }

        public static Rocks get(final int rotation) {
            return rocks.get(rotation);
        }

        Rocks(int rotation, BiFunction<Player, WorldObject, Location> handler) {
            this.rotation = rotation;
            this.handler = handler;
        }

        public int getRotation() {
            return rotation;
        }

        public BiFunction<Player, WorldObject, Location> getHandler() {
            return handler;
        }
    }

    @Override
    public int getDelay() {
        return 2;
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final int rotation = object.getRotation();
        final RocksObstacle.Rocks rocks = Rocks.get(rotation);
        if (rocks == null) {
            return;
        }
        final BiFunction<Player, WorldObject, Location> handler = rocks.getHandler();
        final Location destination = handler.apply(player, object);
        final Location currentTile = new Location(player.getLocation());
        final int direction = DirectionUtil.getFaceDirection(destination.getX() - currentTile.getX(), destination.getY() - currentTile.getY());
        if (object.getId() == ObjectId.ROCKS_34548) {
            // rocks that lead to the alchemical hydra
            climb(player, destination, direction, true);
            return;
        }
        if (KaruulmSlayerDungeon.isProtectedAgainstHeat(player) || !GlobalAreaManager.getArea(KaruulmSlayerDungeonLobby.class).getPlayers().contains(player)) {
            climb(player, destination, direction, true);
        } else {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    plain("Warning: The heat of the ground beyond this point can burn you as<br><br>you walk upon it. It is recommended you wear appropriate boots for<br><br>this.");
                    options("Proceed regardless?", "Yes.", "No.").onOptionOne(() -> climb(player, destination, direction, false));
                }
            });
        }
    }

    private static void climb(final Player player, final Location destination, final int direction, final boolean protection) {
        player.lock(3);
        player.setAnimation(climb);
        player.autoForceMovement(destination, 0, 60, direction);
        WorldTasksManager.schedule(new WorldTask() {

            int ticks;

            @Override
            public void run() {
                if (ticks == 0) {
                    player.sendFilteredMessage("You climb over the rocks.");
                } else if (ticks == 2) {
                    if (!protection) {
                        player.sendFilteredMessage("The heat of the dungeon floor rapidly burns you.");
                    }
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ROCKS_34544 };
    }
}
