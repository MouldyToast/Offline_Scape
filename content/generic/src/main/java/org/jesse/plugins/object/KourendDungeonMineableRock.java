package org.jesse.plugins.object;

import org.jesse.game.content.skills.mining.MiningDefinitions;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.CollisionUtil;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.pathfinding.events.npc.NPCObjectEvent;
import org.jesse.game.world.entity.pathfinding.events.player.ObjectEvent;
import org.jesse.game.world.entity.pathfinding.strategy.ObjectStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.CharacterLoop;
import org.jesse.plugins.dialogue.PlainChat;

import java.util.Optional;

/**
 * @author Kris | 10/05/2019 16:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KourendDungeonMineableRock implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Mine")) {
            final Optional<MiningDefinitions.PickaxeDefinitions.PickaxeResult> axe = MiningDefinitions.PickaxeDefinitions.get(player, true);
            if (!axe.isPresent()) {
                player.getDialogueManager().start(new PlainChat(player, "You need a pickaxe to mine this rock. You do not have a pickaxe which you have the Mining level to use."));
                return;
            }
            player.setAnimation(axe.get().getDefinition().getAnim());
            WorldTasksManager.schedule(() -> {
                player.setAnimation(Animation.STOP);
                if (World.containsObjectWithId(object, object.getId())) {
                    World.removeObject(object);
                    WorldTasksManager.schedule(() -> {
                        CharacterLoop.forEach(object, 1, Entity.class, entity -> {
                            if (CollisionUtil.collides(object.getX(), object.getY(), 2, entity.getX(), entity.getY(), entity.getSize())) {
                                if (entity instanceof Player) {
                                    entity.setRouteEvent(new ObjectEvent(((Player) entity), new ObjectStrategy(object), null));
                                } else {
                                    entity.setRouteEvent(new NPCObjectEvent(((NPC) entity), new ObjectStrategy(object)));
                                }
                            }
                        });
                        World.spawnObject(object);
                    }, 100);
                }
            }, 3);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ROCKS_28890 };
    }
}
