package org.jesse.game.world.entity.player.action.combat.magic;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.degradableitems.DegradableItem;
import org.jesse.game.model.item.degradableitems.DegradeType;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.action.combat.MagicCombat;

public class TumekensShadowCombat extends MagicCombat {

    public TumekensShadowCombat(final Entity target, final CombatSpell spell, final CastType type) {
        super(target, spell, type);
    }

    @Override
    protected void extra(Hit hit) {
        super.extra(hit);
        final Item weapon = player.getWeapon();
        if (weapon == null || weapon.getCharges() <= 0) {
            interrupt = true;
        }
    }

    @Override
    protected int attackSpeed() {
        return 5;
    }

    @Override
    protected boolean canAttack() {
        final Item weapon = player.getWeapon();
        final int charges = weapon.getCharges();
        if (weapon.getId() != ItemId.CORRUPTED_TUMEKENS_SHADOW && DegradableItem.getDefaultCharges(weapon.getId(), -1) != charges && charges <= 0) {
            player.sendMessage("Tukemen's shadow has no charges! You need to charge it with soul runes and chaos runes.");
            return false;
        }
        return super.canAttack();
    }

    @Override
    protected int getAttackDistance() {
        if (player.getCombatDefinitions().getStyle() == 3) {
            return 8;
        }
        return 6;
    }

    @Override
    protected int baseDamage() {
        return (int) (player.getSkills().getLevel(SkillConstants.MAGIC) / 3.0F) + 1;
    }

    @Override
    protected void degrade() {
        player.getChargesManager().removeCharges(DegradeType.TRIDENT);
        if (player.getWeapon().getId() == ItemId.CORRUPTED_TUMEKENS_SHADOW)
            player.getChargesManager().removeCharges(DegradeType.USE);
    }
}
