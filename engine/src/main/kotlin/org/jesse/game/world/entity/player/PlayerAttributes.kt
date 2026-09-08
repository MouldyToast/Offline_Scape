package org.jesse.game.world.entity.player

import org.jesse.game.content.bountyhunter.BountyHunterWildernessRange
import org.jesse.game.util.Ticker
import org.jesse.game.item.Item
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.attribute
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.persistentAttribute
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.impl.Trade
import org.jesse.game.world.entity.player.container.impl.death.DeathMechanics
import org.jesse.game.world.entity.player.privilege.GameMode
import org.jesse.game.world.entity.player.`var`.VarCollection
import org.jesse.game.world.entity.weakReferenceAttribute


/**
 * The id of the discord user linked to this account.
 */
var Player.discordUserId by persistentAttribute<Long?>("discordUserId", null)
var Player.disableDiscordLinkRequests by persistentAttribute("disableDiscordLinkRequests", false)
var Player.killingBlowHit: Hit? by attribute("killingBlowHit", null)
var Player.selectedGameMode: GameMode by attribute("selected_game_mode", GameMode.REGULAR)
var Player.selectedGameModeDifficulty by persistentAttribute("selected_game_mode_difficulty", 0)

var Player.selectedUniversalShopCategory: Int by attribute("selected_universal_shop_category", 0)
var Player.univShopSearchActive: Boolean by attribute("univ_shop_search_active", false)
var Player.univShopDoubleProcess: Boolean by attribute("univShopDoubleProcess", false)

var Player.dailyChallengePoints: Int by persistentAttribute("dailyChallengePoints", 0)

var Player.flaggedAsBot: Boolean by persistentAttribute("flaggedAsBot", false)

var Player.slayerBaseDryStreak by persistentAttribute("slayer_base_dry_streak", 0)
var Player.slayerShaftDryStreak by persistentAttribute("slayer_shaft_dry_streak", 0)
var Player.slayerLeftBoneDryStreak by persistentAttribute("slayer_left_bone_dry_streak", 0)
var Player.slayerRightBoneDryStreak by persistentAttribute("slayer_right_bone_dry_streak", 0)
/* Bounty Hunter Start */

var Player.bountyHunterPoints: Int by persistentAttribute("bountyHunterPoints", 0)
var Player.bountyHunterCurrentWildernessRange: BountyHunterWildernessRange? by attribute("bountyHunterWildernessRange", null)
var Player.bountyAbandonedTicker: Ticker by attribute("bhTicker", Ticker(100, active = false, resetAutomatically = true, defaultsToInactive = true))
var Player.bountyEarningPotentialTicker: Ticker by attribute("bh_ep_ticker", Ticker(50, active = true, resetAutomatically = true, defaultsToInactive = true))
var Player.bountyEarningPotentialDeathTicker: Ticker by attribute("bh_ep_death_ticker", Ticker(1500, active = true, resetAutomatically = false, defaultsToInactive = true))
var Player.bountyHunterInfoDisplay: Int by persistentAttribute("bountyHunterInfoDisplayIdx", 0)
var Player.bountyHunterInterfaceRateLimit: Int by attribute("bountyHunterRateLimit", 0)
var Player.bountyHunterInfoCooldown: Int by attribute("bountyHunterInfoCooldown", 0)
var Player.bountyHunterKills: Int by persistentAttribute("bountyHunterKills", 0)
var Player.bountyHunterDeaths: Int by persistentAttribute("bountyHunterDeaths", 0)
var Player.bountyHunterKillstreak: Int by persistentAttribute("bountyHunterKillstreak", 0)
var Player.bountyHunterSkipCount: Int by persistentAttribute("bountyHunterSkipCount", 0)
var Player.bountyHunterLastTarget: String by persistentAttribute("bountyHunterLastTarget", "")
var Player.bountyTargetLevelRange : Int by persistentAttribute("bounty_hunter_target_range", 5)
var Player.bountyHunterEarningPotential: Int by persistentAttribute("bounty_hunter_earning_potential", 0)
var Player.bountyHunterEarningPotentialDeathEarned: Int by persistentAttribute("bounty_hunter_earning_potential_death_modifier", 0)

/* Bounty Hunter End */

/* Scar Essence Mine */
var Player.scarEssenceMineCoffer: Int by persistentAttribute("scarEssenceMineCoffer", 0)
var Player.totalWrathToHagus: Int by persistentAttribute("totalWrathToHagus", 0)
var Player.depositedTaintedEssenceChunks: Int by persistentAttribute("depositedTaintedEssenceChunks", 0)

/* Scar Essence Mine End */


var Player.sacrificedTwistedBow: Boolean by persistentAttribute("sacrificedTwistedBow", false)
var Player.sacrificedScytheOfVitur: Boolean by persistentAttribute("sacrificedScytheOfVitur", false)
var Player.sacrificedTumekensShadow: Boolean by persistentAttribute("sacrificedTumekensShadow", false)

var Player.manuallyLeftHelpChat: Boolean by persistentAttribute("manuallyLeftHelpChat", false)

var Player.toaPetAkkhito: Boolean by persistentAttribute("toa-pet-akkha", false)
var Player.toaPetBabi: Boolean by persistentAttribute("toa-pet-baba", false)
var Player.toaPetKephriti: Boolean by persistentAttribute("toa-pet-kephri", false)
var Player.toaPetZebo: Boolean by persistentAttribute("toa-pet-zebak", false)
var Player.toaPetRemnant: Boolean by persistentAttribute("toa-pet-remnant", false)
var Player.dailyRemainingTomes: Int by persistentAttribute("exchange-daily-tomes-remaining", 0)
var Player.migrationVersion: Int by persistentAttribute("nr-migration-version", 0)

var Player.nidRaxMetamorphUnlocked: Boolean by persistentAttribute("nid_rax_metamorph", false)
var Player.tobPetMetamorphUnlocked: Boolean by persistentAttribute("tob_pet_metamorph", false)
var Player.nightmareMetamorphosisUnlocked: Boolean by persistentAttribute("nightmare_pet_metamorph", false)

var Player.revCaveEntryFee by persistentAttribute("rev_cave_entry_fee", 0)

var Player.tormentedDemonAccuracyBoost: Boolean by persistentAttribute("tormentedDemonAccuracyBoost", false)

var Player.shootingStarsMined: Int by persistentAttribute("shooting_stars_mined", 0)
var Player.claimedFreeMB: Boolean by persistentAttribute("claimed_referral_mb", false)
var Player.totalDonatedAfterLaunch: Int by persistentAttribute("total_donated_after_launch", 0)

/**
 * TODO: bad design, future improvements to plugin system are needed.
 */
var Player.ironGroupTradeAddItemCheck: (Trade.(Item) -> Boolean)? by attribute(
    "ironGroupTradeAddItemCheck",
    null
)

/**
 * TODO: bad design, future improvements to plugin system are needed.
 */

var Player.hardcoreIronGroupDeathHandlingOverride: (DeathMechanics.(Player, Entity?) -> Unit)? by attribute(
    "hardcoreIronGroupDeathHandlingOverride",
    null
)

var Player.ironGroupMessageHandler: ((message: String, name: String) -> Unit)? by attribute(
    "ironGroupMessageHandler",
    null
)

var Player.boneCrusherNecklaceActivationTime : Long by attribute("boneCrusherNecklaceActivationTime", 0L)

var Player.freezeCaster: Entity? by weakReferenceAttribute("freezeCaster")
var Player.pvmArenaPoints: Long by persistentAttribute("pvmArenaPoints", 0L)
var Player.pvmArenaPointsGainedDuringGame: Long by attribute("pvmArenaPointsGainedInMatch", 0L)
var Player.pvmArenaMvpCountDuringGame: Long by attribute("pvmArenaMvpCountInMatch", 0L)

/**
 * Used to display a blue or red icon beneath the player, which is an item shown in the beard slot.
 */
var Player.pvmArenaAppearanceBeardOffset: Int by attribute("pvmArenaAppearanceBeardOffset", 0)

var Player.pvmArenaRevivalCount: Int by attribute("pvmArenaRevivalCount", 0)

var Player.pvmArenaInRevivalState: Boolean by attribute("pvmArenaInRevivalState", false)

var Player.pvpKills : Int by persistentAttribute("pvp-kills", 0)
var Player.pvpDeaths : Int by persistentAttribute("pvp-deaths", 0)
var Player.pvpKillStreak: Int by persistentAttribute("current-killstreak", 0)

var Player.wildernessResourceAreaPaidFeeAmount: Int by attribute("wildernessResourceAreaPaidFeeAmount", 0)

var Player.blackSkulled : Boolean by persistentAttribute("blackSkulled", false)
var Player.sanityValue by attribute("dt2_whispy_sanity", 100)

var Player.hasBreachesHintArrow: Boolean by persistentAttribute("hasBreachesHintArrow", false)

var Player.firstElderMaulSpecOnTekton: Boolean by persistentAttribute("firstElderMaulSpecOnTekton", true)

var Player.pinProtectedItemValue: Int by persistentAttribute("pinProtectedItemValue", 50_000)

var Player.echoAxeBurningLogs: Boolean by persistentAttribute("echoAxeBurningLogs", true)
var Player.echoAxeBanking: Boolean by persistentAttribute("echoAxeBanking", false)

var Player.echoPickaxeSmelting: Boolean by persistentAttribute("echoPickaxeSmelting", true)
var Player.echoPickaxeBanking: Boolean by persistentAttribute("echoPickaxeBanking", false)

var Player.echoHarpoonCookingFish: Boolean by persistentAttribute("echoHarpoonCookingFish", true)
var Player.echoHarpoonBanking: Boolean by persistentAttribute("echoHarpoonBanking", false)

var Player.echoBootsActive by persistentAttribute("echo_boots_active", true)

var Player.dizanasQuiverAmmo by persistentAttribute("dizanas_quiver_ammo", -1)
var Player.dizanasQuiverAmmoAmount by persistentAttribute("dizanas_quiver_ammo_amount", 0)

fun Player.setDizanasQuiver(ammoId: Int, amount: Int) {
    this.dizanasQuiverAmmo = ammoId
    this.dizanasQuiverAmmoAmount = amount
    VarCollection.DIZANAS_QUIVER_AMMO.updateSingle(this)
    VarCollection.DIZANAS_QUIVER_AMMO_AMOUNT.updateSingle(this)
}

var Player.dailyMysteryBox by persistentAttribute("daily-mystery-box-nezicheneds", false)
var Player.extraDailyMysteryBox by persistentAttribute("daily-extra-mystery-box-nezicheneds", false)

var Player.storeTab : Int by attribute("store_tab", 0)
var Player.storeCategory : Int by attribute("store_category", 0)
var Player.storeLoyaltyRewardsClaimed : Int by persistentAttribute("store_loyalty_rewards_claimed", 0)
