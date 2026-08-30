package com.zenyte.game.content.boons;

import com.google.common.eventbus.Subscribe;
import com.near_reality.api.service.item.ItemConfigManager;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import static com.zenyte.game.item.ItemId.*;
import com.zenyte.plugins.events.ServerLaunchEvent;
import it.unimi.dsi.fastutil.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;

public class RemnantValueManager {

    public static ArrayList<RemnantValue> values = new ArrayList<>();

    static {
        values.add(gen(1500, ENHANCED_CRYSTAL_WEAPON_SEED));
                /* Bonds / Special Items */
        values.add(gen(1800, DONATOR_PIN_10));
        values.add(gen(4500, DONATOR_PIN_25));
        values.add(gen(9000, DONATOR_PIN_50));
        values.add(gen(18000, DONATOR_PIN_100));
        values.add(gen(1, REMNANT_POINT_VOUCHER_1));
        values.add(gen(5, REMNANT_POINT_VOUCHER_5));
        values.add(gen(10, REMNANT_POINT_VOUCHER_10));
        values.add(gen(20, REMNANT_WHEEL));
        values.add(gen(35, REMNANT_COG));
        values.add(gen(300, REMNANT_TOOLS));


        values.add(gen(50,
                TOME_OF_EXPERIENCE,
                TOME_OF_EXPERIENCE_30215,
                DRAGON_AXE,
                SMOKE_BATTLESTAFF,
                LAVA_BATTLESTAFF,
                LUMBERJACK_LEGS,
                LUMBERJACK_BOOTS,
                LUMBERJACK_HAT,
                LUMBERJACK_TOP,
                PYROMANCER_BOOTS,
                PYROMANCER_GARB,
                PYROMANCER_HOOD,
                PYROMANCER_ROBE,
                PROSPECTOR_BOOTS,
                PROSPECTOR_HELMET,
                PROSPECTOR_JACKET,
                PROSPECTOR_LEGS,
                FARMERS_BORO_TROUSERS,
                FARMERS_JACKET,
                FARMERS_BOOTS,
                FARMERS_STRAWHAT,
                ANGLER_BOOTS,
                ANGLER_TOP,
                ANGLER_HAT,
                ANGLER_WADERS
        ));

        values.add(gen(75,
                AHRIMS_HOOD,
                AHRIMS_ROBESKIRT,
                AHRIMS_ROBETOP,
                AHRIMS_STAFF,
                VERACS_BRASSARD,
                VERACS_FLAIL,
                VERACS_PLATESKIRT,
                VERACS_HELM,
                TORAGS_HAMMERS,
                TORAGS_HELM,
                TORAGS_PLATEBODY,
                TORAGS_PLATELEGS,
                KARILS_COIF,
                KARILS_CROSSBOW,
                KARILS_LEATHERSKIRT,
                KARILS_LEATHERTOP,
                GUTHANS_WARSPEAR,
                GUTHANS_PLATEBODY,
                GUTHANS_HELM,
                GUTHANS_CHAINSKIRT,
                DHAROKS_GREATAXE,
                DHAROKS_HELM,
                DHAROKS_PLATEBODY,
                DHAROKS_PLATELEGS
        ));

        values.add(gen(100,
                WARRIOR_RING,
                SEERS_RING,
                DAGONHAI_HAT,
                DAGONHAI_ROBE_BOTTOM,
                DAGONHAI_ROBE_TOP,
                AMULET_OF_ETERNAL_GLORY
        ));

        values.add(gen(150,
                BERSERKER_RING,
                ARCHERS_RING,
                ABYSSAL_WHIP,
                GODSWORD_BLADE,
                STEAM_BATTLESTAFF,
                SARADOMIN_SWORD,
                BOOK_OF_THE_DEAD,
                ODIUM_SHARD_1,
                ODIUM_SHARD_2,
                ODIUM_SHARD_3,
                MALEDICTION_SHARD_1,
                MALEDICTION_SHARD_2,
                MALEDICTION_SHARD_3
        ));
        values.add(gen(200,
                TRIDENT_OF_THE_SEAS,
                SARADOMINS_LIGHT,
                UNCHARGED_TRIDENT
        ));
        values.add(gen(300,
                BLESSED_SPIRIT_SHIELD,
                VENATOR_SHARD,
                KRAKEN_TENTACLE,
                ANGELIC_ARTIFACT
        ));
        values.add(gen(400,
                DRAGON_CROSSBOW,
                SMOULDERING_STONE,
                CRYSTAL_ARMOUR_SEED
        ));
        values.add(gen(500,
                ZAMORAK_HILT,
                SARADOMIN_HILT,
                BANDOS_HILT,
                ARMADYL_HILT,
                ZAMORAKIAN_SPEAR,
                ZAMORAKIAN_HASTA,
                OCCULT_NECKLACE,
                STAFF_OF_THE_DEAD,
                BANDOS_BOOTS,
                DRAGON_PICKAXE,
                HYDRA_TAIL,
                STAFF_OF_LIGHT,
                BOTTOMLESS_COMPOST_BUCKET,
                SARACHNIS_CUDGEL,
                DRAGONBONE_NECKLACE,
                ODIUM_WARD,
                MALEDICTION_WARD
        ));
        values.add(gen(600,
                LIGHT_BALLISTA
        ));
        values.add(gen(750,
                ZAMORAK_GODSWORD,
                SARADOMIN_GODSWORD,
                BANDOS_GODSWORD,
                ARMADYL_CROSSBOW,
                UNCUT_ZENYTE,
                WYVERN_VISAGE,
                ANCIENT_WYVERN_SHIELD,
                RANGER_BOOTS,
                BLOOD_SHARD,
                JAR_OF_VENOM,
                JAR_OF_DIRT,
                JAR_OF_SAND,
                JAR_OF_SWAMP,
                JAR_OF_SOULS,
                JAR_OF_MIASMA,
                JAR_OF_DARKNESS,
                JAR_OF_STONE,
                JAR_OF_DECAY,
                JAR_OF_CHEMICALS,
                JAR_OF_EYES,
                JAR_OF_DREAMS,
                JAR_OF_SPIRITS,
                JAR_OF_SMOKE
        ));
        values.add(gen(800,
                HEAVY_BALLISTA,
                BANDOS_TASSETS,
                BANDOS_CHESTPLATE,

                ABYSSAL_DAGGER,
                BASILISK_JAW,
                AMULET_OF_TORTURE,
                NECKLACE_OF_ANGUISH,
                RING_OF_SUFFERING,
                TORMENTED_BRACELET
        ));
        values.add(gen(900,
                TOXIC_BLOWPIPE,
                MAGIC_FANG,
                SERPENTINE_VISAGE,
                SERPENTINE_HELM_UNCHARGED,
                SERPENTINE_HELM
        ));
        values.add(gen(1000,
                VENGEFUL_KITESHIELD,
                MERCILESS_KITESHIELD,
                CHAOTIC_CROSSBOW,
                CHAOTIC_STAFF,
                SKULL_OF_VETION,
                FANGS_OF_VENENATIS,
                CLAWS_OF_CALLISTO,
                BRIMSTONE_RING,
                ETERNAL_GEM,
                _3RD_AGE_PLATESKIRT,
                _3RD_AGE_AMULET,
                _3RD_AGE_RANGE_COIF,
                _3RD_AGE_RANGE_TOP,
                _3RD_AGE_RANGE_LEGS,
                _3RD_AGE_VAMBRACES,
                _3RD_AGE_FULL_HELMET,
                _3RD_AGE_PLATEBODY,
                _3RD_AGE_PLATELEGS,
                _3RD_AGE_KITESHIELD,
                _3RD_AGE_MAGE_HAT,
                _3RD_AGE_ROBE_TOP,
                _3RD_AGE_ROBE,
                ARMADYL_HELMET,
                ARMADYL_CHESTPLATE,
                ARMADYL_CHAINSKIRT
        ));
        values.add(gen(1250,
                _3RD_AGE_WAND,
                _3RD_AGE_BOW,
                _3RD_AGE_LONGSWORD,
                _3RD_AGE_AXE,
                _3RD_AGE_PICKAXE,
                AVERNIC_DEFENDER_HILT,
                ANCIENT_HILT,
                ZARYTE_VAMBRACES,
                LIGHTBEARER,
                ABYSSAL_BLUDGEON,
                NIGHTMARE_STAFF,
                IMBUED_HEART,
                DRACONIC_VISAGE,
                DRAGONFIRE_SHIELD,
                DRAGONFIRE_SHIELD_11284
        ));
        values.add(gen(1500,
                ANCIENT_EYE,
                ARMADYL_GODSWORD,
                VESTAS_LONGSWORD,
                STATIUSS_WARHAMMER,
                VESTAS_SPEAR,
                ZURIELS_STAFF,
                _3RD_AGE_DRUIDIC_ROBE_TOP,
                _3RD_AGE_DRUIDIC_ROBE_BOTTOMS,
                _3RD_AGE_DRUIDIC_STAFF,
                _3RD_AGE_DRUIDIC_CLOAK,
                NIHIL_HORN,
                ZARYTE_CROSSBOW,
                BLADE_OF_SAELDOR,
                BOW_OF_FAERDHINEN_INACTIVE,
                ETERNAL_CRYSTAL,
                PEGASIAN_CRYSTAL,
                PRIMORDIAL_CRYSTAL,
                VENATOR_BOW,
                DINHS_BULWARK,
                TWISTED_BUCKLER,
                DEXTEROUS_PRAYER_SCROLL,
                ARCANE_PRAYER_SCROLL,
                KODAI_INSIGNIA,
                KODAI_WAND,
                DRAGON_HUNTER_CROSSBOW
        ));
        values.add(gen(1750,
                RING_OF_THE_GODS,
                TREASONOUS_RING,
                TYRANNICAL_RING,
                HYDRAS_CLAW,
                ANCIENT_GODSWORD
        ));
        values.add(gen(2000,
                SKELETAL_VISAGE,
                DRAGONFIRE_WARD,
                DRAGONFIRE_WARD_22003,
                INQUISITORS_MACE,
                PEGASIAN_BOOTS,
                ELDER_MAUL,
                SANGUINE_DUST,
                TANZANITE_MUTAGEN,
                MAGMA_MUTAGEN,
                PRIMORDIAL_BOOTS,
                ETERNAL_BOOTS
        ));
        values.add(gen(2250,
                GHRAZI_RAPIER,
                SANGUINESTI_STAFF
        ));
        values.add(gen(2500,
                OSMUMTENS_FANG,
                GREEN_HALLOWEEN_MASK,
                BLUE_HALLOWEEN_MASK,
                RED_HALLOWEEN_MASK,
                SANTA_HAT,
                KORASI,
                DRAGON_CLAWS,
                ANCESTRAL_HAT,
                ANCESTRAL_ROBE_TOP,
                ANCESTRAL_ROBE_BOTTOM,
                DRAGON_HUNTER_LANCE,
                RED_PARTYHAT,
                YELLOW_PARTYHAT,
                BLUE_PARTYHAT,
                GREEN_PARTYHAT,
                PURPLE_PARTYHAT,
                WHITE_PARTYHAT,
                JUSTICIAR_FACEGUARD,
                JUSTICIAR_CHESTGUARD,
                JUSTICIAR_LEGGUARDS,
                VOIDWAKER_BLADE,
                VOIDWAKER_GEM,
                VOIDWAKER_HILT
        ));
        values.add(gen(2750,
                CRAWS_BOW_U,
                VIGGORAS_CHAINMACE_U,
                THAMMARONS_SCEPTRE_U,
                INQUISITORS_GREAT_HELM,
                INQUISITORS_HAUBERK,
                INQUISITORS_PLATESKIRT
        ));
        values.add(gen(3000,
                HOLY_ORNAMENT_KIT,
                SANGUINE_ORNAMENT_KIT,
                BLACK_SANTA_HAT,
                INVERTED_SANTA_HAT,
                BLACK_HWEEN_MASK,
                HYDRA_LEATHER,
                LIME_WHIP
        ));
        values.add(gen(3250,
                PINK_HWEEN_MASK,
                ICE_HWEEN_MASK,
                DRAGON_WARHAMMER
        ));
        values.add(gen(3500,
                ICE_SANTA_HAT,
                PINK_SANTA_HAT,
                WISE_OLD_MANS_SANTA_HAT
        ));
        values.add(gen(4000,
                BLACK_PARTYHAT,
                RAINBOW_PARTYHAT,
                PARTYHAT__SPECS,
                VOLATILE_ORB,
                ELDRITCH_ORB,
                HARMONISED_ORB
        ));
        values.add(gen(4500,
                SILVER_PARTYHAT,
                PINK_PARTYHAT,
                ORANGE_PARTYHAT,
                ICE_PARTYHAT
        ));
        values.add(gen(5000,
                NEAR_REALITY_PARTY_HAT,
                GHOSTLY_PARTYHAT,
                POLYPORE_STAFF,
                ELIDINIS_WARD
        ));
        values.add(gen(5500,
                ZURIELS_HOOD,
                ZURIELS_ROBE_BOTTOM,
                ZURIELS_ROBE_TOP,
                STATIUSS_FULL_HELM,
                STATIUSS_PLATEBODY,
                STATIUSS_PLATELEGS,
                VESTAS_CHAINBODY,
                VESTAS_HELM,
                VESTAS_PLATESKIRT,
                MORRIGANS_COIF,
                MORRIGANS_LEATHER_BODY,
                MORRIGANS_LEATHER_CHAPS,
                SPECTRAL_SIGIL
        ));

        values.add(gen(6500,
                ARCANE_SIGIL,
                SPECTRAL_SPIRIT_SHIELD
        ));

        values.add(gen(7500,
                VOIDWAKER_27690,
                TORVA_FULLHELM,
                TORVA_PLATEBODY,
                TORVA_PLATELEGS,
                MASORI_MASK,
                MASORI_BODY,
                MASORI_CHAPS,
                VIRTUS_MASK,
                VIRTUS_ROBE_LEGS,
                VIRTUS_ROBE_TOP,
                ARCANE_SPIRIT_SHIELD
        ));

        values.add(gen(8500,
                MASORI_BODY_F,
                MASORI_CHAPS_F,
                MASORI_MASK_F
        ));

        values.add(gen(14000,
                ELIDINIS_WARD_F
        ));


        values.add(gen(22500,
                ELYSIAN_SIGIL,
                DIVINE_SIGIL
        ));

        values.add(gen(25000,
                ELYSIAN_SPIRIT_SHIELD,
                DIVINE_SPIRIT_SHIELD
        ));

        values.add(gen(50000, TWISTED_BOW));
        values.add(gen(65000, TUMEKENS_SHADOW, SCYTHE_OF_VITUR));
        values.add(gen(25, DRAGON_BOOTS, DRAGON_CHAINBODY_3140, MASTER_WAND, DARK_BOW, INFINITY_BOOTS, MAGES_BOOK, ELDER_CHAOS_HOOD, ELDER_CHAOS_TOP, ELDER_CHAOS_ROBE));

    }

    @Subscribe
    public static void onServerLaunchEvent(final ServerLaunchEvent event) {
        categorize();
    }

    private static final LinkedHashMap<Integer, Integer> VALUE_BY_ID = new LinkedHashMap<>();
    static void categorize() {
        for(RemnantValue value: values) {
            for(Item item: value.getItems()) {
                if(!VALUE_BY_ID.containsKey(item.getId())) {
                    VALUE_BY_ID.put(item.getId(), value.getValue());
                } else {
                    throw new RuntimeException("Duplicate exchange value defined for ID: " + item.getId());
                }
            }
        }
    }

    public static boolean isPresent(int itemId) {
        for(RemnantValue value: values) {
            for(Item item: value.getItems()) {
                if (item.getId() == itemId || item.toNote().getId() == itemId)
                    return true;
            }
        }
        return false;
    }

    public static Pair<Integer, Boolean> getValue(Item item) {
        int baseId = item.getDefinitions().getUnnotedOrDefault();
        if(ItemConfigManager.INSTANCE.hasPresentConfig(baseId)) {
            Optional<Integer> value = ItemConfigManager.INSTANCE.remnantValue(baseId);
            if(value.isPresent())
                return Pair.of(value.get(), true);
        }
        return Pair.of(VALUE_BY_ID.getOrDefault(baseId, 0), false);
    }

    public static RemnantValue gen(int value, int... ids) {
        return new RemnantValue(value, ids);
    }

    private static ArrayList<RemnantValue> values() {
        return values;
    }
}
