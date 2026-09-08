package org.jesse.game.world.entity.npc.impl.slayer;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.Toxins;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot;

public class FeverSpider extends NPC implements CombatScript, Spawnable {
    public FeverSpider(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int attack(final Entity target) {
        final FeverSpider npc = this;
        if (target instanceof Player) {
            final Player player = (Player) target;
            if (!player.getToxins().isDiseased() && player.getEquipment().getId(EquipmentSlot.HANDS) != ItemId.SLAYER_GLOVES_6720) {
                player.getToxins().applyToxin(Toxins.ToxinType.DISEASE, 10, false, this);
            }
        }
        attackSound();
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
        return npc.getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 626;
    }
}
