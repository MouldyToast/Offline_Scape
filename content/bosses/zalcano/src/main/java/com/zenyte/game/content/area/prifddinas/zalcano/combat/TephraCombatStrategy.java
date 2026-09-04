package com.zenyte.game.content.area.prifddinas.zalcano.combat;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.action.combat.RangedCombat;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;

public class TephraCombatStrategy extends RangedCombat {

    public TephraCombatStrategy(Entity target) {
        super(target);
    }

    @Override
    protected int getWeaponSpeed() {
        return 3;
    }

    @Override
    public final Hit getHit(final Player player, final Entity target, final double accuracyModifier, final double passiveModifier, double activeModifier, final boolean ignorePrayers) {
        final int maxHit = getMaxHit(player, passiveModifier, 1, ignorePrayers);
        final int damage = getRandomHit(player, target, maxHit, accuracyModifier);
        final Hit hit = new Hit(player, damage, HitType.ARMOUR);
        if (damage == maxHit) {
            hit.setMax(true);
        }
        return hit;
    }

    @Override
    protected void grantExperience(int skill, double amount) {
        /* empty */
    }

    @Override
    public int getMaxHit(Player player, double specialModifier, double activeModifier, boolean ignorePrayers) {
        int finalDamage = 0;

        int smithingBoost = player.getSkills().getLevel(SkillConstants.SMITHING) / 10;
        int runecraftingBoost = player.getSkills().getLevel(SkillConstants.RUNECRAFTING) / 10;
        boolean onBlueSymbol = false; // Testing

        if (player.getTemporaryAttributes().containsKey("BLUE_SYMBOL")) {
            onBlueSymbol = (boolean)player.getTemporaryAttributes().get("BLUE_SYMBOL");

            player.setGraphics(new Graphics(1726));
            player.sendFilteredMessage("You feel the symbol beneath your feet fill you with power.");
        }

        finalDamage += (smithingBoost + runecraftingBoost);
        if (finalDamage <= 10) {
            finalDamage = 10;
        }

        if (onBlueSymbol) {
            finalDamage += finalDamage * 50 / 100;
        }

        if (player.hasPrivilege(PlayerPrivilege.DEVELOPER)) {
            finalDamage *= 4;
        }

        return finalDamage;
    }

    @Override
    public boolean applyAmmo() {
        if (player.getEquipment().getId(EquipmentSlot.WEAPON) != ItemId.IMBUED_TEPHRA) {
            player.sendMessage("You can only attack Zalcano with Imbued tephra.");
            return false;
        }

        return super.applyAmmo();
    }

}
