package com.zenyte.game.world.entity.player;

import com.near_reality.game.world.entity.player.action.combat.AmmunitionDefinition;
import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ids.ItemId;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.action.combat.AmmunitionDefinitions;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.action.combat.RangedCombat;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import mgi.types.config.items.ItemDefinitions;
import net.runelite.api.ItemID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A utility class for manipulating & setting player bonuses.
 *
 * @author Mack
 */
public class Bonuses {
    private final Player player;
    private final Map<Integer, Integer> bonuses;

    /**
     * Creates a new {@link Bonuses} instance for the player.
     *
     * @param player
     */
    public Bonuses(final Player player) {
        this.player = player;
        bonuses = new HashMap<>();
    }

    public void setBonus(final int index, final int bonus) {
        bonuses.put(index, bonus);
    }

    /**
     * Updates the bonus information.
     */
    public void update() {
        if (!bonuses.isEmpty()) {
            bonuses.clear();
        }
        for (int equipmentIndex = 0; equipmentIndex < Equipment.SIZE; equipmentIndex++) {
            final Item item = player.getEquipment().getItem(equipmentIndex);
            if (Objects.nonNull(item) && Objects.nonNull(item.getDefinitions())) {
                final ItemDefinitions defs = item.getDefinitions();
                final int[] data = defs.getBonuses();
                if (data == null) continue;
                for (final Bonus bonus : Bonus.VALUES) {
                    if (bonus.getBonusIndex() >= data.length) {
                        continue;
                    }
                    final int existing_value = bonuses.getOrDefault(bonus.getBonusIndex(), 0);
                    int b = data[bonus.getBonusIndex()];
                    if (b == 0) continue;
                    final int id = item.getId();

                    if (equipmentIndex == 2 && bonus == Bonus.PRAYER &&
                            (id == ItemID.AMULET_OF_THE_DAMNED_FULL
                                    || id == ItemId.AMULET_OF_THE_DAMNED)) {
                        if (CombatUtilities.hasFullBarrowsSet(player, "Verac's")) {
                            b += 4;
                        }
                    }


                    if (equipmentIndex == 3 && bonus.equals(Bonus.RANGE_STRENGTH)) {
                        final int weaponId = player.getEquipment()
                                .getId(EquipmentSlot.WEAPON.getSlot());
                        if (weaponId == ItemId.TOXIC_BLOWPIPE) {
                            final Item blowpipe = player.getEquipment()
                                    .getItem(EquipmentSlot.WEAPON.getSlot());
                            if (blowpipe != null) {
                                final int dartTypeId = blowpipe.getNumericAttribute("blowpipeDartType")
                                        .intValue();
                                final ItemDefinitions dartDef = ItemDefinitions.get(dartTypeId);
                                if (dartTypeId > 0 && dartDef != null && dartDef.getBonuses() != null) {
                                    b += dartDef.getBonuses()[Bonus.RANGE_STRENGTH.getBonusIndex()];
                                }
                            }
                        }
                    }

                    if (equipmentIndex == 13 && bonus.equals(Bonus.RANGE_STRENGTH)) {
                        final int weaponId = player.getEquipment()
                                .getId(EquipmentSlot.WEAPON.getSlot());
                        final int ammunitionId = player.getEquipment()
                                .getId(EquipmentSlot.AMMUNITION);

                        AmmunitionDefinitions.AmmoApplicability applicability = AmmunitionDefinitions.getAmmoApplicability(weaponId, ammunitionId);

                        if (applicability != AmmunitionDefinitions.AmmoApplicability.INCLUDED) {
                            continue;
                        }
                    }
                    bonuses.put(bonus.getBonusIndex(), existing_value + b);
                }
            }
        }

        final boolean stats = player.getInterfaceHandler().isVisible(84);
        final boolean bank = player.getInterfaceHandler().isVisible(GameInterface.BANK.getId());
        if (stats || bank) {
            final int interfaceId = stats ? 84 : GameInterface.BANK.getId();
            for (final Bonus bonus : Bonus.VALUES) {
                final int child = stats ? bonus.getChildId() : bonus.getBankChildId();
                if (bonus.equals(Bonus.UNDEAD)) {
                    final int necklace = player.getEquipment().getId(EquipmentSlot.AMULET);
                    final int percentage = necklace == ItemId.SALVE_AMULET || necklace == ItemId.SALVE_AMULETI ? 15 :
                            necklace == ItemId.SALVE_AMULET_E || necklace == ItemId.SALVE_AMULETEI ? 20 : 0;
                    final String suffix = necklace == ItemId.SALVE_AMULET || necklace == ItemId.SALVE_AMULET_E ? " " +
                            "(melee)" : necklace == ItemId.SALVE_AMULETI || necklace == ItemId.SALVE_AMULETEI ? " (all" +
                            " styles)" : "";
                    player.getPacketDispatcher()
                            .sendComponentText(interfaceId, child, bonus.getBonusName() + ": "
                                    + percentage + "%" + suffix);
                    continue;
                } else if (bonus.equals(Bonus.SLAYER)) {
                    final Item helmet = player.getHelmet();
                    String percentage = "0";
                    String suffix = "";
                    if (helmet != null) {
                        final String name = helmet.getName().toLowerCase();
                        if (name.contains("black mask") || name.contains("slayer helm")) {
                            final boolean allStyles = name.contains("(i)");
                            percentage = "15";
                            suffix = allStyles ? " (all styles)" : " (melee)";
                        }
                    }
                    player.getPacketDispatcher()
                            .sendComponentText(interfaceId, child, bonus.getBonusName() + ": "
                                    + percentage + "%" + suffix);
                    continue;
                }
                final int value = bonuses.getOrDefault(bonus.getBonusIndex(), 0);
                String prefix = "";
                String suffix = "";
                if (value >= 0) {
                    prefix = "+";
                } else {
                    prefix = "";
                }
                if (bonus.equals(Bonus.MAGIC_DAMAGE)) {
                    suffix = "%";
                }
                player.getPacketDispatcher()
                        .sendComponentText(interfaceId, child, bonus.getBonusName() + ": " + prefix + value + suffix);
            }

            player.getPacketDispatcher().sendComponentText(interfaceId, stats ? 43 : 117, "");
        }
    }


    public enum Bonus {
        ATT_STAB(0, 24, "Stab", 98),
        ATT_SLASH(1, 25, "Slash", 99),
        ATT_CRUSH(2, 26, "Crush", 100),
        ATT_MAGIC(3, 27, "Magic", 101),
        ATT_RANGED(4, 28, "Range", 102),
        DEF_STAB(5, 30, "Stab", 104),
        DEF_SLASH(6, 31, "Slash", 105),
        DEF_CRUSH(7, 32, "Crush", 106),
        DEF_MAGIC(8, 33, "Magic", 107),
        DEF_RANGE(9, 34, "Range", 108),
        STRENGTH(10, 36, "Melee strength", 110),
        RANGE_STRENGTH(11, 37, "Ranged strength", 111),
        MAGIC_DAMAGE(12, 38, "Magic damage", 112),
        PRAYER(13, 39, "Prayer", 113),
        UNDEAD(14, 41, "Undead", 115),
        SLAYER(15, 42, "Slayer", 116);

        private final int bonusIndex;
        private final int childId;

        public int getBankChildId() {
            return bankChildId;
        }

        private final int bankChildId;
        private final String bonusName;
        public static final Bonus[] VALUES = values();

        Bonus(final int bonusIndex, final int childId, final String bonusName, int bankChildId) {
            this.bankChildId = bankChildId;
            this.bonusIndex = bonusIndex;
            this.childId = childId;
            this.bonusName = bonusName;
        }

        public int getBonusIndex() {
            return bonusIndex;
        }

        public int getChildId() {
            return childId;
        }

        public String getBonusName() {
            return bonusName;
        }
    }

    /**
     * Gets the bonus value for the specified index.
     *
     * @param bonusIndex
     * @return
     */
    public int getBonus(final int bonusIndex) {
        return bonuses.getOrDefault(bonusIndex, 0);
    }

    public int getBonus(final Bonus bonus) {
        return getBonus(bonus.bonusIndex);
    }

    public int getDefenceBonus(final AttackType type) {
        final Bonuses.Bonus bonus = type.isMagic() ? Bonus.DEF_MAGIC : type.isRanged() ? Bonus.DEF_RANGE : type == AttackType.CRUSH ? Bonus.DEF_CRUSH : type == AttackType.SLASH ? Bonus.DEF_SLASH : Bonus.DEF_STAB;
        return getBonus(bonus.getBonusIndex());
    }
}
