package com.near_reality.game.content

import com.zenyte.game.item._Item

object CollectionLogRewards {


    val rewards = mutableListOf<CollectionLogReward>()

    init {
        rewards.addAll(
            arrayOf(
                // Abyssal Sire: 5 unsired (13273), Pet Booster: 3 (32152), Ulti M Box: 1 (32165)
                CollectionLogReward(476, arrayOf(_Item(13273, 5), _Item(32152, 3), _Item(32165, 1))),

                // Alchemical Hydra: 50m GP, 3x pet booster, 10x slayer task picker scrolls, 1x super mystery box (995, 32152, 32157, 32164)
                CollectionLogReward(539, arrayOf(gen(995, 50.toM()), gen(32152, 3), gen(32157, 10), gen(32164, 1))),

                // Araxxor: 50m, 3x pet boosters, 2 mystery boxes (995, 32152, 32165)
                CollectionLogReward(10503, arrayOf(gen(995, 50.toM()), gen(32152, 3), gen(32165, 2))),

                // Barrows: 30m GP, 25x Barrows Totems, Malevolent Energy x250, x4 Ahrim's Ornament Kits (995, 32168, 32185, 30451)
                CollectionLogReward(477, arrayOf(gen(995, 30.toM()), gen(32168, 10), gen(32185, 250), gen(30451, 4))),

                // Bryophyta: 5m, 25x Herb Boxes  (995) / (11738)
                CollectionLogReward(478, arrayOf(gen(995, 5.toM()), gen(11738, 25))),

                // Callisto & Artio: 25m, 5x blood money booster (32154), 5x larrans booster (32149), 1500x remnant points (32363)
                CollectionLogReward(479, arrayOf(gen(995, 25.toM()), gen(32154, 5), gen(32149, 5), gen(32363, 1500))),

            // Cerberus: 30m GP, 10x slayer task picker scrolls, Cerberus Echo Orb (995, 32157, 30393)
            CollectionLogReward(480, arrayOf(gen(995, 30.toM()), gen(32157, 10), gen(30393, 1))),

            // Chaos Elemental: 10m GP (995) , 500 rem vouchers        (32363)    , larrans booster 3x (32149)
            CollectionLogReward(481, arrayOf(gen(995, 10.toM()), gen(32363, 500), gen(32149, 3))),

            // Chaos Fanatic: 2.5m GP (995) / 500 rem vouchers   (32363)
            CollectionLogReward(482, arrayOf(gen(995, 5.toM()), gen(32363, 500))),

            // Commander Zilyana: 40m GP, 2x super mystery box (995, 32164)
            CollectionLogReward(483, arrayOf(gen(995, 40.toM()), gen(32164, 2))),

            // Corporeal Beast: 40m (995), 2x ultimate box, $25 bond (6199, 32231, 32071)
            CollectionLogReward(484, arrayOf(gen(995, 40.toM()), gen(32231, 1), gen(32071, 1))),

            // Crazy Archaeologist: 5m GP (995)    , voucher 500x - 32363
            CollectionLogReward(485, arrayOf(gen(995, 5.toM()), gen(32363, 500))),

            // Dagannoth Kings: 15m (995), imbue scroll (26706)    2x mbox (6199)
            CollectionLogReward(486, arrayOf(gen(995, 15.toM()), gen(26706, 1), gen(6199, 2))),

            // Duke Sucellus:              30m gp 995 - 1x chrom ignot -    28276 , 1x super mbox 32164, 3x echo virtus orn kit (30443)
            CollectionLogReward(10500, arrayOf(gen(995, 30.toM()), gen(28276, 1), gen(32164, 1), gen(30443, 3))),

            // Vardorvis:                      50m 995 - 1x chrom ignot - 28276, 1x ult mbox 32164, 3x echo virtus orn kit (30443)
            CollectionLogReward(10501, arrayOf(gen(995, 50.toM()), gen(28276, 1), gen(32165, 1), gen(30443, 3))),

            // Tormented Demons:                     15m 995 , 3x mbox
            CollectionLogReward(10502, arrayOf(gen(995, 15.toM()), gen(6199, 3))),

            // Fight Caves (move to minigames): 2.5m gp, 50k Tokkul, 1x fire cape (6529, 6570)
            CollectionLogReward(500, arrayOf(gen(995, 2_500_000), gen(6529, 50_000), gen(6570, 1))),

            // The Gauntlet (move to minigames): ult mbox (32165), 3x armour seed (23956), 50 E ckeys  (23951), tir echo orb (30398)
            CollectionLogReward(605, arrayOf(gen(32165, 1), gen(23956, 3), gen(23951, 50), gen(30398, 1))),

            // Ganodermic Beast: $50 bond (32072), 5k blood money 13307, ultimate mystery box (32165), 5x ganodermic booster (13190, 32165, 32150)
            CollectionLogReward(10300, arrayOf(gen(32072, 1), gen(13307, 5_000), gen(32165, 1), gen(32150, 5))),

            // General Graardor: 20m GP, 2x super mystery box (995, 32164)
            CollectionLogReward(487, arrayOf(gen(995, 20.toM()), gen(32164, 2))),

            // Giant Mole: 1000x toadflax (unf) (3003), 1000x crushed birdnest (6694), 2x pet boosters (32152)
//            CollectionLogReward(488, arrayOf(gen(3003, 500), gen(6694, 500), gen(32152, 2))),

            // Grotesque Guardians: double cannonball mold (27012), 5x slayer boosters, 5x slayer task picker scrolls, mory echo orb (32151, 32157, 30397)
            CollectionLogReward(489, arrayOf(gen(27012, 1), gen(32151, 5), gen(32157, 5), gen(30397, 1))),

            // Hespori: 100x clean snapdragon, 100x clean torstol, queens secs, kourend echo orb (2999, 3001, 270, 22875, 32200, 7410)
            CollectionLogReward(541, arrayOf(gen(3001, 100), gen(270, 100), gen(30400, 1), gen(7410, 1))),

            // The Inferno (move to minigames): 100k tokkul, 1x infernal cape (6529, 21295)
            CollectionLogReward(499, arrayOf(gen(6529, 250_000), gen(21295, 1))),

            // Kalphite Queen: 10m GP, 2x Mystery Box, Desert echo orb (995, 6199, 30394)
            CollectionLogReward(490, arrayOf(gen(995, 10.toM()), gen(6199, 2), gen(30394, 1))),

            // King Black Dragon: 10m gp, 3x Pet Booster (32152), 3x larrans booster (32149), wilderness echo orb (30399)
            CollectionLogReward(491, arrayOf(gen(995, 10.toM()), gen(32152, 1), gen(32149, 3), gen(30399, 1))),

            // Kraken: 10m gp, 5x slayer task picker scroll, 5x slayer task reset scroll, 1x mystery box (32157, 32158)
            CollectionLogReward(492, arrayOf(gen(995, 10.toM()), gen(32157, 5), gen(32158, 5), gen(6199, 1))),

            // Kree'arra: 40m GP, 3x Pet Booster (32152), 2000 rem pts (995, 32363) 2x sup mbox
            CollectionLogReward(493, arrayOf(gen(995, 40.toM()), gen(32363, 2000), gen(32164, 2))),

            // K'ril Tsutsaroth: 15m GP, 1k rem pts (995, 6199, 32363), 1x mbox
            CollectionLogReward(494, arrayOf(gen(995, 15.toM()), gen(32164, 1), gen(32363, 1000))),

            // Nex: 10x nex booster, pet booster x3, $50 dpin, bandos comp x3, arma plate x3 (32167, 32152, 32072, 26394, 27269)
            CollectionLogReward(3769, arrayOf(gen(32167, 10), gen(32072, 1), gen(26394, 3), gen(27269, 3))),

            // The Nightmare: 1x regal box, 3x pet booster, $10 bond (32231, 32152, 30051)
            CollectionLogReward(1263, arrayOf(gen(32231, 3), gen(32152, 3), gen(32070, 1))),

            // Obor: 5m gp, 5x slayer task reset (995, 32157)
            CollectionLogReward(495, arrayOf(gen(995, 5_000_000), gen(20754, 5))),

            // Phantom Muspah: 15m cash, 1x venator shard, 10x frozen cache, venator bow orn kit (995, 27614, 27622, 30432)
            CollectionLogReward(4455, arrayOf(gen(995, 5.toM()), gen(27614, 1), gen(27622, 10), gen(30432, 1))),

            // Rise of the Six: 50m cash, 1x regal mbox (995, 32231, 32072)
            CollectionLogReward(10321, arrayOf(gen(995, 50.toM()), gen(32231, 1), gen(32072, 1))),

            // Sarachnis: 5m gp, 1x Mystery Box (995, 32363)
            CollectionLogReward(601, arrayOf(gen(995, 10.toM()), gen(32363, 750))),

            // Scorpia: 10m GP, 2x bm booster, 2x larran booster (995, 32154, 32149)
            CollectionLogReward(496, arrayOf(gen(995, 10.toM()), gen(32154, 2), gen(32149, 2))),

            // Skotizo: 5m GP, 5x dark totems, ancient shard pack (995, 19685, 31300)
            CollectionLogReward(497, arrayOf(gen(995, 5.toM()), gen(19685, 5), gen(31300, 1))),

            // Thermonuclear Smoke Devil: 5m gp, 1x Mystery Box, 10x slayer task picker (995, 6199, 32157)
            CollectionLogReward(498, arrayOf(gen(995, 5.toM()), gen(6199, 1), gen(32157, 10))),

            // Vanstrom Klause: 15m GP (995), 1x blood shard
            CollectionLogReward(10322, arrayOf(gen(995, 15.toM()), gen(24777, 1))),

            // Venenatis and Spindel: 10m GP, 3x bm boosters, 3x larran booster, 1k remnant (995, 32154, 32149, 32363)
            CollectionLogReward(501, arrayOf(gen(995, 10.toM()), gen(32154, 3), gen(32149, 3))),

            // Vet'ion and Calvar'ion: 10m GP, 3x bm boosters, 3x larran booster, 1k remnant (995, 32154, 32149, 32363)
            CollectionLogReward(502, arrayOf(gen(995, 10.toM()), gen(32154, 3), gen(32149, 3))),

            // Vorkath: 5m GP, 250x superior bones, 1k remnant vouchers (995, 22124, 32363)
            CollectionLogReward(503, arrayOf(gen(995, 25.toM()), gen(22124, 250), gen(32363, 1000))),

            // Wintertodt (move to minigames): 10m GP, 5x supply crates (995, 20703)
            CollectionLogReward(504, arrayOf(gen(995, 10.toM()), gen(20703, 25))),

            // Zalcano: 5x skilling mbox, 1x crystal tool seed (32212, 23953)
            CollectionLogReward(604, arrayOf(gen(32212, 5), gen(23953, 1))),

            // Zulrah: 25m gp, 25k scales, 1x magma ornament kit (995, 12934, 28690)
            CollectionLogReward(505, arrayOf(gen(995, 25.toM()), gen(12934, 25_000), gen(28690, 1))),

            // RAIDS
            // Chambers of Xeric: $100 scroll, 20x xeric's wisdoms, 10 cox cores, omega spike (32073, 32231, 32369, 32609)
            CollectionLogReward(507, arrayOf(gen(32073, 1), gen(32369, 20), gen(32609, 1))),
            // Theater of Blood: $100 scroll, 1x regal mystery box, 25x ToB booster, 10 tob cores, omega horn (32073, 32156, 32231, 32370, 32608)
            CollectionLogReward(506, arrayOf(gen(32073, 1), gen(32156, 25), gen(32370, 10), gen(32608, 1))),
            // Tombs of Amascut: $100 scroll, 1x regal mystery box, 5x world tokens, 1x omega symbol (32073, 32231, 32424, 32610)
            CollectionLogReward(4378, arrayOf(gen(32073, 1), gen(32231, 1), gen(32424, 5), gen(32610, 1))),

            // CLUES
            // Beginner Treasure Trails: 5m GP, one 10-pack notes (995, 30210)
            CollectionLogReward(593, arrayOf(gen(995, 5.toM()), gen(30210, 1))),
            // Easy Treasure Trails: 10m GP two ten-pack notes (995, 30210)
            CollectionLogReward(508, arrayOf(gen(995, 10.toM()), gen(30210, 2))),
            // Medium Treasure Trails: 25m GP, 5x clue booster, two 10 pack (995, 32155, )
            CollectionLogReward(509, arrayOf(gen(995, 25.toM()), gen(32155, 5), gen(30210, 2))),
            // Hard Treasure Trails: 50m GP, 1x clue booster three 10 pack (995, 32155)
            CollectionLogReward(510, arrayOf(gen(995, 50.toM()), gen(32155, 5), gen(30210, 3))),
            // Elite Treasure Trails: 100m GP, $50 dpin 10x clue booster (995, 32155)
            CollectionLogReward(511, arrayOf(gen(995, 100.toM()), gen(32071, 1), gen(32155, 10), gen(30210, 3))),
            // Master Treasure Trails: 250m gp, $100 dpin 1x clue booster (995, 32155, 30210)
            CollectionLogReward(512, arrayOf(gen(995, 250.toM()), gen(32073, 1), gen(32155, 15), gen(30210, 5))),
            // Hard Treasure Trails (Rare):250m gp, 10x clue booster (995, 32155)
            CollectionLogReward(2869, arrayOf(gen(995, 250.toM()), gen(32155, 10))),
            // Elite Treasure Trails (Rare): 500m gp, 10x clue booster, 200 sherlock (995, 32155, 32136)
            CollectionLogReward(2870, arrayOf(gen(995, 500.toM()), gen(32155, 10), gen(32136, 2))),
            // Master Treasure Trails (Rare): 1B gp, 10x clue booster, 300 sherlock (995, 32155, 32136)
            CollectionLogReward(2871, arrayOf(gen(995, 1000.toM()), gen(32155, 10), gen(32136, 3))),

            // MINIGAMES
            // Pest Control: 10m GP (995), 5x crystal key
            CollectionLogReward(518, arrayOf(gen(995, 10_000_000))),
            // Rogues Den: 5m GP (995), 3x skiling mbox 32212
            CollectionLogReward(522, arrayOf(gen(32212, 1))),

        // OTHER
        // Aerial Fishing: 5m cash, 1x mystery box (995, 6199)
        CollectionLogReward(540, arrayOf(gen(995, 5.toM()), gen(6199, 1))),

        // All Pets: 5x $100 bond (32073), 2.147b gp (995), 5x regal (32231), 1x NR phat (32078)
        CollectionLogReward(535, arrayOf(gen(32073, 5), gen(995, 2_147_483_647), gen(32231, 5), gen(32078, 1))),

        // Chaos Druids: 5x larrans key booster, elder chaos druid ornament kit x3 (32149, 27113)
        CollectionLogReward(533, arrayOf(gen(32149, 5), gen(27113, 3))),

        // Cyclopes: 5m gp (995), dragon defender ornament kit (20143)
        CollectionLogReward(532, arrayOf(gen(995, 5_000_000), gen(20143, 1))),

        // Glough's Experiments: 1x zenyte, 1x onyx, heavy ballista ornament kit (19529, 6571, 26711)
        CollectionLogReward(526, arrayOf(gen(19529, 1), gen(6571, 1), gen(26711, 1))),

        // Motherlode Mine: dragon pickaxe ornament kit, 10x skilling mbox (12800, 32212)
        CollectionLogReward(530, arrayOf(gen(12800, 1), gen(32212, 10))),

        // Revenants: 15k blood money, 3x pvm mbox, 10x revenant booster (995, 32203, 32166)
        CollectionLogReward(525, arrayOf(gen(13307, 15_000), gen(32203, 1), gen(32166, 10))),

        // Rooftop Agility: 15x graceful dye (2710)
        CollectionLogReward(547, arrayOf(gen(2710, 15))),

        // Shooting Stars: 5k stardust (25527)
        CollectionLogReward(2858, arrayOf(gen(25527, 5_000))),

        // Skilling Pets: 100m GP, $50 dpin, 25x skilling mbox, 10x tome of experience (995, 32072, 32212, 30215)
        CollectionLogReward(529, arrayOf(gen(995, 100.toM()), gen(32072, 1), gen(32212, 25), gen(30215, 5))),

        // Slayer: 25x slayer task picker scrolls, 25x slayer task skip scrolls, 50x superior calls, 25k remnant (32157, 32158, 32611, 32363)
        CollectionLogReward(527, arrayOf(gen(32157, 25), gen(32158, 25), gen(32611, 50), gen(32363, 25_000))),

        // Tzhaar: 5m GP, 1x onyx, 5x crystal keys (995, 6571, 989)
        CollectionLogReward(536, arrayOf(gen(995, 5.toM()), gen(6571, 1), gen(989, 5))),

        // Miscellaneous: 3x mystery box, 3x super mbox, 3x ultimate, 3x regal (6199, 32164, 32165, 32231)
        CollectionLogReward(534, arrayOf(gen(6199, 3), gen(32164, 1), gen(32165, 1), gen(32231, 3)))
        ))
    }

    fun gen(id: Int, q: Int) = _Item(id, q)

    @JvmStatic
    fun getRewardSet(struct: Int): CollectionLogRewardSet {
        return rewards.find { it.struct == struct }?.toSet() ?: CollectionLogRewardSet()
    }
}

private fun Int.toM(): Int {
    return this * 1_000_000

}
