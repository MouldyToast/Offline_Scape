package com.zenyte.game.world.entity.npc.combat.impl.poison_waste;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AggressionType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;

import static com.zenyte.game.item.ItemId.EARMUFFS;

/**
 * @author Zei | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Z
 * @social Github: <a href="https://github.com/Zeighe">Zeighe</a>
 * @since 1/28/2025
 */
public class WarpedTerrorbird extends NPC implements Spawnable, CombatScript {

    public WarpedTerrorbird(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius, false);
    }

    @Override
    public NPC spawn() {
        super.spawn();
        this.combatDefinitions.setAggressionType(AggressionType.ALWAYS_AGGRESSIVE);
        return this;
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
    //            player.sendMessage("You need a crystal chime to harm the warped terrorbird.");
    //        }
    //    }
    //    super.handleIngoingHit(hit);
    //}

    @Override
    public void handleOutgoingHit(final Entity target, Hit hit) {
        if (hit.getSource() instanceof Player currentPlayer) {
            var item = currentPlayer.getEquipment().getItem(0);
            if (item != null) {
                int itemId = item.getId();
                var isHelm = item.getName().toLowerCase().contains("slayer helm");
                if (isHelm || itemId == EARMUFFS) {
                    var damage = hit.getDamage();
                    int halvedDamage = damage / 2;
                    hit.setDamage(halvedDamage);
                }
            }
        }
    }

    @Override
    public int attack(final Entity target) {
        animate();
        executeMeleeHit(target, CombatUtilities.getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MAGIC, MELEE, target));
        setAttackDistance(9);
        return combatDefinitions.getAttackSpeed();
    }

    @Override
    public boolean validate(int id, String name) {
        return id >= NpcId.WARPED_TERRORBIRD && id <= NpcId.WARPED_TERRORBIRD_12504;
    }
}
