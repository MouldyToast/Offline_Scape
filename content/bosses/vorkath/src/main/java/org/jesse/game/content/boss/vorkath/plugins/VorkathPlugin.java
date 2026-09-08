package org.jesse.game.content.boss.vorkath.plugins;

import org.jesse.game.content.boss.vorkath.VorkathNPC;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.pathfinding.events.player.EntityEvent;
import org.jesse.game.world.entity.pathfinding.strategy.EntityStrategy;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 27/11/2018 11:38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class VorkathPlugin extends NPCPlugin {

    private static final Animation POKE_ANIM = new Animation(827);

    private static final Animation VORKATH_RISE_ANIM = new Animation(7950);

    private static final SoundEffect pokeSound = new SoundEffect(2581);

    private static final SoundEffect vorkathWakeSound = new SoundEffect(1522, 10, 0);

    private static final SoundEffect vorkathWakeSoundContinue = new SoundEffect(1518, 10, 120);

    @Override
    public void handle() {
        bind("Poke", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                if (!(npc instanceof VorkathNPC)) {
                    return;
                }
                VorkathNPC vorkath = (VorkathNPC) npc;
                if (player != vorkath.getInstance().getPlayer()) {
                    player.sendMessage("Vorkath doesn\'t seem to respond to your poking.");
                    return;
                }
                if (vorkath.isLocked()) {
                    return;
                }
                player.lock(1);
                player.setAnimation(POKE_ANIM);
                player.sendSound(pokeSound);
                vorkath.lock();
                WorldTasksManager.schedule(new WorldTask() {

                    private int ticks;

                    @Override
                    public void run() {
                        if (ticks++ == 0) {
                            vorkath.setAnimation(VORKATH_RISE_ANIM);
                            vorkath.setTransformation(8058);
                            final Location middle = vorkath.getMiddleLocation();
                            vorkathWakeSound.sendGlobal(middle);
                            vorkathWakeSoundContinue.sendGlobal(middle);
                            return;
                        }
                        player.getBossTimer().startTracking("Vorkath");
                        vorkath.setTransformation(8061);
                        vorkath.unlock();
                        stop();
                    }
                }, 1, 6);
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new EntityStrategy(npc), () -> {
                    player.stopAll();
                    player.faceEntity(npc);
                    handle(player, npc);
                }, true));
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.VORKATH_8059 };
    }
}
