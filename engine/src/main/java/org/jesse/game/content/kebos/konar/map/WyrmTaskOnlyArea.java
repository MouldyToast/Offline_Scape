package org.jesse.game.content.kebos.konar.map;

import org.jesse.game.content.kebos.konar.npc.Wyrm;
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
public class WyrmTaskOnlyArea extends WyrmArea implements EntityAttackPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{1251, 10163}, {1251, 10157}, {1255, 10157}, {1255, 10147}, {1259, 10147}, {1259, 10151}, {1271, 10151}, {1271, 10157}, {1277, 10157}, {1277, 10163}, {1273, 10163}, {1273, 10171}, {1269, 10171}, {1269, 10163}}, 0)};
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof Wyrm) {
            final Assignment assignment = player.getSlayer().getAssignment();
            if (assignment == null || assignment.getTask() != RegularTask.WYRMS) {
                player.getDialogueManager().start(new Dialogue(player, SlayerMaster.KONAR_QUO_MATEN.getNpcId()) {
                    @Override
                    public void buildDialogue() {
                        npc("You can only kill Wyrms in this area if you're assigned to kill them.");
                    }
                });
                return false;
            }
        }
        return true;
    }

    @Override
    public String name() {
        return "Karuulm Slayer Dungeon (Wyrm Task-only)";
    }
}
