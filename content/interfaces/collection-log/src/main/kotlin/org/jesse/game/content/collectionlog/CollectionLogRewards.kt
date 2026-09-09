package org.jesse.game.content.collectionlog

import org.jesse.game.item.ids.*
import org.jesse.game.item._Item



object CollectionLogRewards {

    /* Reward List */
    private const val standardMB = MYSTERY_BOX
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
    private const val darkTotem = DARK_TOTEM
    private const val bloodShard = BLOOD_SHARD
    private const val wintertodtCrate = SUPPLY_CRATE
    private const val blowpipeKit = TRAILBLAZER_RELOADED_BLOWPIPE_ORNAMENT_KIT
    private const val crystalKey = CRYSTAL_KEY
    private const val chaosRobesOrnKit = ELDER_CHAOS_ROBES_ORNAMENT_KIT
    private const val dragonDefOrnKit = DRAGON_DEFENDER_ORNAMENT_KIT
    private const val dragonPickOrnKit = DRAGON_PICKAXE_UPGRADE_KIT
    private const val ballistaOrnKit = HEAVY_BALLISTA_ORNAMENT_KIT
    private const val zenyte = ZENYTE
    private const val onyx = ONYX
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

    val gp = "gp"
    val rewards = mutableListOf<CollectionLogReward>()

    infix fun Int.x(quantity: Int) = _Item(this, quantity)
    infix fun Int.m(item: String) = if(item == "gp") _Item(995, this.toM()) else _Item(DWARF_REMAINS, 1)

    init {
        rewards.addAll(
            arrayOf(
                /* Bosses */
                CollectionLogReward(abyssalSire, arrayOf(UNSIRED x 5)),
                CollectionLogReward(
                    barrows,
                    arrayOf(ahrimKit x 4)
                ),
                CollectionLogReward(bryophyta, arrayOf(standardMB x 2, herbBox x 25)),
                CollectionLogReward(
                    callistoAndArtio,
                    arrayOf(standardMB x 2)
                ),
                CollectionLogReward(
                    cerberus,
                    arrayOf(asgarniaOrb x 1)
                ),
                CollectionLogReward(dagannothKings, arrayOf(imbueScroll x 1, standardMB x 2)),
                CollectionLogReward(
                    dukeSucellus,
                    arrayOf(chromiumIngot x 1, echoVirtusKit x 3)
                ),
                CollectionLogReward(fightCaves, arrayOf(tokkul x 50_000, fireCape x 1)),
                CollectionLogReward(gauntlet, arrayOf(armourSeed x 3, enhancedCKey x 25, tirannwnOrb x 1)),
                CollectionLogReward(
                    grotesqueGuardians,
                    arrayOf(doubleAmmoMold x 1, morytaniaOrb x 1)
                ),
                CollectionLogReward(
                    hespori,
                    arrayOf(torstol x 100, snapdragon x 100, kourendOrb x 1, queenSecateurs x 1)
                ),
                CollectionLogReward(inferno, arrayOf(tokkul x 100_000, infernalCape x 1)),
                CollectionLogReward(kalphiteQueen, arrayOf(standardMB x 2, desertOrb x 1)),
                CollectionLogReward(
                    kingBlackDragon,
                    arrayOf(wildyOrb x 1)
                ),
                CollectionLogReward(
                    kraken,
                    arrayOf(standardMB x 1)
                ),
                CollectionLogReward(kreearra, arrayOf(standardMB x 2)),
                CollectionLogReward(krilTsutsaroth, arrayOf(standardMB x 2)),
                CollectionLogReward(leviathan, arrayOf(chromiumIngot x 1, echoVirtusKit x 3)),
                CollectionLogReward(nex, arrayOf(bandosComp x 3, armadylComp x 3)),
                CollectionLogReward(obor, arrayOf(standardMB x 2)),
                CollectionLogReward(
                    phantomMuspah,
                    arrayOf(venatorShard x 1, frozenCache x 10, venatorKit x 1)
                ),
                CollectionLogReward(sarachnis, arrayOf(standardMB x 2)),
                CollectionLogReward(
                    scorpia,
                    arrayOf(standardMB x 2)
                ),
                CollectionLogReward(skotizo, arrayOf(darkTotem x 10)),
                CollectionLogReward(
                    thermonuclearSmokeDevil,
                    arrayOf(standardMB x 1)
                ),
                CollectionLogReward(tormentedDemons, arrayOf(standardMB x 2)),
                CollectionLogReward(vardorvis, arrayOf(chromiumIngot x 1, echoVirtusKit x 3)),
                CollectionLogReward(
                    venenatisAndSpindel,
                    arrayOf(standardMB x 2)
                ),
                CollectionLogReward(
                    vetionAndCalvarion,
                    arrayOf(standardMB x 2)
                ),
                CollectionLogReward(vorkath, arrayOf(standardMB x 3)),
                CollectionLogReward(whisperer, arrayOf(chromiumIngot x 1, echoVirtusKit x 3)),
                CollectionLogReward(wintertodt, arrayOf(standardMB x 2, wintertodtCrate x 25)),
                CollectionLogReward(zalcano, arrayOf(toolSeed x 1)),
                CollectionLogReward(zulrah, arrayOf(blowpipeKit x 1)),

                /* Treasure Trails */
                CollectionLogReward(beginnerClues, arrayOf(standardMB x 1)),
                CollectionLogReward(
                    easyClues,
                    arrayOf(standardMB x 2)
                ),
                CollectionLogReward(
                    mediumClues,
                    arrayOf(standardMB x 3)
                ),

                /* Minigames */
                CollectionLogReward(pestControl, arrayOf(crystalKey x 7)),
                CollectionLogReward(roguesDen, arrayOf(crystalKey x 5)),

                /* Other */
                CollectionLogReward(aerialFishing, arrayOf(standardMB x 1)),
                CollectionLogReward(chaosDruids, arrayOf(chaosRobesOrnKit x 3)),
                CollectionLogReward(cyclops, arrayOf(standardMB x 1, dragonDefOrnKit x 1)),
                CollectionLogReward(
                    gloughsExperiments,
                    arrayOf(standardMB x 2, zenyte x 1, onyx x 1, ballistaOrnKit x 1)
                ),
                CollectionLogReward(motherlodeMine, arrayOf(dragonPickOrnKit x 1)),
                CollectionLogReward(rooftopAgility, arrayOf(standardMB x 1)),
                CollectionLogReward(shootingStars, arrayOf(standardMB x 1, stardust x 5_000)),
                CollectionLogReward(tzhaar, arrayOf(standardMB x 1, onyx x 1, crystalKey x 5)),
                CollectionLogReward(miscellaneous, arrayOf(standardMB x 3))
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
