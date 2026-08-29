package com.near_reality.game.content.remnantpets

import com.near_reality.game.content.remnantpets.RemnantPerk.*
import com.near_reality.game.content.remnantpets.RemnantPet.*
import com.near_reality.game.item.CustomItemId
import com.zenyte.game.item.Item
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Utils
import com.zenyte.game.world.broadcasts.BroadcastType
import com.zenyte.game.world.broadcasts.WorldBroadcasts
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.persistentAttribute
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.region.area.plugins.PrayerPlugin
import com.zenyte.game.world.region.area.wilderness.WildernessArea
import mgi.types.config.npcs.NPCDefinitions
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.reflect.KClass

fun Player.inWildy() = WildernessArea.isWithinWilderness(this)
fun Player.inPvpCombat() = this.inCombatWithPlayer() || ((area is PrayerPlugin) && !(area as PrayerPlugin).restrictMemberPerk())
fun Player.inMeleeCombat() = this.actionManager.action is PlayerCombat && (this.actionManager.action as PlayerCombat).attackStyle.type.hitType == HitType.MELEE
fun Player.inMagicCombat() = this.actionManager.action is PlayerCombat && (this.actionManager.action as PlayerCombat).attackStyle.type.hitType == HitType.RANGED
fun Player.inRangedCombat() = this.actionManager.action is PlayerCombat && (this.actionManager.action as PlayerCombat).attackStyle.type.hitType == HitType.MAGIC

val WildyOnly = { player: Player -> player.inWildy() }
val NonWildyOnly = { player: Player -> !player.inWildy() }
val NonPvpOnly = { player: Player -> !player.inPvpCombat() }
val NonPvpOrWildy = { player: Player -> !player.inWildy() && !player.inPvpCombat() }

var Player.resetScrollsToday by persistentAttribute("rem-pet-reset-scrolls-today", 0)
var Player.choiceScrollsToday by persistentAttribute("rem-pet-choice-scrolls-today", 0)
var Player.larransBoostersToday by persistentAttribute("rem-pet-larrans-boosters-today", 0)
var Player.slayerBoostersToday by persistentAttribute("rem-pet-slayer-boosters-today", 0)
var Player.petBoostersToday by persistentAttribute("rem-pet-pet-boosters-today", 0)
var Player.lastTickPetUsage by persistentAttribute("rem-pet-last-usage-pet-restore", 0L)
var Player.burnedPostiePete by persistentAttribute("rem-pet-postie-pete", 0)
var Player.burnedShadowArcher by persistentAttribute("rem-pet-shadow-archer", 0)
var Player.burnedShadowWarrior by persistentAttribute("rem-pet-shadow-warrior", 0)
var Player.burnedShadowWizard by persistentAttribute("rem-pet-shadow-wizard", 0)
var Player.burnedToucan by persistentAttribute("rem-pet-toucan", 0)
var Player.burnedKingPenguin by persistentAttribute("rem-pet-king-penguin", 0)
var Player.burnedImp by persistentAttribute("rem-pet-imp", 0)
var Player.burnedKklik by persistentAttribute("rem-pet-kklik", 0)
var Player.burnedHealerDeathSpawn by persistentAttribute("rem-pet-healer-death-spawn", 0)
var Player.burnedHolyDeathSpawn by persistentAttribute("rem-pet-healer-death-spawn", 0)
var Player.burnedSeren by persistentAttribute("rem-pet-seren", 0)
var Player.burnedCorruptBeast by persistentAttribute("rem-pet-corrupt-beast", 0)
var Player.burnedRoc by persistentAttribute("rem-pet-roc", 0)
var Player.burnedKratos by persistentAttribute("rem-pet-kratos", 0)

var Player.burnedDarkRoc by persistentAttribute("rem-pet-dark-roc", false)
var Player.burnedDarkKratos by persistentAttribute("rem-pet-dark-kratos", false)
var Player.receivedTaskToGetFissileKratos by persistentAttribute("rem-pet-fissile-kratos-task", false)
var Player.receivedFissileKratos by persistentAttribute("rem-pet-received-fissile-kratos", false)

var Player.selectedImpPerkIsExtend by persistentAttribute("rem-pet-imp-perk-extend", true)

var Player.hasSpokenToDrifter by persistentAttribute("rem-pet-spoken-to-drifter", false)

val supplyRegex = Regex(
    """\b[A-Za-z]+(?: [A-Za-z]+)*\s+  # prefix (item name)
     (?:                          
       logs?        | ores?    | bars?       | seeds?     |
       herbs?       | leaf      | potions?    |
       runes?       | arrows?   | bolts?      | javelin heads? |
       planks?      | feathers? | bones?      | essence
     )\b
  """.trimIndent().replace("\n",""),
    RegexOption.IGNORE_CASE
)

fun Item.isSupplies() : Boolean = name.matches(supplyRegex)

fun Item.isEnsouledHead() : Boolean = name.contains("Ensouled")
fun Item.isBloodMoney() : Boolean = name.contains("Blood money", ignoreCase = true)

fun RemnantPet.inheritPerksFrom(vararg pets: RemnantPet) : MutableSet<RemnantPerk> {
    val set = mutableSetOf<RemnantPerk>()
    pets.forEach { set.addAll(it.perks) }
    return set
}
fun RemnantPet.cooldownAmt(): Int = this.perks.firstOrNull {
    it is RestoreAll || it is RestoreHealth || it is RestorePrayer
}?.requiredCooldown ?: 0
fun NPC.isRemnantPet() = RemnantPetManager.registeredPets.any { it.npcId == this.id }
fun NPC.toRemnantPet(): RemnantPet = RemnantPetManager.registeredPets.firstOrNull { it.npcId == this.id } ?: RemnantPet.NonRemnantPet
fun Item.isRemnantPet() = RemnantPetManager.registeredPets.any { it.itemId == this.id }
fun Item.toRemnantPet(): RemnantPet = RemnantPetManager.registeredPets.firstOrNull { it.itemId == this.id } ?: RemnantPet.NonRemnantPet
fun RemnantPetManager.activePerks() = this.currentRemnantPet.perks
fun RemnantPet.hasPerk(perk: RemnantPerk) = this.perks.any { perk.javaClass == it.javaClass }
inline infix fun <reified T : RemnantPerk> Collection<RemnantPerk>.has(type: KClass<T>): Boolean =
    hasType<T>()
inline fun <reified T : RemnantPerk> Collection<RemnantPerk>.hasType(): Boolean =
    any { it is T }

inline infix fun <reified T : RemnantPerk> Collection<RemnantPerk>.get(type: KClass<T>): T =
    filterIsInstance<T>().first()
inline fun <reified T : RemnantPerk> Collection<RemnantPerk>.findType(): T? =
    filterIsInstance<T>().firstOrNull()

/**
 * Only use if a hasType() check is used in advance of this call
 */
inline fun <reified T : RemnantPerk> Collection<RemnantPerk>.getType(): T =
    filterIsInstance<T>().first()

infix fun RemnantPerk.activeFor(player: Player) = this.primaryPredicate(player) && this.secondaryPredicate(player) && (this.requiredCooldown == 0 || this.cooldownPredicate(player))
fun RemnantPerk.roll() = if(scalar > 100.0) true else Utils.roll(this.scalar)

infix fun Player.chucksFor(pet: RemnantPet): Int {
    return when(pet){
        is PostiePete -> this@chucksFor.burnedPostiePete
        is Imp -> this@chucksFor.burnedImp
        is Toucan -> this@chucksFor.burnedToucan
        is KingPenguin -> this@chucksFor.burnedKingPenguin
        is Kklik -> this@chucksFor.burnedKklik
        is ShadowWarrior -> this@chucksFor.burnedShadowWarrior
        is ShadowArcher -> this@chucksFor.burnedShadowArcher
        is ShadowWizard -> this@chucksFor.burnedShadowWizard
        is HealerDeathSpawn -> this@chucksFor.burnedHealerDeathSpawn
        is HolyDeathSpawn -> this@chucksFor.burnedHolyDeathSpawn
        is Seren -> this@chucksFor.burnedSeren
        is CorruptBeast -> this@chucksFor.burnedCorruptBeast
        is Roc -> this@chucksFor.burnedRoc
        is Kratos -> this@chucksFor.burnedKratos
        else -> 0
    }
}

fun RemnantPet.shopPrice(): Int = when(this) {
        is PostiePete -> 15_000
        is Imp -> 20_000
        is Toucan -> 15_000
        is KingPenguin -> 20_000
        is Kklik -> 20_000
        is ShadowWarrior -> 15_000
        is ShadowArcher -> 15_000
        is ShadowWizard -> 15_000
        is HealerDeathSpawn -> 25_000
        is HolyDeathSpawn -> 25_000
        is Seren -> 35_000
        is CorruptBeast -> 35_000
        is Roc -> 55_000
        is Kratos -> 175_000
        else -> 0
    }

fun RemnantPet.name(): String = NPCDefinitions.get(npcId).name
infix fun Player.removed(pet: RemnantPet): Boolean {
    return inventory.deleteItem(pet.itemId, 1).result == RequestResult.SUCCESS
}

infix fun Player.awardFragmentsFor(pet: RemnantPet) {
    val rng = (Utils.random(15, 25) / 100.0)
    val refund = floor(rng * pet.shopPrice())
    inventory.addItem(Item(CustomItemId.REMNANT_POINT_VOUCHER_1, refund.toInt()))
}

infix fun Player.awardCompLeftoverFor(cnt: Int) {
    val rng = (Utils.random(20, 40) / 100.0)
    val refund = ceil(rng * cnt)
    inventory.addItem(Item(CustomItemId.PRIMAL_COMPONENTS, refund.toInt()))
}

infix fun Player.broadcast(msg: String) = WorldBroadcasts.sendMessage(Colour.GREY.wrap(msg), BroadcastType.RARE_DROP, false)
infix fun Player.broadcastBadChuck(pet: RemnantPet) = WorldBroadcasts.sendMessage(Colour.ORANGE_RED.wrap("$titleName has just lost their " + pet.name() + " to the primal fire! (# ${this chucksFor pet})") , BroadcastType.RARE_DROP, false)
infix fun Player.broadcastPrimalPet(pet: RemnantPet) = WorldBroadcasts.sendMessage(Colour.RED.wrap("$titleName has just completed fission and created a ${pet.name()}!"), BroadcastType.SUPER_RARE_DROP, false)

infix fun Player.incrementBadChuck(pet: RemnantPet) {
    when(pet){
        is PostiePete -> this@incrementBadChuck.burnedPostiePete++
        is Imp -> this@incrementBadChuck.burnedImp++
        is Toucan -> this@incrementBadChuck.burnedToucan++
        is KingPenguin -> this@incrementBadChuck.burnedKingPenguin++
        is Kklik -> this@incrementBadChuck.burnedKklik++
        is ShadowWarrior -> this@incrementBadChuck.burnedShadowWarrior++
        is ShadowArcher -> this@incrementBadChuck.burnedShadowArcher++
        is ShadowWizard -> this@incrementBadChuck.burnedShadowWizard++
        is HealerDeathSpawn -> this@incrementBadChuck.burnedHealerDeathSpawn++
        is HolyDeathSpawn -> this@incrementBadChuck.burnedHolyDeathSpawn++
        is Seren -> this@incrementBadChuck.burnedSeren++
        is CorruptBeast -> this@incrementBadChuck.burnedCorruptBeast++
        is Roc -> this@incrementBadChuck.burnedRoc++
        is Kratos -> this@incrementBadChuck.burnedKratos++
        else -> {}
    }
}
fun RemnantPet.upgrade() = PrimalFission.upgradeMappings[this]
