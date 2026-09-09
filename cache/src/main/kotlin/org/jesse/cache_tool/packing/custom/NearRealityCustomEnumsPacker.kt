package org.jesse.cache_tool.packing.custom

import org.jesse.game.item.ids.*
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
            this.values[idx++] = 4455   // muspah
            this.values[idx++] = 601    // sarachnis
            this.values[idx++] = 496    // scorpia
            this.values[idx++] = 497    // skotizo
            this.values[idx++] = 498    // thermy
            this.values[idx++] = 4653   // Vardorvis
            this.values[idx++] = 501    // Venenatis
            this.values[idx++] = 502    // Vetion
            this.values[idx++] = 503    // Vorkath
            this.values[idx++] = 4654   // Whisperer
            this.values[idx++] = 504    // Wintertodt
            this.values[idx++] = 604    // Zalcano
            this.values[idx] = 505      // Zulrah
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

        // Duke
        EnumDefinitions.get(5148).apply {
            this.values[5] = MAGUS_ICON
            this.pack();
        }

        // Vardorvis
        EnumDefinitions.get(5149).apply {
            this.values[5] = ULTOR_ICON
            this.pack();
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

        EnumDefinitions.get(840).apply {
            values.clear()
            var idx = 0
            this.values[idx++] = SLAYER_RING_8                 // Slayer ring (8)
            this.values[idx++] = BROAD_BOLTS                     // Broad bolts
            this.values[idx++] = BROAD_ARROWS_4160                    // Broad arrows
            this.values[idx++] = HERB_SACK                       // Herb sack
            this.values[idx++] = RUNE_POUCH                      // Rune pouch
            this.values[idx++] = FIGHTER_TORSO                   // Fighter torso
            this.values[idx++] = DWARF_CANNON_SET                // Dwarf cannon set
            this.values[idx++] = BARRELCHEST_ANCHOR                // Barrelchest anchor
            this.values[idx++] = HERB_BOX                        // Herb box
            this.values[idx++] = SALVE_AMULET                   // Salve amulet
            this.values[idx++] = SHAYZIEN_HELM_5                 // Shayzien helm (5)
            this.values[idx++] = SHAYZIEN_PLATEBODY_5                 // Shayzien body (5)
            this.values[idx++] = SHAYZIEN_GREAVES_5              // Shayzien greaves (5)
            this.values[idx++] = SHAYZIEN_GLOVES_5               // Shayzien gloves (5)
            this.values[idx++] = SHAYZIEN_BOOTS_5               // Shayzien boots (5)
            this.values[idx++] = SCROLL_OF_IMBUING               // Scroll of imbuing
            this.values[idx++] = HOLY_WRENCH                   // Holy wrench
            this.values[idx++] = KERIS                          // Keris
            this.values[idx++] = KERIS_PARTISAN                  // Keris partisan
            this.values[idx++] = WOLFBANE                        // Wolfbane
            this.values[idx++] = IVANDIS_FLAIL                  // Ivandis flail
            this.values[idx++] = BLISTERWOOD_FLAIL               // Blisterwood flail
            this.values[idx++] = TARNS_DIARY                     // Tarn's diary
            this.values[idx++] = VS_SHIELD_24266                       // V's shield
            this.values[idx++] = DARKLIGHT                     // Darklight
            this.values[idx++] = BRACELET_OF_ETHEREUM_UNCHARGED  // Bracelet of ethereum (uncharged)
            this.values[idx++] = CRYSTAL_KEY                     // Crystal key
            this.values[idx++] = CANNONBALL                      // Cannonball
            this.values[idx++] = ASH_SANCTIFIER                  // Ash sanctifier
            this.values[idx++] = DRAGON_DART                     // Dragon dart
            this.values[idx++] = 32161              // Enhanced excalibur
            this.values[idx++] = 26300                // Dragonhide pouch
            this.values[idx++] = 26304                      // Bone pouch
            this.values[idx++] = BONECRUSHER                    // Bonecrusher
            this.pack()
        }


        EnumDefinitions.get(841).apply {
            this.values[CANNONBALL] = 10   // Cannonball
            this.values[BROAD_ARROWS_4160] = 250  // Broad arrows
            this.values[DRAGON_DART] = 2    // Dragon dart
            this.values[BROAD_BOLTS] = 250  // Broad bolts

            this.pack()
        }

        EnumDefinitions.get(842).apply {
            values.clear()
            this.values[CANNONBALL] = 1
            this.values[CRYSTAL_KEY] = 50
            this.values[WOLFBANE] = 100
            this.values[SALVE_AMULET] = 40
            this.values[BROAD_ARROWS_4160] = 35
            this.values[HOLY_WRENCH] = 200
            this.values[DARKLIGHT] = 400
            this.values[FIGHTER_TORSO] = 400
            this.values[KERIS] = 150
            this.values[TARNS_DIARY] = 40
            this.values[BARRELCHEST_ANCHOR] = 200
            this.values[DRAGON_DART] = 1
            this.values[HERB_BOX] = 10
            this.values[SLAYER_RING_8] = 75
            this.values[BROAD_BOLTS] = 35
            this.values[RUNE_POUCH] = 750
            this.values[DWARF_CANNON_SET] = 300
            this.values[BONECRUSHER] = 250
            this.values[HERB_SACK] = 750
            this.values[SHAYZIEN_GLOVES_5] = 40
            this.values[SHAYZIEN_BOOTS_5] = 40
            this.values[SHAYZIEN_HELM_5] = 40
            this.values[SHAYZIEN_GREAVES_5] = 40
            this.values[SHAYZIEN_PLATEBODY_5] = 40
            this.values[BRACELET_OF_ETHEREUM_UNCHARGED] = 100
            this.values[IVANDIS_FLAIL] = 150
            this.values[VS_SHIELD_24266] = 50
            this.values[BLISTERWOOD_FLAIL] = 300
            this.values[ASH_SANCTIFIER] = 300
            this.values[KERIS_PARTISAN] = 400
            this.values[26300] = 500
            this.values[26304] = 500
            this.values[26706] = 150
            this.values[32161] = 400

            this.pack()
        }

        EnumDefinitions.get(843).apply {
            values.clear()
            this.values[CANNONBALL] = "Ammo for the Dwarf Cannon."
            this.values[CRYSTAL_KEY] = "A mysterious key for a mysterious chest."
            this.values[WOLFBANE] = "A silver dagger that can prevent werewolves from changing form."
            this.values[SALVE_AMULET] = "An amulet which increases the wearer's strength and accuracy by 15% when fighting the undead."
            this.values[BROAD_ARROWS_4160] = "Arrows that can pierce the hides of creatures such as Turoth and Kurasks. Levels 55 Slayer and 50 Ranged required, and a magic bow or better."
            this.values[HOLY_WRENCH] = "A shining paragon of wrenchly virtue."
            this.values[DARKLIGHT] = "The magical sword 'Silverlight', enhanced with the blood of Agrith-Naar."
            this.values[FIGHTER_TORSO] = "A torso worn by penance fighters. Requires level 40 Defence."
            this.values[KERIS] = "A sharp mystical dagger that can penetrate through Kalphite chitin."
            this.values[TARNS_DIARY] = "Tarn Razorlor's diary, used for enchanting salve amulets."
            this.values[BARRELCHEST_ANCHOR] = "An anchor used by the Barrelchest. Requires level 60 Attack & 40 Strength."
            this.values[DRAGON_DART] = "A deadly throwing dart with a dragon tip."
            this.values[HERB_BOX] = "A herb box containing an assortment of random herbs, giving ten herbs per box."
            this.values[SLAYER_RING_8] = "A wieldable ring that can check your task progress. It has 8 charges for teleporting to useful Slayer sites."
            this.values[BROAD_BOLTS] = "Crossbow bolts that can pierce the hides of creatures such as Turoth and Kurasks. Levels 55 Slayer and 61 Ranged required, and a runite crossbow or better."
            this.values[RUNE_POUCH] = "The rune pouch has the ability to store up to 16,000 runes of 3 types."
            this.values[DWARF_CANNON_SET] = "A powerful ranging device that fires metal balls."
            this.values[BONECRUSHER] = "A crusher for the toughest of bones."
            this.values[HERB_SACK] = "The herb sack has the ability to store up to 30 of each major grimy herb. Requires 58 Herblore to use."
            this.values[SHAYZIEN_GLOVES_5] = "Some gloves from the Shayzien guards, used for protection against lizardmen."
            this.values[SHAYZIEN_BOOTS_5] = "Some boots from the Shayzien guards, used for protection against lizardmen."
            this.values[SHAYZIEN_HELM_5] = "A helmet from the Shayzien guards, used for protection against lizardmen."
            this.values[SHAYZIEN_GREAVES_5] = "Some greaves from the Shayzien guards, used for protection against lizardmen."
            this.values[SHAYZIEN_PLATEBODY_5] = "A platebody from the Shayzien guards, used for protection against lizardmen."
            this.values[BRACELET_OF_ETHEREUM_UNCHARGED] = "The bracelet is dull and powerless."
            this.values[IVANDIS_FLAIL] = "Used to fight Vampyres."
            this.values[VS_SHIELD_24266] = "A recreation of V's mighty shield."
            this.values[BLISTERWOOD_FLAIL] = "Super effective against Vampyres."
            this.values[ASH_SANCTIFIER] = "Scatters ashes."
            this.values[KERIS_PARTISAN] = "A mystical-feeling polearm that can easily penetrate through Kalphite chitin."
            this.values[26300] = "You can carry dragonhide in here."
            this.values[26304] = "A pouch for storing bones."
            this.values[26706] = "A scroll that can be used to imbue items."
            this.values[32161] = "A powerful defensive weapon."
            this.pack()
        }
    }
}
