package com.zenyte.game.content.boons;

import com.zenyte.game.content.boons.impl.DukeDanger;
import com.zenyte.game.content.boons.impl.SoulStealer;
import com.zenyte.game.content.boons.impl.UnknownBoon;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.HashMap;
import java.util.Map;

public enum BoonWrapper {
//    UnholyIntervention    // REMOVED 3/20/2025
//    SliceNDice            // REMOVED 3/20/2025
//    IceForTheEyeless      // REMOVED 3/20/2025
//    CryptKeeper           // REMOVED 3/20/2025
//    IWantItAll            // REMOVED 3/20/2025
//    SousChef              // REMOVED 3/20/2025
//    Woodsman              // REMOVED 3/20/2025
//    Pyromaniac            // REMOVED 3/20/2025
//    Botanist              // REMOVED 3/20/2025
//    TrackStar             // REMOVED 3/20/2025
//    SuperiorSorcery       // REMOVED 3/20/2025
//    BurnBabyBurn          // REMOVED 3/20/2025
//    ArcaneKnowledge       // REMOVED 3/20/2025
//    Alchoholic            // REMOVED 3/20/2025
//    SustainedAggression   // REMOVED 3/20/2025
//    HoleyMoley            // REMOVED 3/20/2025
//    TheRedeemer           // REMOVED 3/20/2025
//    AshesToAshes          // MERGED INTO BOUNTIFUL SACRIFICE 3/20/2025
//    DoubleChins           // REMOVED 3/20/2025
//    EndlessQuiver         // REMOVED 3/20/2025
//    ContractKiller        // REMOVED 3/20/2025
//    SlayersFavor          // REMOVED 3/20/2025
//    TheLegendaryFisherman // REMOVED 3/20/2025
//    EyeDontSeeYou         // REMOVED 3/20/2025
//    JabbasRightHand       // REMOVED 3/20/2025

    BarbarianFisher(com.zenyte.game.content.boons.impl.BarbarianFisher.class, 1),
    HardWorkPaysOff(com.zenyte.game.content.boons.impl.HardWorkPaysOff.class, 2),
    BoneCruncher(com.zenyte.game.content.boons.impl.BoneCruncher.class,3),
    BlessedFromAbove(com.zenyte.game.content.boons.impl.BlessedFromAbove.class,4),
    NoOnesHome(com.zenyte.game.content.boons.impl.NoOnesHome.class, 5),
    DrawPartner(com.zenyte.game.content.boons.impl.DrawPartner.class, 6),
    DharoksBlessing(com.zenyte.game.content.boons.impl.DharoksBlessing.class, 7),
    MasterOfTheCraft(com.zenyte.game.content.boons.impl.MasterOfTheCraft.class, 8),
    RevItUp(com.zenyte.game.content.boons.impl.RevItUp.class, 9),
    FirstImpressions(com.zenyte.game.content.boons.impl.FirstImpressions.class, 10),
    ClueCollector(com.zenyte.game.content.boons.impl.ClueCollector.class, 11),
    NoPetDebt(com.zenyte.game.content.boons.impl.NoPetDebt.class, 12),
    CrystalCatalyst(com.zenyte.game.content.boons.impl.CrystalCatalyst.class, 13),
    ImRubberYoureGlue(com.zenyte.game.content.boons.impl.ImRubberYoureGlue.class, 14),
    LunarEnthusiast(com.zenyte.game.content.boons.impl.LunarEnthusiast.class, 15),
    HammerDown(com.zenyte.game.content.boons.impl.HammerDown.class, 16),
    SlayersSpite(com.zenyte.game.content.boons.impl.SlayersSpite.class, 17),
    DagaWHO(com.zenyte.game.content.boons.impl.DagaWHO.class, 18),
    RunForrestRun(com.zenyte.game.content.boons.impl.RunForrestRun.class, 19),
    LessIsMore(com.zenyte.game.content.boons.impl.LessIsMore.class, 20),
    HoarderMentality(com.zenyte.game.content.boons.impl.HoarderMentality.class, 21),
    CorporealScrutiny(com.zenyte.game.content.boons.impl.CorporealScrutiny.class, 22),
    RelentlessPrecision(com.zenyte.game.content.boons.impl.RelentlessPrecision.class, 23),
    FourSure(com.zenyte.game.content.boons.impl.FourSure.class, 24),
    DivineHealing(com.zenyte.game.content.boons.impl.DivineHealing.class, 25),
    DoubleTap(com.zenyte.game.content.boons.impl.DoubleTap.class, 26),
    Locksmith(com.zenyte.game.content.boons.impl.Locksmith.class, 27),
    LethalAttunement(com.zenyte.game.content.boons.impl.LethalAttunement.class, 28),
    CrushingBlow(com.zenyte.game.content.boons.impl.CrushingBlow.class, 29),
    Mixologist(com.zenyte.game.content.boons.impl.Mixologist.class, 30),
    NoShardRequired(com.zenyte.game.content.boons.impl.NoShardRequired.class, 31),
    SwissArmyMan(com.zenyte.game.content.boons.impl.SwissArmyMan.class, 32),
    SleightOfHand(com.zenyte.game.content.boons.impl.SleightOfHand.class, 33),
    SpecialBreed(com.zenyte.game.content.boons.impl.SpecialBreed.class, 34),
    AnimalTamer(com.zenyte.game.content.boons.impl.AnimalTamer.class, 35),
    IVoted(com.zenyte.game.content.boons.impl.IVoted.class, 36),
    FamiliarsFortune(com.zenyte.game.content.boons.impl.FamiliarsFortune.class, 37),
    ThePointyEnd(com.zenyte.game.content.boons.impl.ThePointyEnd.class, 38),
    HashSlingingSlasher(com.zenyte.game.content.boons.impl.HashSlingingSlasher.class, 39),
    HolierThanThou(com.zenyte.game.content.boons.impl.HolierThanThou.class, 40),
    BrawnOfJustice(com.zenyte.game.content.boons.impl.BrawnOfJustice.class, 41),
    VigourOfInquisition(com.zenyte.game.content.boons.impl.VigourOfInquisition.class, 42),
    InfallibleShackles(com.zenyte.game.content.boons.impl.InfallibleShackles.class, 43),
    IgnoranceIsBliss(com.zenyte.game.content.boons.impl.IgnoranceIsBliss.class, 44),
    FarmersFortune(com.zenyte.game.content.boons.impl.FarmersFortune.class, 45),
    MinionsMight(com.zenyte.game.content.boons.impl.MinionsMight.class, 46),
    SoulStealer(com.zenyte.game.content.boons.impl.SoulStealer.class, 47),
    SlayersSovereignty(com.zenyte.game.content.boons.impl.SlayersSovereignty.class, 48),
    HolyInterventionI(com.zenyte.game.content.boons.impl.HolyInterventionI.class, 49),
    HolyInterventionII(com.zenyte.game.content.boons.impl.HolyInterventionII.class, 50),
    HolyInterventionIII(com.zenyte.game.content.boons.impl.HolyInterventionIII.class, 51),
    NoShardRequiredII(com.zenyte.game.content.boons.impl.NoShardRequiredII.class, 52),
    NoShardRequiredIII(com.zenyte.game.content.boons.impl.NoShardRequiredIII.class, 53),
    CantBeAxed(com.zenyte.game.content.boons.impl.CantBeAxed.class, 54),
    DukeDanger(DukeDanger.class, 55),
    UnholyIntervention(com.zenyte.game.content.boons.impl.UnholyIntervention.class, 56),
    NoMoleHoles(com.zenyte.game.content.boons.impl.NoMoleHoles.class, 57),
    DivinedDefense(com.zenyte.game.content.boons.impl.DivinedDefense.class, 58),
    Unknown(UnknownBoon.class, -1),
    ;

    private final Class<? extends Boon> perk;
    private final int id;
    private static final BoonWrapper[] VALUES = values();
    public static final Map<Class<? extends Boon>, BoonWrapper> PERKS_BY_CLASS = new HashMap<>();
    private static final Map<String, BoonWrapper> PERKS_BY_NAME = new HashMap<>();
    public static final Int2ObjectOpenHashMap<BoonWrapper> PERKS_BY_ID = new Int2ObjectOpenHashMap<>();

    static {
        for (final BoonWrapper value : VALUES) {
            PERKS_BY_CLASS.put(value.getPerk(), value);
            PERKS_BY_NAME.put(value.name(), value);
            PERKS_BY_NAME.put("TwistedTradeOff", RelentlessPrecision);
            PERKS_BY_NAME.put("VitursOffering", FourSure);
            PERKS_BY_NAME.put("TumekensTribute", DivineHealing);
            PERKS_BY_ID.put(value.getId(), value);
        }
    }

    public static BoonWrapper get(final Class<? extends Boon> perk) {
        if(!PERKS_BY_CLASS.containsKey(perk))
            throw new RuntimeException("MISSING BOON WRAPPER: " + perk.getSimpleName());
        return PERKS_BY_CLASS.get(perk);
    }

    public static BoonWrapper getByString(final String perk) {
        return PERKS_BY_NAME.get(perk);
    }

    public static BoonWrapper get(final int id) {
        return PERKS_BY_ID.get(id);
    }

    BoonWrapper(Class<? extends Boon> perk, int id) {
        this.perk = perk;
        this.id = id;
    }

    public Class<? extends Boon> getPerk() {
        return perk;
    }

    public int getId() {
        return id;
    }
}
