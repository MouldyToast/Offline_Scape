package org.jesse.game.world.region.area;

import org.jesse.game.content.slayer.Assignment;
import org.jesse.game.content.slayer.RegularTask;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.impl.slayer.wyverns.SkeletalWyvern;
import org.jesse.game.world.entity.npc.impl.slayer.wyverns.Wyvern;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.PlayerCombat;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.entity.player.teleportsystem.PortalTeleport;
import org.jesse.game.world.region.PolygonRegionArea;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.area.plugins.CannonRestrictionPlugin;
import org.jesse.game.world.region.area.plugins.EntityAttackPlugin;

/**
 * @author Kris | 21/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TaskOnlyWyvernCave extends PolygonRegionArea implements CannonRestrictionPlugin, EntityAttackPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{3584, 10304}, {3584, 10240}, {3648, 10240}, {3648, 10304}})};
    }

    @Override
    public void enter(Player player) {
        player.getTeleportManager().unlock(PortalTeleport.WYVERN_CAVE);
    }

    @Override
    public void leave(Player player, boolean logout) {
    }

    @Override
    public String name() {
        return "Wyvern Cave (task only)";
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof Wyvern && !(entity instanceof SkeletalWyvern)) {
            final Assignment assignment = player.getSlayer().getAssignment();
            if (assignment == null || assignment.getTask() != RegularTask.FOSSIL_ISLAND_WYVERN) {
                player.getDialogueManager().start(new Dialogue(player, NpcId.WEVE) {
                    @Override
                    public void buildDialogue() {
                        npc("You can only kill the wyverns in this area if you're assigned to kill them.");
                    }
                });
                return false;
            }
        }
        return true;
    }
}
