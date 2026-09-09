package org.jesse.game.content.collectionlog

import org.jesse.game.item.ids.*
import org.jesse.game.item._Item



object CollectionLogRewards {

    /* Reward List */
    private const val regalMB = REGAL_MYSTERY_BOX
    private const val ultraMB = ULTIMATE_MYSTERY_BOX
    private const val superMB = SUPER_MYSTERY_BOX
    private const val standardMB = MYSTERY_BOX
    private const val skillingMB = SKILLING_MYSTERY_BOX
    private const val pvmMB = PVM_MYSTERY_BOX
    private const val petBooster = PET_BOOSTER
    private const val larransBooster = LARRANS_KEY_BOOSTER
    private const val slayerBooster = SLAYER_BOOSTER
    private const val bloodMoneyBooster = BLOOD_MONEY_BOOSTER
    private const val revenantBooster = REVENANT_BOOSTER
    private const val clueBooster = CLUE_SCROLL_BOOSTER
    private const val nexBooster = NEX_BOOSTER
    private const val slayerTaskPicker = SLAYER_TASK_PICKER_SCROLL
    private const val slayerTaskReset = SLAYER_TASK_RESET_SCROLL
    private const val barrowsTotem = BARROWS_TOTEM
    private const val malevolentEnergy = MALEVOLENT_ENERGY
    private const val ahrimKit = ECHO_AHRIMS_ORNAMENT_KIT
    private const val venatorKit = ECHO_VENATOR_BOW_ORNAMENT_KIT
    private const val herbBox = HERB_BOX
    private const val venatorShard = VENATOR_SHARD
    private const val asgarniaOrb = ASGARNIA_ECHO_ORB
    private const val tirannwnOrb = TIRANNWN_ECHO_ORB
    private const val morytaniaOrb = MORYTANIA_ECHO_ORB
    private const val kourendOrb = KOUREND_ECHO_ORB
    private const val desertOrb = DESERT_ECHO_ORB
    private const val wildyOrb = WILDERNESS_ECHO_ORB
    private const val dpin10 = DONATOR_PIN_10
    private const val dpin25 = DONATOR_PIN_25
    private const val dpin100 = DONATOR_PIN_100
    private const val imbueScroll = SCROLL_OF_IMBUING
    private const val echoVirtusKit = ECHO_VIRTUS_ORNAMENT_KIT
    private const val chromiumIngot = CHROMIUM_INGOT
    private const val fireCape = FIRE_CAPE
    private const val tokkul = TOKKUL
    private const val armourSeed = CRYSTAL_ARMOUR_SEED
    private const val toolSeed = CRYSTAL_TOOL_SEED
    private const val enhancedCKey = ENHANCED_CRYSTAL_KEY
    private const val bloodMoney = BLOOD_MONEY
    private const val doubleAmmoMold = DOUBLE_AMMO_MOULD
    private const val torstol = TORSTOL + 1
    private const val snapdragon = SNAPDRAGON + 1
    private const val queenSecateurs = QUEENS_SECATEURS
    private const val infernalCape = INFERNAL_CAPE
    private const val bandosComp = BANDOSIAN_COMPONENTS
    private const val armadylComp = ARMADYLEAN_PLATE
    private const val frozenCache = FROZEN_CACHE
    private const val ancientShardPack = ANCIENT_SHARD_PACK
    private const val darkTotem = DARK_TOTEM
    private const val bloodShard = BLOOD_SHARD
    private const val wintertodtCrate = SUPPLY_CRATE
    private const val blowpipeKit = TRAILBLAZER_RELOADED_BLOWPIPE_ORNAMENT_KIT
    private const val coxSoloOrb = ORB_OF_XERIC
    private const val tobSoloOrb = ORB_OF_BLOOD
    private const val toaSoloOrb = ORB_OF_AMASCUT
    private const val sherlockNote = SHERLOCKS_NOTES
    private const val omegaSpike = OMEGA_SPIKE
    private const val omegaHorn = OMEGA_HORN
    private const val omegaSymbol = OMEGA_SYMBOL
    private const val superiorBell = SUPERIOR_BELL
    private const val boostToken = WORLD_BOOST_TOKEN
    private const val crystalKey = CRYSTAL_KEY
    private const val nrPartyhat = NEAR_REALITY_PARTY_HAT
    private const val chaosRobesOrnKit = ELDER_CHAOS_ROBES_ORNAMENT_KIT
    private const val dragonDefOrnKit = DRAGON_DEFENDER_ORNAMENT_KIT
    private const val dragonPickOrnKit = DRAGON_PICKAXE_UPGRADE_KIT
    private const val ballistaOrnKit = HEAVY_BALLISTA_ORNAMENT_KIT
    private const val zenyte = ZENYTE
    private const val onyx = ONYX
    private const val gracefulDye = GRACEFUL_DYE
    private const val stardust = STARDUST

    /* Struct List */
    private const val abyssalSire = 476
    private const val alchemicalHydra = 539
    private const val araxxor = 10503
    private const val barrows = 477
    private const val bryophyta = 478
    private const val callistoAndArtio = 479
    private const val cerberus = 480
    private const val chaosElemental = 480
    private const val chaosFanatic = 482
    private const val commanderZilyana = 483
    private const val corporealBeast = 484
    private const val crazyArcheologist = 485
    private const val dagannothKings = 486
    private const val dukeSucellus = 4652
    private const val fightCaves = 500
    private const val gauntlet = 605
    private const val generalGraardor = 487
    private const val giantMole = 488
    private const val grotesqueGuardians = 489
    private const val hespori = 541
    private const val inferno = 499
    private const val kalphiteQueen = 490
    private const val kingBlackDragon = 491
    private const val kraken = 492
    private const val kreearra = 493
    private const val krilTsutsaroth = 494
    private const val leviathan = 4654
    private const val nex = 3769
    private const val nightmare = 1263
    private const val obor = 495
    private const val phantomMuspah = 4455
    private const val primal = 10501
    private const val riseOfTheSix = 10321
    private const val sarachnis = 601
    private const val scorpia = 496
    private const val skotizo = 497
    private const val thermonuclearSmokeDevil = 498
    private const val tormentedDemons = 4654
    private const val vanstromKlause = 10322
    private const val vardorvis = 4652
    private const val venenatisAndSpindel = 501
    private const val vetionAndCalvarion = 502
    private const val vorkath = 503
    private const val whisperer = 4654
    private const val wintertodt = 504
    private const val zalcano = 604
    private const val zulrah = 505

    private const val chambersOfXeric = 507
    private const val theatreOfBlood = 506
    private const val tombsOfAmascut = 4378

    private const val beginnerClues = 593
    private const val easyClues = 508
    private const val mediumClues = 509
    private const val hardClues = 510
    private const val eliteClues = 511
    private const val masterClues = 512
    private const val hardCluesRares = 2869
    private const val eliteCluesRares = 2870
    private const val masterCluesRares = 2871

    private const val pestControl = 518
    private const val roguesDen = 522
    private const val aerialFishing = 540
    private const val allPets = 535
    private const val chaosDruids = 533
    private const val cyclops = 532
    private const val gloughsExperiments = 526
    private const val motherlodeMine = 530
    private const val revenants = 525
    private const val rooftopAgility = 547
    private const val shootingStars = 2858
    private const val skillingPets = 529
    private const val slayer = 527
    private const val tzhaar = 536
    private const val miscellaneous = 534

    // Giant Mole: 1000x toadflax (unf) (3003), 1000x crushed birdnest (6694), 2x pet boosters (32152)
//            CollectionLogReward(488, arrayOf(gen(3003, 500), gen(6694, 500), gen(32152, 2))),

    val gp = "gp"
    val rewards = mutableListOf<CollectionLogReward>()

    infix fun Int.x(quantity: Int) = _Item(this, quantity)
    infix fun Int.m(item: String) = if(item == "gp") _Item(995, this.toM()) else _Item(DWARF_REMAINS, 1)

    init {
        rewards.addAll(
            arrayOf(
                /* Bosses */
                CollectionLogReward(abyssalSire, arrayOf(ultraMB x 1, UNSIRED x 5, petBooster x 3)),
                CollectionLogReward(
                    alchemicalHydra,
                    arrayOf(ultraMB x 1, petBooster x 3, slayerTaskPicker x 10, superMB x 1)
                ),
                CollectionLogReward(araxxor, arrayOf(ultraMB x 2, petBooster x 3, slayerTaskPicker x 10)),
                CollectionLogReward(
                    barrows,
                    arrayOf(superMB x 1, barrowsTotem x 10, malevolentEnergy x 250, ahrimKit x 4)
                ),
                CollectionLogReward(bryophyta, arrayOf(standardMB x 2, slayerTaskPicker x 5, herbBox x 25)),
                CollectionLogReward(
                    callistoAndArtio,
                    arrayOf(superMB x 1, standardMB x 2, bloodMoneyBooster x 3, larransBooster x 3)
                ),
                CollectionLogReward(
                    cerberus,
                    arrayOf(ultraMB x 1, superMB x 2, slayerTaskPicker x 10, asgarniaOrb x 1)
                ),
                CollectionLogReward(chaosElemental, arrayOf(superMB x 2, larransBooster x 3)),
                CollectionLogReward(chaosFanatic, arrayOf(superMB x 2)),
                CollectionLogReward(commanderZilyana, arrayOf(ultraMB x 1, superMB x 2)),
                CollectionLogReward(corporealBeast, arrayOf(dpin25 x 1, ultraMB x 3, superMB x 2)),
                CollectionLogReward(crazyArcheologist, arrayOf(superMB x 2)),
                CollectionLogReward(dagannothKings, arrayOf(imbueScroll x 1, superMB x 1, standardMB x 2)),
                CollectionLogReward(
                    dukeSucellus,
                    arrayOf(ultraMB x 1, superMB x 2, chromiumIngot x 1, echoVirtusKit x 3)
                ),
                CollectionLogReward(fightCaves, arrayOf(superMB x 1, tokkul x 50_000, fireCape x 1)),
                CollectionLogReward(gauntlet, arrayOf(ultraMB x 1, armourSeed x 3, enhancedCKey x 25, tirannwnOrb x 1)),
                CollectionLogReward(generalGraardor, arrayOf(ultraMB x 1, superMB x 2)),
                CollectionLogReward(
                    grotesqueGuardians,
                    arrayOf(doubleAmmoMold x 1, slayerTaskPicker x 5, slayerBooster x 5, morytaniaOrb x 1)
                ),
                CollectionLogReward(
                    hespori,
                    arrayOf(torstol x 100, snapdragon x 100, kourendOrb x 1, queenSecateurs x 1)
                ),
                CollectionLogReward(inferno, arrayOf(tokkul x 100_000, infernalCape x 1)),
                CollectionLogReward(kalphiteQueen, arrayOf(superMB x 1, standardMB x 2, desertOrb x 1)),
                CollectionLogReward(
                    kingBlackDragon,
                    arrayOf(superMB x 1, petBooster x 3, larransBooster x 3, wildyOrb x 1)
                ),
                CollectionLogReward(
                    kraken,
                    arrayOf(superMB x 1, slayerTaskPicker x 5, slayerBooster x 5, standardMB x 1)
                ),
                CollectionLogReward(kreearra, arrayOf(superMB x 1, standardMB x 2)),
                CollectionLogReward(krilTsutsaroth, arrayOf(superMB x 1, standardMB x 2)),
                CollectionLogReward(leviathan, arrayOf(ultraMB x 1, superMB x 2, chromiumIngot x 1, echoVirtusKit x 3)),
                CollectionLogReward(nex, arrayOf(dpin25 x 1, nexBooster x 10, bandosComp x 3, armadylComp x 3)),
                CollectionLogReward(nightmare, arrayOf(ultraMB x 1, superMB x 2, petBooster x 3, dpin10 x 1)),
                CollectionLogReward(obor, arrayOf(standardMB x 2, slayerTaskPicker x 5)),
                CollectionLogReward(
                    phantomMuspah,
                    arrayOf(superMB x 1, venatorShard x 1, frozenCache x 10, venatorKit x 1)
                ),
                CollectionLogReward(primal, arrayOf(regalMB x 2, ultraMB x 3, dpin25 x 2)),
                CollectionLogReward(riseOfTheSix, arrayOf(dpin10 x 1, ultraMB x 1, superMB x 2, standardMB x 3)),
                CollectionLogReward(sarachnis, arrayOf(superMB x 1, standardMB x 2)),
                CollectionLogReward(
                    scorpia,
                    arrayOf(superMB x 1, standardMB x 2, bloodMoneyBooster x 2, larransBooster x 2)
                ),
                CollectionLogReward(skotizo, arrayOf(superMB x 1, darkTotem x 10, ancientShardPack x 1)),
                CollectionLogReward(
                    thermonuclearSmokeDevil,
                    arrayOf(standardMB x 1, slayerBooster x 5, slayerTaskPicker x 5)
                ),
                CollectionLogReward(tormentedDemons, arrayOf(superMB x 1, standardMB x 2)),
                CollectionLogReward(vanstromKlause, arrayOf(standardMB x 2, bloodShard x 1)),
                CollectionLogReward(vardorvis, arrayOf(ultraMB x 1, superMB x 2, chromiumIngot x 1, echoVirtusKit x 3)),
                CollectionLogReward(
                    venenatisAndSpindel,
                    arrayOf(superMB x 1, standardMB x 2, bloodMoneyBooster x 3, larransBooster x 3)
                ),
                CollectionLogReward(
                    vetionAndCalvarion,
                    arrayOf(superMB x 1, standardMB x 2, bloodMoneyBooster x 3, larransBooster x 3)
                ),
                CollectionLogReward(vorkath, arrayOf(superMB x 2, standardMB x 3)),
                CollectionLogReward(whisperer, arrayOf(ultraMB x 1, superMB x 2, chromiumIngot x 1, echoVirtusKit x 3)),
                CollectionLogReward(wintertodt, arrayOf(superMB x 1, standardMB x 2, wintertodtCrate x 25)),
                CollectionLogReward(zalcano, arrayOf(skillingMB x 5, toolSeed x 1)),
                CollectionLogReward(zulrah, arrayOf(ultraMB x 1, superMB x 2, blowpipeKit x 1)),

                /* Raids */
                CollectionLogReward(
                    chambersOfXeric,
                    arrayOf(regalMB x 3, ultraMB x 5, coxSoloOrb x 25, omegaSpike x 1)
                ),
                CollectionLogReward(theatreOfBlood, arrayOf(regalMB x 3, ultraMB x 5, tobSoloOrb x 25, omegaHorn x 1)),
                CollectionLogReward(
                    tombsOfAmascut,
                    arrayOf(regalMB x 3, ultraMB x 5, toaSoloOrb x 25, omegaSymbol x 1)
                ),

                /* Treasure Trails */
                CollectionLogReward(beginnerClues, arrayOf(standardMB x 1, clueBooster x 5, sherlockNote x 25)),
                CollectionLogReward(
                    easyClues,
                    arrayOf(superMB x 1, standardMB x 2, clueBooster x 5, sherlockNote x 50)
                ),
                CollectionLogReward(
                    mediumClues,
                    arrayOf(superMB x 2, standardMB x 3, clueBooster x 5, sherlockNote x 50)
                ),
                CollectionLogReward(hardClues, arrayOf(ultraMB x 1, superMB x 2, clueBooster x 10, sherlockNote x 75)),
                CollectionLogReward(
                    eliteClues,
                    arrayOf(ultraMB x 2, superMB x 3, clueBooster x 10, sherlockNote x 100)
                ),
                CollectionLogReward(
                    masterClues,
                    arrayOf(regalMB x 1, ultraMB x 3, clueBooster x 15, sherlockNote x 150)
                ),
                CollectionLogReward(
                    hardCluesRares,
                    arrayOf(ultraMB x 2, superMB x 3, clueBooster x 10, sherlockNote x 100)
                ),
                CollectionLogReward(
                    eliteCluesRares,
                    arrayOf(regalMB x 1, ultraMB x 3, clueBooster x 15, sherlockNote x 150)
                ),
                CollectionLogReward(
                    masterCluesRares,
                    arrayOf(regalMB x 3, ultraMB x 5, clueBooster x 20, sherlockNote x 300)
                ),

                /* Minigames */
                CollectionLogReward(pestControl, arrayOf(skillingMB x 5, crystalKey x 7)),
                CollectionLogReward(roguesDen, arrayOf(skillingMB x 3, crystalKey x 5)),

                /* Other */
                CollectionLogReward(aerialFishing, arrayOf(standardMB x 1, skillingMB x 5)),
                CollectionLogReward(allPets, arrayOf(dpin100 x 3, regalMB x 10, ultraMB x 15, nrPartyhat x 1)),
                CollectionLogReward(chaosDruids, arrayOf(larransBooster x 5, chaosRobesOrnKit x 3)),
                CollectionLogReward(cyclops, arrayOf(standardMB x 1, dragonDefOrnKit x 1)),
                CollectionLogReward(
                    gloughsExperiments,
                    arrayOf(standardMB x 2, zenyte x 1, onyx x 1, ballistaOrnKit x 1)
                ),
                CollectionLogReward(motherlodeMine, arrayOf(dragonPickOrnKit x 1, skillingMB x 10)),
                CollectionLogReward(
                    revenants,
                    arrayOf(pvmMB x 5, bloodMoneyBooster x 10, larransBooster x 10, revenantBooster x 10)
                ),
                CollectionLogReward(rooftopAgility, arrayOf(standardMB x 1, gracefulDye x 15)),
                CollectionLogReward(shootingStars, arrayOf(standardMB x 1, stardust x 5_000)),
                CollectionLogReward(skillingPets, arrayOf(dpin100 x 1, ultraMB x 3, skillingMB x 25)),
                CollectionLogReward(
                    slayer,
                    arrayOf(regalMB x 3, slayerTaskPicker x 25, slayerTaskReset x 25, superiorBell x 50)
                ),
                CollectionLogReward(tzhaar, arrayOf(standardMB x 1, onyx x 1, crystalKey x 5)),
                CollectionLogReward(miscellaneous, arrayOf(regalMB x 3, ultraMB x 3, superMB x 3, standardMB x 3))
        ))
    }

    fun gen(id: Int, q: Int) = _Item(id, q)

    @JvmStatic
    fun getRewardSet(struct: Int): CollectionLogRewardSet {
        return rewards.find { it.struct == struct }?.toSet() ?: CollectionLogRewardSet()
    }


    private fun Int.toM(): Int {
        return this * 1_000_000
    }
}