package org.jesse.game.content.toa

import org.jesse.game.content.tombsofamascut.InvocationCategoryType
import org.jesse.game.content.tombsofamascut.raid.TOARaidParty
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.util.Utils
import org.jesse.game.world.World
import org.jesse.game.world.entity.player.Player
import kotlin.math.min

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.31.2025
 */
object TOARewardHelper {

    var rewardMultiplier: Double = 100.0

    private fun TOARaidParty.getWinner() : Player? {
        val players = this.players
        val pointMap = players.map { it to it.toaManager.currentPoints }
        val totalPoints = pointMap.sumOf { it.second }
        val randomValue = totalPoints.random()

        var cumulativePoints = 0
        for ((player, points) in pointMap) {
            cumulativePoints += points
            if (randomValue < cumulativePoints) {
                return player
            }
        }
        return players.randomOrNull()
    }

    /* Entry point from existing logic */
    @JvmStatic fun rollGroupLoot(rrm: TOARewardRoomManager, party: TOARaidParty) {
        if(party.players.size == 1) {
            rewardWinner(party.players.first(), party, party.completedRaidLevel, rrm)
        } else {
            val winner = party.getWinner() ?: return
            val others = party.players.filterNot { it == winner }
            rewardWinner(winner, party, party.completedRaidLevel, rrm)
            others.forEach {
                rewardStandard(it, party, rrm)
            }
        }
    }

    private fun rewardStandard(player: Player, party: TOARaidParty, rrm: TOARewardRoomManager) {
        val points = player.toaManager.currentPoints
        val rolls = rolls()
        val loots = commonLoots.shuffled().take(rolls)
        val rewards = loots.map {
            val amount = (points / it.divisor).coerceAtLeast(1)
            Item(it.id, amount)
        }.toMutableList()
        rewardTertiary(player, rewards, party)
        rewardExtras(player, rewards)
        rrm.assignNormalLoot(player, rewards)
    }

    private fun rewardWinner(winner: Player, party: TOARaidParty, level: Int, rrm: TOARewardRoomManager) {
        val rolledLevel = level
        val clamp = 550
        val levelClamped = rolledLevel.coerceAtMost(clamp)
        if(winner.getBooleanTemporaryAttribute("overridePurple") || rolledUnique(levelClamped, winner.toaManager.currentPoints)) {
            rewardUniqueAndTertiary(winner, party, levelClamped, rrm)
            return
        }
        rewardStandard(winner, party, rrm)
    }

    private fun rewardUniqueAndTertiary(winner: Player, party: TOARaidParty, level: Int, rrm: TOARewardRoomManager) {
        val reward = TOAUniqueReward.random()
        winner.sendDeveloperMessage("TOA Rewards - Rewarding Unique! - Rolled -> ${reward?.item?.let { Item(it).name } ?: "null!"} at lvl: $level")
        if (reward != null && reward.hasLevel(level)) {
            winner.sendDeveloperMessage("TOA Rewards - Passed reward check!")
            rrm.purple = reward
            val rewardItem = Item(reward.item, 1)
            val rewards = mutableListOf(rewardItem)
            rewardTertiary(winner, rewards, party)
            rrm.assignPurpleLoot(winner, rewards)
            return
        }
        rewardStandard(winner, party, rrm)
    }

    private fun rewardTertiary(player: Player, rewards: MutableList<Item>, party: TOARaidParty) {
        val points = player.toaManager.currentPoints
        if(points.isShit()) { rewards.rewardShit(); return }
        if(!party.eligibleForTertiaryRewards()) return

        val team = player and party
        if(team.eligibleForMenaphiteOK()) rewards.rewardMenaphiteKit()
        if(team.eligibleForMasoriCK()) rewards.rewardMasoriKit()
        if(team.eligibleForAkkhaRemnant()) rewards.rewardAkkhaRemnant()
        if(team.eligibleForZebakRemnant()) rewards.rewardZebakRemnant()
        if(team.eligibleForBaBaRemnant()) rewards.rewardBabaRemnant()
        if(team.eligibleForKephriRemnant()) rewards.rewardKephriRemnant()
        if(team.eligibleForAncientRemnant()) rewards.rewardAncientRemnant()
        if(team.eligibleForCursedPhalanx()) rewards.rewardCursedPhalanx()
    }

    private fun rewardExtras(player: Player, rewards: MutableList<Item>) {
        if(rewards.hasMoreRoom() && 15.rollOneIn()) rewards.rewardThreadOfElidinis()
        if(rewards.hasMoreRoom() && 20.rollOneIn()) rewards.rewardRandomJewel()
        if(rewards.hasMoreRoom() && 5.rollOneIn()) rewards.rewardClueScroll()
    }


    private infix fun Player.and(party: TOARaidParty) = this to party
    private fun Int.isShit() = this < 1500

    private val shit = Item(FOSSILISED_DUNG)
    private val akkhaRemnant = Item(REMNANT_OF_AKKHA)
    private val zebakRemnant = Item(REMNANT_OF_ZEBAK)
    private val babaRemnant = Item(REMNANT_OF_BABA)
    private val kephriRemnant = Item(REMNANT_OF_KEPHRI)
    private val ancientRemnant = Item(ANCIENT_REMNANT)
    private val masoriKit = Item(MASORI_CRAFTING_KIT)
    private val menaphiteKit = Item(MENAPHITE_ORNAMENT_KIT)
    private val cursedPhalanx = Item(CURSED_PHALANX)
    private val threadOfElidinis = Item(THREAD_OF_ELIDINIS)
    private val eliteClueScroll = Item(SCROLL_BOX_ELITE)

    private val jewels = listOf(
        Item(BREACH_OF_THE_SCARAB),
        Item(JEWEL_OF_THE_SUN),
        Item(EYE_OF_THE_CORRUPTOR)
    )

    private fun MutableList<Item>.rewardShit() = this.add(shit)
    private fun MutableList<Item>.rewardAkkhaRemnant() = this.add(akkhaRemnant)
    private fun MutableList<Item>.rewardZebakRemnant() = this.add(zebakRemnant)
    private fun MutableList<Item>.rewardBabaRemnant() = this.add(babaRemnant)
    private fun MutableList<Item>.rewardKephriRemnant() = this.add(kephriRemnant)
    private fun MutableList<Item>.rewardAncientRemnant() = this.add(ancientRemnant)
    private fun MutableList<Item>.rewardMenaphiteKit() = this.add(menaphiteKit)
    private fun MutableList<Item>.rewardMasoriKit() = this.add(masoriKit)
    private fun MutableList<Item>.rewardCursedPhalanx() = this.add(cursedPhalanx)
    private fun MutableList<Item>.rewardThreadOfElidinis() = this.add(threadOfElidinis)
    private fun MutableList<Item>.rewardRandomJewel() = this.add(jewels.random())
    private fun MutableList<Item>.rewardClueScroll() = this.add(eliteClueScroll)

    private fun MutableList<Item>.hasMoreRoom() = this.size < 6
    private fun rolls() = 3
    private fun TOARaidParty.eligibleForTertiaryRewards() = this.totalDeaths == 0
    private fun Pair<Player, TOARaidParty>.eligibleForMasoriCK() = second.completedRaidLevel >= 350 && !first.containsAny(MASORI_CRAFTING_KIT)
    private fun Pair<Player, TOARaidParty>.eligibleForMenaphiteOK() = second.completedRaidLevel >= 400 && !first.containsAny(MENAPHITE_ORNAMENT_KIT)
    private fun Pair<Player, TOARaidParty>.eligibleForAkkhaRemnant() = second.completedRaidLevel >= 450 && second.partySettings.allActive(InvocationCategoryType.AKKHA) && !first.containsAny(REMNANT_OF_AKKHA)
    private fun Pair<Player, TOARaidParty>.eligibleForZebakRemnant() = second.completedRaidLevel >= 450 && second.partySettings.allActive(InvocationCategoryType.ZEBAK) && !first.containsAny(REMNANT_OF_ZEBAK)
    private fun Pair<Player, TOARaidParty>.eligibleForBaBaRemnant() = second.completedRaidLevel >= 450 && second.partySettings.allActive(InvocationCategoryType.BA_BA) && !first.containsAny(REMNANT_OF_BABA)
    private fun Pair<Player, TOARaidParty>.eligibleForKephriRemnant() = second.completedRaidLevel >= 450 && second.partySettings.allActive(InvocationCategoryType.KEPHRI) && !first.containsAny(REMNANT_OF_KEPHRI)
    private fun Pair<Player, TOARaidParty>.eligibleForAncientRemnant() = second.completedRaidLevel >= 450 && second.partySettings.allActive(InvocationCategoryType.THE_WARDENS) && !first.containsAny(ANCIENT_REMNANT)
    private fun Pair<Player, TOARaidParty>.eligibleForCursedPhalanx() = second.completedRaidLevel >= 500

    private fun getPointsPerUniqueChance(raidLevel: Int): Int {
        val y = if (raidLevel > 400) raidLevel - 400 else 0
        val x = raidLevel - y
        return 10500 - 20 * (x + (y / 3))
    }

    private fun rolledUnique(raidLevel: Int, points: Int): Boolean {
        val pointsPerChance = getPointsPerUniqueChance(raidLevel)
        val odds = min((points.toDouble() / pointsPerChance) / 100.0, 0.55)
        return Utils.getRandom().nextDouble() < odds
    }

    private fun Int.random(): Int = (Math.random() * (this + 1)).toInt()

    private val commonLoots = buildList {
        add(TOARewardPair(COINS_995, 1))
        add(TOARewardPair(DEATH_RUNE, 15))
        add(TOARewardPair(SOUL_RUNE, 30))
        add(TOARewardPair(BLOOD_RUNE, 40))
        add(TOARewardPair(WRATH_RUNE, 50))
        add(TOARewardPair(GOLD_ORE, 90))
        add(TOARewardPair(DRAGON_DART_TIP, 100))
        add(TOARewardPair(UNCUT_SAPPHIRE, 200))
        add(TOARewardPair(UNCUT_EMERALD, 215))
        add(TOARewardPair(GOLD_BAR, 200))
        add(TOARewardPair(POTATO_CACTUS, 250))
        add(TOARewardPair(RAW_SHARK, 250))
        add(TOARewardPair(UNCUT_RUBY, 300))
        add(TOARewardPair(UNCUT_DIAMOND, 400))
        add(TOARewardPair(RAW_MANTA_RAY, 450))
        add(TOARewardPair(CACTUS_SPINE, 600))
        add(TOARewardPair(UNCUT_DRAGONSTONE, 600))
        add(TOARewardPair(BATTLESTAFF, 1100))
        add(TOARewardPair(COCONUT_MILK, 1100))
        add(TOARewardPair(LILY_OF_THE_SANDS, 1100))
        add(TOARewardPair(TOADFLAX_SEED, 1400))
        add(TOARewardPair(RANARR_SEED, 1500))
        add(TOARewardPair(TORSTOL_SEED, 1500))
        add(TOARewardPair(SNAPDRAGON_SEED, 1500))
        add(TOARewardPair(DRAGON_MED_HELM, 4000))
        add(TOARewardPair(MAGIC_SEED, 3500))
        add(TOARewardPair(BLOOD_ESSENCE, 7500))
        add(TOARewardPair(CRYSTAL_KEY, 8000))
    }

    fun Int.rollOneIn() = Utils.random(this) == 0
}