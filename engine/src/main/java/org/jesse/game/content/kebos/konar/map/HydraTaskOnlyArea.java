package org.jesse.game.content.kebos.konar.map;

import org.jesse.game.content.kebos.konar.npc.Hydra;
import org.jesse.game.content.slayer.Assignment;
import org.jesse.game.content.slayer.RegularTask;
import org.jesse.game.content.slayer.SlayerMaster;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.PlayerCombat;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.area.plugins.EntityAttackPlugin;

/**
 * @author Tommeh | 24/10/2019 | 20:06
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class HydraTaskOnlyArea extends KaruulmSlayerDungeon implements EntityAttackPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{1307, 10257}, {1311, 10257}, {1311, 10259}, {1315, 10259}, {1315, 10265}, {1323, 10265}, {1323, 10261}, {1327, 10261}, {1327, 10259}, {1331, 10259}, {1331, 10265}, {1333, 10265}, {1333, 10271}, {1327, 10271}, {1327, 10273}, {1323, 10273}, {1323, 10269}, {1317, 10269}, {1317, 10275}, {1313, 10275}, {1313, 10277}, {1309, 10277}, {1309, 10271}, {1301, 10271}, {1301, 10267}, {1303, 10267}, {1303, 10259}, {1307, 10259}})};
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof Hydra) {
            final Assignment assignment = player.getSlayer().getAssignment();
            if (assignment == null || assignment.getTask() != RegularTask.HYDRAS) {
                player.getDialogueManager().start(new Dialogue(player, SlayerMaster.KONAR_QUO_MATEN.getNpcId()) {
                    @Override
                    public void buildDialogue() {
                        npc("You can only kill Hydras in this area if you're assigned to kill them.");
                    }
                });
                return false;
            }
        }
        return true;
    }

    @Override
    public String name() {
        return "Karuulm Slayer Dungeon (Hydra Task-only)";
    }
}
