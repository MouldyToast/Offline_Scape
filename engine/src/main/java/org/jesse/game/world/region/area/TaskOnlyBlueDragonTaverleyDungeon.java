package org.jesse.game.world.region.area;

import org.jesse.game.content.slayer.Assignment;
import org.jesse.game.content.slayer.RegularTask;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.PlayerCombat;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.area.plugins.EntityAttackPlugin;

/**
 * @author Kris | 10/07/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TaskOnlyBlueDragonTaverleyDungeon extends TaverleyDungeon implements EntityAttackPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{2882, 9852}, {2882, 9796}, {2980, 9796}, {2980, 9852}}, 1)};
    }

    @Override
    public String name() {
        return "Taverley Underground: Blue Dragons(Task-Only)";
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof NPC && ((NPC) entity).getName(player).equalsIgnoreCase("blue dragon")) {
            final Assignment assignment = player.getSlayer().getAssignment();
            if (assignment == null || assignment.getTask() != RegularTask.BLUE_DRAGONS) {
                player.getDialogueManager().start(new Dialogue(player, NpcId.EVE) {
                    @Override
                    public void buildDialogue() {
                        npc("Hey, go and train somewhere else! I'm not having people mess with my dragons unless a " +
                                "Slayer Master's told them to do it.");
                    }
                });
                return false;
            }
        }
        return true;
    }
}
