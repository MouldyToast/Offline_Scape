package com.near_reality.cache_tool.packing.custom

import com.near_reality.game.item.CustomItemId
import com.zenyte.game.item.ItemId
import mgi.types.config.enums.EnumDefinitions
import net.runelite.cache.util.ScriptVarType

object NearRealityCustomEnumsPacker {
    @JvmStatic
    fun pack() {

        /* Bosses Collection Log Entries */
        EnumDefinitions.get(2103).apply {
            values.clear()
            var idx = 0
            this.values[idx++] = 476
            this.values[idx++] = 539
            this.values[idx++] = 995
            this.values[idx++] = 477
            this.values[idx++] = 478
            this.values[idx++] = 479
            this.values[idx++] = 480
            this.values[idx++] = 481
            this.values[idx++] = 482
            this.values[idx++] = 483
            this.values[idx++] = 484
            this.values[idx++] = 485
            this.values[idx++] = 486
            this.values[idx++] = 4652   // duke
            this.values[idx++] = 500    // fight caves
            this.values[idx++] = 909    // Fortis Colosseum
            this.values[idx++] = 605    // gauntlet
            this.values[idx++] = 10300  // gano
            this.values[idx++] = 487    // graardor
            this.values[idx++] = 488    // giant mole
            this.values[idx++] = 489    // grot guardians
            this.values[idx++] = 541    // hespori
            this.values[idx++] = 499    // the inferno
            this.values[idx++] = 490    // kalphite queen
            this.values[idx++] = 491    // king black dragon
            this.values[idx++] = 492    // kraken
            this.values[idx++] = 493    // kree'arra
            this.values[idx++] = 494    // kril
            this.values[idx++] = 4655   // leviathan
            this.values[idx++] = 3769   // nex
            this.values[idx++] = 1263   // the nightmare
            this.values[idx++] = 495    // obor
            this.values[idx++] = 10501   // primals
            this.values[idx++] = 4455   // muspah
            this.values[idx++] = 10321  // rots
            this.values[idx++] = 601    // sarachnis
            this.values[idx++] = 496    // scorpia
            this.values[idx++] = 497    // skotizo
            this.values[idx++] = 10500  // strykewyrms
            this.values[idx++] = 498    // thermy
            this.values[idx++] = 10322  // Vanstrom Klause
            this.values[idx++] = 4653   // Vardorvis
            this.values[idx++] = 501    // Venenatis
            this.values[idx++] = 502    // Vetion
            this.values[idx++] = 503    // Vorkath
            this.values[idx++] = 4654   // Whisperer
            this.values[idx++] = 10502  // Wild Mole
            this.values[idx++] = 504    // Wintertodt
            this.values[idx++] = 604    // Zalcano
            this.values[idx] = 505      // Zulrah
            this.pack()
        }

        // Slayer log - add statue pieces
        EnumDefinitions.get(2162).apply {
            var index = this.size
            this.values[index++] = ItemId.SLAYER_BASE
            this.values[index++] = ItemId.SLAYER_SHAFT
            this.values[index++] = ItemId.SLAYER_LEFT_BONE
            this.values[index] = ItemId.SLAYER_RIGHT_BONE
            this.pack()
        }

        // All Pets log
        EnumDefinitions.get(2158).apply {
            var index = this.size
            this.values[index] = ItemId.GANODERMIC_RUNT
            this.pack()
        }

        // Grotesque Guardians
        EnumDefinitions.get(2122).apply {
            var index = this.size
            this.values[index] = ItemId.DOUBLE_AMMO_MOULD
            this.pack()
        }

        /* Barrows */
        EnumDefinitions.get(2110).apply {
            var index = this.size
            this.values[index++] = ItemId.BARROWS_WHIP
            this.pack()
        }

        /* Chaos Elemental */
        EnumDefinitions.get(2114).apply {
            var index = this.size
            this.values[index++] = ItemId.ELEMENTAL_WHIP
            this.pack()
        }

        //TOA
        EnumDefinitions.get(4805).apply {
            values.clear()
            values[0] = 27277  // Tumeken's shadow (uncharged)Removed
            values[1] = 25985  // Elidinis' ward
            values[2] = 27226  // Masori mask
            values[3] = 27229  // Masori body
            values[4] = 27232  // Masori chaps
            values[5] = 25975  // Lightbearer
            values[6] = 26219  // Osmumten's fang
            values[7] = 27279  // Thread of elidinis
            values[8] = 27283  // Breach of the scarab
            values[9] = 27285  // Eye of the corruptor
            values[10] = 27289 // Jewel of the sun
            values[11] = 27255 // Menaphite ornament kit
            values[12] = 27248 // Cursed phalanx
            values[13] = 27372 // Masori crafting kit
            values[14] = 27257 // Icthlarin's shroud (tier 1)
            values[15] = 27259 // Icthlarin's shroud (tier 2)
            values[16] = 27261 // Icthlarin's shroud (tier 3)
            values[17] = 27263 // Icthlarin's shroud (tier 4)
            values[18] = 27265 // Icthlarin's shroud (tier 5)
            values[19] = 27377 // Remnant of Akkha
            values[20] = 27378 // Remnant of Ba-Ba
            values[21] = 27379 // Remnant of Kephri
            values[22] = 27380 // Remnant of Zebak
            values[23] = 27381 // Ancient remnant
            this.pack()
        }

        // Vanstrom Klause
        EnumDefinitions.create(10030, ScriptVarType.INTEGER, ScriptVarType.NAMEDOBJ).apply {
            this.values[0] = ItemId.BLOOD_SHARD
            this.pack()
        }

        // Ganodermic Beast
        EnumDefinitions.create(10025, ScriptVarType.INTEGER, ScriptVarType.NAMEDOBJ).apply {
            this.values[0] = ItemId.LIME_WHIP
            this.values[1] = ItemId.POLYPORE_STAFF_DEG
            this.values[2] = ItemId.ANCIENT_EYE
            this.values[3] = ItemId.DRAGON_KITE
            this.values[4] = ItemId.PVP_MYSTERY_BOX
            this.values[5] = ItemId.POLYPORE_SPORES
            this.pack()
        }

        // Duke
        EnumDefinitions.get(5148).apply {
            this.values[5] = ItemId.MAGUS_ICON
            this.pack();
        }

        // Vardorvis
        EnumDefinitions.get(5149).apply {
            this.values[5] = ItemId.ULTOR_ICON
            this.pack();
        }

        /* Strykewyrms */
        EnumDefinitions.create(10500, ScriptVarType.INTEGER, ScriptVarType.NAMEDOBJ).apply {
            this.values[0] = ItemId.STAFF_OF_LIGHT
            this.values[1] = ItemId.CHAOTIC_STAFF
            this.values[2] = ItemId.CHAOTIC_CROSSBOW
            this.values[3] = ItemId.CHAOTIC_KITESHIELD
            this.values[4] = ItemId.EAGLE_EYE_KITESHIELD
            this.values[5] = ItemId.FARSEER_KITESHIELD
            this.pack()
        }

        /* Primal Items */
        EnumDefinitions.create(10501, ScriptVarType.INTEGER, ScriptVarType.NAMEDOBJ).apply {
            this.values[0] = ItemId.PRIMAL_2H_SWORD
            this.values[1] = ItemId.PRIMAL_PICKAXE
            this.values[2] = ItemId.PRIMAL_KITESHIELD
            this.values[3] = ItemId.PRIMAL_SPEAR
            this.values[4] = ItemId.PRIMAL_HATCHET
            this.values[5] = ItemId.PRIMAL_MAUL
            this.values[6] = ItemId.PRIMAL_DAGGER
            this.values[7] = ItemId.PRIMAL_LONGSWORD
            this.values[8] = ItemId.PRIMAL_RAPIER
            this.values[9] = ItemId.PRIMAL_WARHAMMER
            this.values[10] = ItemId.PRIMAL_BOOTS
            this.values[11] = ItemId.PRIMAL_GAUNTLETS
            this.values[12] = ItemId.PRIMAL_FULL_HELM
            this.values[13] = ItemId.PRIMAL_PLATEBODY
            this.values[14] = ItemId.PRIMAL_CHAINBODY
            this.values[15] = ItemId.PRIMAL_PLATESKIRT
            this.values[16] = ItemId.PRIMAL_PLATELEGS
            this.pack()
        }

        /* Wild Mole */
        EnumDefinitions.create(10502, ScriptVarType.INTEGER, ScriptVarType.NAMEDOBJ).apply {
            this.values[0] = ItemId.GHOSTLY_PARTYHAT
            this.values[1] = ItemId.DEMONHORN_NECKLACE
            this.values[2] = ItemId.DEATH_CAPE
            this.values[3] = ItemId.DRAGON_KITE
            this.values[4] = ItemId.SPIRIT_CAPE
            this.values[5] = ItemId.MERCENARY_GLOVES
            this.values[6] = ItemId.MYSTERY_BOX
            this.values[7] = ItemId.SKILLING_MYSTERY_BOX
            this.values[8] = ItemId.PRIMAL_FULL_HELM
            this.values[9] = ItemId.PRIMAL_CHAINBODY
            this.values[10] = ItemId.PRIMAL_PLATESKIRT
            this.values[11] = ItemId.PRIMAL_PLATELEGS
            this.values[12] = ItemId.PRIMAL_BOOTS
            this.values[13] = ItemId.PRIMAL_GAUNTLETS
            this.pack()
        }

        EnumDefinitions.create(10024, ScriptVarType.INTEGER, ScriptVarType.OBJ).apply {
            defaultInt = -1
            this.values[0] = 565
            this.values[1] = 560
            this.values[2] = 9075
            this.values[3] = 557
            this.values[4] = 555
            this.values[5] = 562
            this.values[6] = 566
            this.values[7] = 554
            this.values[8] = 556
            this.values[9] = 561
            this.values[10] = 563
            this.values[11] = 564
            this.values[12] = 21880
            this.values[13] = 3144
            this.values[14] = 385
            this.values[15] = 391
            this.values[16] = 397
            this.values[17] = 13441
            this.values[18] = 11936
            this.values[19] = 6685
            this.values[20] = 10925
            this.values[21] = 3024
            this.values[22] = 2434
            this.values[23] = 2440
            this.values[24] = 2442
            this.values[25] = 2436
            this.values[26] = 12695
            this.values[27] = 2444
            this.values[28] = 3040
            this.values[29] = 4417
            this.values[30] = 11090
            this.values[31] = 2550
            this.values[32] = 5698
            this.values[33] = 24225
            this.values[34] = 10887
            this.values[35] = 11802
            this.values[36] = 20784
            this.pack()
        }

        EnumDefinitions.create(20002, ScriptVarType.INTEGER, ScriptVarType.STRING).apply {
            defaultString = "Overview"
            this.values[0] = "Overview"
            this.values[1] = "Melee"
            this.values[2] = "Ranged"
            this.values[3] = "Magic"
            this.values[4] = "Supplies"
            this.values[5] = "Skilling"
            this.values[6] = "Jewelry"
            this.values[7] = "General"
            this.values[8] = "Slayer"
            this.values[9] = "Bounty Hunter"
            this.values[10] = "Capes"
            this.values[11] = "Blood Money"
            this.values[12] = "Loyalty"
            this.values[13] = "Vote"
            this.pack()
        }

        /* Slayer Task Defs - 82 replaces unused Gorak */
        EnumDefinitions.get(693).apply {
            this.values[82] = "Strykewyrms"
            this.pack()
        }

        EnumDefinitions.get(840).apply {
            values.clear()
            var idx = 0
            this.values[idx++] = ItemId.SLAYER_RING_8                 // Slayer ring (8)
            this.values[idx++] = ItemId.BROAD_BOLTS                     // Broad bolts
            this.values[idx++] = ItemId.BROAD_ARROWS_4160                    // Broad arrows
            this.values[idx++] = ItemId.HERB_SACK                       // Herb sack
            this.values[idx++] = ItemId.RUNE_POUCH                      // Rune pouch
            this.values[idx++] = ItemId.FIGHTER_TORSO                   // Fighter torso
            this.values[idx++] = ItemId.DWARF_CANNON_SET                // Dwarf cannon set
            this.values[idx++] = ItemId.BARRELCHEST_ANCHOR                // Barrelchest anchor
            this.values[idx++] = ItemId.HERB_BOX                        // Herb box
            this.values[idx++] = ItemId.SALVE_AMULET                   // Salve amulet
            this.values[idx++] = ItemId.SHAYZIEN_HELM_5                 // Shayzien helm (5)
            this.values[idx++] = ItemId.SHAYZIEN_PLATEBODY_5                 // Shayzien body (5)
            this.values[idx++] = ItemId.SHAYZIEN_GREAVES_5              // Shayzien greaves (5)
            this.values[idx++] = ItemId.SHAYZIEN_GLOVES_5               // Shayzien gloves (5)
            this.values[idx++] = ItemId.SHAYZIEN_BOOTS_5               // Shayzien boots (5)
            this.values[idx++] = ItemId.SCROLL_OF_IMBUING               // Scroll of imbuing
            this.values[idx++] = ItemId.HOLY_WRENCH                   // Holy wrench
            this.values[idx++] = ItemId.KERIS                          // Keris
            this.values[idx++] = ItemId.KERIS_PARTISAN                  // Keris partisan
            this.values[idx++] = ItemId.WOLFBANE                        // Wolfbane
            this.values[idx++] = ItemId.IVANDIS_FLAIL                  // Ivandis flail
            this.values[idx++] = ItemId.BLISTERWOOD_FLAIL               // Blisterwood flail
            this.values[idx++] = ItemId.TARNS_DIARY                     // Tarn's diary
            this.values[idx++] = ItemId.VS_SHIELD_24266                       // V's shield
            this.values[idx++] = ItemId.DARKLIGHT                     // Darklight
            this.values[idx++] = ItemId.BRACELET_OF_ETHEREUM_UNCHARGED  // Bracelet of ethereum (uncharged)
            this.values[idx++] = ItemId.CRYSTAL_KEY                     // Crystal key
            this.values[idx++] = ItemId.CANNONBALL                      // Cannonball
            this.values[idx++] = ItemId.ASH_SANCTIFIER                  // Ash sanctifier
            this.values[idx++] = ItemId.DRAGON_DART                     // Dragon dart
            this.values[idx++] = 32161              // Enhanced excalibur
            this.values[idx++] = 26255            // Dragon Hunter Gloves
            this.values[idx++] = 26300                // Dragonhide pouch
            this.values[idx++] = 26304                      // Bone pouch
            this.values[idx++] = ItemId.BONECRUSHER                    // Bonecrusher
            this.values[idx] = ItemId.BALMUNG                         // Balmung
            this.pack()
        }


        EnumDefinitions.get(841).apply {
            this.values[ItemId.CANNONBALL] = 10   // Cannonball
            this.values[ItemId.BROAD_ARROWS_4160] = 250  // Broad arrows
            this.values[ItemId.DRAGON_DART] = 2    // Dragon dart
            this.values[ItemId.BROAD_BOLTS] = 250  // Broad bolts

            this.pack()
        }

        EnumDefinitions.get(842).apply {
            values.clear()
            this.values[ItemId.CANNONBALL] = 1
            this.values[ItemId.CRYSTAL_KEY] = 50
            this.values[ItemId.WOLFBANE] = 100
            this.values[ItemId.SALVE_AMULET] = 40
            this.values[ItemId.BROAD_ARROWS_4160] = 35
            this.values[ItemId.HOLY_WRENCH] = 200
            this.values[ItemId.DARKLIGHT] = 400
            this.values[ItemId.FIGHTER_TORSO] = 400
            this.values[ItemId.KERIS] = 150
            this.values[ItemId.TARNS_DIARY] = 40
            this.values[ItemId.BARRELCHEST_ANCHOR] = 200
            this.values[ItemId.DRAGON_DART] = 1
            this.values[ItemId.HERB_BOX] = 10
            this.values[ItemId.SLAYER_RING_8] = 75
            this.values[ItemId.BROAD_BOLTS] = 35
            this.values[ItemId.RUNE_POUCH] = 750
            this.values[ItemId.DWARF_CANNON_SET] = 300
            this.values[ItemId.BONECRUSHER] = 250
            this.values[ItemId.HERB_SACK] = 750
            this.values[ItemId.SHAYZIEN_GLOVES_5] = 40
            this.values[ItemId.SHAYZIEN_BOOTS_5] = 40
            this.values[ItemId.SHAYZIEN_HELM_5] = 40
            this.values[ItemId.SHAYZIEN_GREAVES_5] = 40
            this.values[ItemId.SHAYZIEN_PLATEBODY_5] = 40
            this.values[ItemId.BRACELET_OF_ETHEREUM_UNCHARGED] = 100
            this.values[ItemId.IVANDIS_FLAIL] = 150
            this.values[ItemId.VS_SHIELD_24266] = 50
            this.values[ItemId.BLISTERWOOD_FLAIL] = 300
            this.values[ItemId.ASH_SANCTIFIER] = 300
            this.values[ItemId.KERIS_PARTISAN] = 400
            this.values[26255] = 400
            this.values[26300] = 500
            this.values[26304] = 500
            this.values[26706] = 150
            this.values[32161] = 400
            this.values[32612] = 750

            this.pack()
        }

        EnumDefinitions.get(843).apply {
            values.clear()
            this.values[ItemId.CANNONBALL] = "Ammo for the Dwarf Cannon."
            this.values[ItemId.CRYSTAL_KEY] = "A mysterious key for a mysterious chest."
            this.values[ItemId.WOLFBANE] = "A silver dagger that can prevent werewolves from changing form."
            this.values[ItemId.SALVE_AMULET] = "An amulet which increases the wearer's strength and accuracy by 15% when fighting the undead."
            this.values[ItemId.BROAD_ARROWS_4160] = "Arrows that can pierce the hides of creatures such as Turoth and Kurasks. Levels 55 Slayer and 50 Ranged required, and a magic bow or better."
            this.values[ItemId.HOLY_WRENCH] = "A shining paragon of wrenchly virtue."
            this.values[ItemId.DARKLIGHT] = "The magical sword 'Silverlight', enhanced with the blood of Agrith-Naar."
            this.values[ItemId.FIGHTER_TORSO] = "A torso worn by penance fighters. Requires level 40 Defence."
            this.values[ItemId.KERIS] = "A sharp mystical dagger that can penetrate through Kalphite chitin."
            this.values[ItemId.TARNS_DIARY] = "Tarn Razorlor's diary, used for enchanting salve amulets."
            this.values[ItemId.BARRELCHEST_ANCHOR] = "An anchor used by the Barrelchest. Requires level 60 Attack & 40 Strength."
            this.values[ItemId.DRAGON_DART] = "A deadly throwing dart with a dragon tip."
            this.values[ItemId.HERB_BOX] = "A herb box containing an assortment of random herbs, giving ten herbs per box."
            this.values[ItemId.SLAYER_RING_8] = "A wieldable ring that can check your task progress. It has 8 charges for teleporting to useful Slayer sites."
            this.values[ItemId.BROAD_BOLTS] = "Crossbow bolts that can pierce the hides of creatures such as Turoth and Kurasks. Levels 55 Slayer and 61 Ranged required, and a runite crossbow or better."
            this.values[ItemId.RUNE_POUCH] = "The rune pouch has the ability to store up to 16,000 runes of 3 types."
            this.values[ItemId.DWARF_CANNON_SET] = "A powerful ranging device that fires metal balls."
            this.values[ItemId.BONECRUSHER] = "A crusher for the toughest of bones."
            this.values[ItemId.HERB_SACK] = "The herb sack has the ability to store up to 30 of each major grimy herb. Requires 58 Herblore to use."
            this.values[ItemId.SHAYZIEN_GLOVES_5] = "Some gloves from the Shayzien guards, used for protection against lizardmen."
            this.values[ItemId.SHAYZIEN_BOOTS_5] = "Some boots from the Shayzien guards, used for protection against lizardmen."
            this.values[ItemId.SHAYZIEN_HELM_5] = "A helmet from the Shayzien guards, used for protection against lizardmen."
            this.values[ItemId.SHAYZIEN_GREAVES_5] = "Some greaves from the Shayzien guards, used for protection against lizardmen."
            this.values[ItemId.SHAYZIEN_PLATEBODY_5] = "A platebody from the Shayzien guards, used for protection against lizardmen."
            this.values[ItemId.BRACELET_OF_ETHEREUM_UNCHARGED] = "The bracelet is dull and powerless."
            this.values[ItemId.IVANDIS_FLAIL] = "Used to fight Vampyres."
            this.values[ItemId.VS_SHIELD_24266] = "A recreation of V's mighty shield."
            this.values[ItemId.BLISTERWOOD_FLAIL] = "Super effective against Vampyres."
            this.values[ItemId.ASH_SANCTIFIER] = "Scatters ashes."
            this.values[ItemId.KERIS_PARTISAN] = "A mystical-feeling polearm that can easily penetrate through Kalphite chitin."
            this.values[26255] = "Has the same stats as Barrows Gloves, in addition gives a 25% slayer xp bonus on dragon slayer tasks, and 15% more accuracy on dragons. Requires 41 defence."
            this.values[26300] = "You can carry dragonhide in here."
            this.values[26304] = "A pouch for storing bones."
            this.values[26706] = "A scroll that can be used to imbue items."
            this.values[32161] = "A powerful defensive weapon."
            this.values[32612] = "Two-handed weapon made of some banite ore and sharpened on the hides of dagannoths, deals 25% extra damage against dagannoths with 15% increased accuracy."
            this.pack()
        }
    }
}