package org.jesse.game.world.entity.npc.combat.impl.poison_waste;

import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;

/**
 * @author Zei | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Z
 * @social Github: <a href="https://github.com/Zeighe">Zeighe</a>
 * @since 1/28/2025
 */
public class WarpedTortoise extends NPC implements Spawnable, CombatScript {
    public WarpedTortoise(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius, false);
    }

    @Override
    public boolean isMultiArea() {
        return true;
    }

    //@Override
    //public void handleIngoingHit(final Hit hit) {
    //    if (hit.getSource() instanceof Player player) {
    //        boolean hasCrystalChime = player.getInventory().containsItem(CRYSTAL_CHIME);
    //        if (!hasCrystalChime) {
    //            hit.setDamage(0);
    //            player.sendMessage("You need a crystal chime to harm the warped tortoise.");
    //        }
    //    }
    //    super.handleIngoingHit(hit);
    //}

    @Override
    public int attack(final Entity target) {
        animate();
        executeMeleeHit(target, CombatUtilities.getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MAGIC, MELEE, target));
        return combatDefinitions.getAttackSpeed();
    }

    @Override
    public boolean validate(int id, String name) {
        return id == NpcId.WARPED_TORTOISE;
    }
}
