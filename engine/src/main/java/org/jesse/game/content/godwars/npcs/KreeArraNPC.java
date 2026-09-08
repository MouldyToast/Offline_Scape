package org.jesse.game.content.godwars.npcs;

import org.jesse.game.task.TickTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Action;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.PlayerCombat;

/**
 * @author Kris | 6/14/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KreeArraNPC extends NPCPlugin {
    @Override
    public void handle() {
        bind("Attack", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                if (npc.isAttackable(player)) {
                    if (npc instanceof KreeArra) {
                        final KreeArra kree = (KreeArra) npc;
                        kree.clickDelay = System.currentTimeMillis() + 4800;
                    }
                    PlayerCombat.attackEntity(player, npc, null);
                    final Action action = player.getActionManager().getAction();
                    if (action instanceof PlayerCombat && npc instanceof KreeArra) {
                        WorldTasksManager.schedule(new TickTask() {
                            @Override
                            public void run() {
                                if (npc.isDead() || npc.isFinished() || player.getActionManager().getAction() != action || ++ticks >= 100) {
                                    stop();
                                    return;
                                }
                                ((KreeArra) npc).clickDelay = System.currentTimeMillis() + 4800;
                            }
                        }, 0, 0);
                    }
                }
            }
            @Override
            public void click(Player player, NPC npc, final NPCOption option) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {NpcId.KREEARRA, NpcId.KREEARRA_12443};
    }
}
