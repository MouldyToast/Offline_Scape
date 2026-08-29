package com.zenyte.game.content.wildernessVault.reward;

import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.enums.RareDrop;
import com.zenyte.game.util.Utils;
import com.zenyte.game.util.math.Probability;
import com.zenyte.game.util.math.ProbabilityCollection;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.concurrent.ThreadLocalRandom;

public enum VaultRewardItem implements Probability {

    // Pet & Artifact/Rares
    ANGELIC_ARTEFACT(126, 32182, 1,1),
    BLOOD_TENTACLE(600, CustomItemId.BLOOD_TENTACLES, 1,1),
    DEGRADED_ESSENCE(1, CustomItemId.DEGRADED_ESSENCE, 10,35),
    BOOK_OF_THE_DEAD(100, ItemId.BOOK_OF_THE_DEAD, 1,1),
    ARMADYL_GODSWORD(300, ItemId.ARMADYL_GODSWORD, 1,1),
    LIME_WHIP(750, CustomItemId.LIME_WHIP, 1,1),
    BLOOD_MONEY(1, ItemId.BLOOD_MONEY, 25, 100),

    // Combat Misc
    ELDER_CHAOS_HOOD(30, ItemId.ELDER_CHAOS_HOOD, 1,1),
    ELDER_CHAOS_TOP(30, ItemId.ELDER_CHAOS_TOP, 1,1),
    ELDER_CHAOS_ROBE(30, ItemId.ELDER_CHAOS_ROBE, 1,1),
    DARK_BOW(30, ItemId.DARK_BOW, 1,1),
    OCCULT_NECKLACE(30, ItemId.OCCULT_NECKLACE, 1,1),
    STAFF_OF_THE_DEAD(50, ItemId.STAFF_OF_THE_DEAD, 1,1),

    // Barrows
    AHRIMS_ROBESKIRT(5, ItemId.AHRIMS_ROBESKIRT, 1,1),
    AHRIMS_ROBETOP(5, ItemId.AHRIMS_ROBETOP, 1,1),
    KARILS_LEATHERTOP(5, ItemId.KARILS_LEATHERTOP, 1,1),
    KARILS_LEATHERSKIRT(5, ItemId.KARILS_LEATHERSKIRT, 1,1),
    GRANITE_MAUL(5, ItemId.GRANITE_MAUL + 1, 1,2),
    INFINITY_BOOTS(5, ItemId.INFINITY_BOOTS, 1, 1),
    MAGES_BOOK(5, ItemId.MAGES_BOOK, 1, 1),
    MASTER_WAND(5, ItemId.MASTER_WAND, 1, 1),

    // Alchables
    RUNE_PLATESKIRT(5, ItemId.RUNE_PLATESKIRT + 1, 1,4),
    RUNE_PLATELEGS(5, ItemId.RUNE_PLATELEGS + 1, 1,4),
    RUNE_PLATEBODY(5, ItemId.RUNE_PLATEBODY + 1, 1,4),
    RUNE_FULL_HELM(5, ItemId.RUNE_FULL_HELM + 1, 1,4),
    DRAGON_DAGGER(5, ItemId.DRAGON_DAGGER + 1, 1,4),
    DRAGON_LONGSWORD(5, ItemId.DRAGON_LONGSWORD + 1, 1,3),
    DRAGON_PLATELEGS(5, ItemId.DRAGON_PLATELEGS + 1, 1,3),
    DRAGON_MED_HELM(5, ItemId.DRAGON_MED_HELM + 1, 1,3),
    DRAGON_DAGGER_2(5, ItemId.DRAGON_DAGGER + 1, 1,3),

    // Supplies
    DEATH_RUNE(5, ItemId.DEATH_RUNE, 400,800),
    BLOOD_RUNE(5, ItemId.BLOOD_RUNE, 400,800),
    COAL(5, ItemId.COAL +1 , 400,800),
    RUNITE_ORE(5, ItemId.RUNITE_ORE + 1, 60,90),
    WINE_OF_ZAMORAK(5, ItemId.WINE_OF_ZAMORAK + 1, 60,100),
    GRIMY_CADANTINE(5, ItemId.GRIMY_CADANTINE + 1, 50,100),
    GRIMY_TOADFLAX(5, ItemId.GRIMY_TOADFLAX + 1, 40,80),
    GRIMY_RANARR_WEED(5, ItemId.GRIMY_RANARR_WEED + 1, 40,80),
    GRIMY_TORSTOL(5, ItemId.GRIMY_TORSTOL + 1, 30,60),
    SUPER_RESTORE4(5, ItemId.SUPER_RESTORE4 + 1, 10,20),
    SARADOMIN_BREW4(5, ItemId.SARADOMIN_BREW4 + 1, 20,30),
    DRAGON_DART_TIP(5, ItemId.DRAGON_DART_TIP, 50,150),
    COOKED_KARAMBWAN(5, ItemId.COOKED_KARAMBWAN + 1, 50,150),
    ANGLERFISH(5, ItemId.ANGLERFISH + 1, 25,75),
    DARK_CRAB(5, ItemId.DARK_CRAB + 1, 50,100),
    SANFEW_SERUM4(5, ItemId.SANFEW_SERUM4 + 1, 30,60),
    SUPER_COMBAT_POTION4(5, ItemId.SUPER_COMBAT_POTION4 + 1, 30,60),
    OPAL_DRAGON_BOLTS_E(5, ItemId.DARK_CRAB + 1, 50,100),
    ABYSSAL_WHIP(5, ItemId.ABYSSAL_WHIP, 1, 1),
    BANDOS_GODSWORD(5, ItemId.BANDOS_GODSWORD, 1, 1),
    SARADOMIN_GODSWORD(5, ItemId.SARADOMIN_GODSWORD, 1, 1),
    ZAMORAK_GODSWORD(5, ItemId.ZAMORAK_GODSWORD, 1, 1),
    AMULET_OF_TORTURE(5, ItemId.AMULET_OF_TORTURE, 1, 1),
    AMULET_OF_ANGUISH(5, ItemId.NECKLACE_OF_ANGUISH, 1, 1),
    RING_OF_SUFFERING(5, ItemId.RING_OF_SUFFERING, 1, 1),
    STAFF_OF_LIGHT(5, ItemId.STAFF_OF_LIGHT, 1, 1),
    TOXIC_STAFF(5, ItemId.TOXIC_STAFF_UNCHARGED, 1, 1)

    ;


    private final double probability;
    private final int id;
    private final int min;
    private final int max;
    private final double chance;

    VaultRewardItem(double roll, int id, int min, int max) {
        this.chance = 1D / roll;
        this.probability = roll;
        this.id = id;
        this.min = min;
        this.max = max;
    }

    public static final ObjectArrayList<VaultRewardItem> ALWAYS = new ObjectArrayList<>();

    public static final ProbabilityCollection<VaultRewardItem> REWARDS = new ProbabilityCollection<>(values());
    static {

        for (VaultRewardItem drop : values()) {
            if(drop.isRare())
                RareDrop.addDynamic(drop.id);

            if(drop.probability == 1)
                ALWAYS.add(drop);
        }

    }

    public static VaultRewardItem getAlways() {
        return Utils.random(ALWAYS);
    }


    public boolean roll() {
        return chance >= ThreadLocalRandom.current().nextDouble();
    }


    public double getRoll() {
        return probability;
    }

    public int getId() {
        return id;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public boolean isRare() {
        return probability >= 100;
    }

    @Override
    public double getProbability() {
        return chance;
    }
}
