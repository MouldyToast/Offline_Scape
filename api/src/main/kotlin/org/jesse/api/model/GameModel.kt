package org.jesse.api.model

import kotlinx.serialization.Serializable

typealias SlotItemMap = Map<Int, Item>

@Serializable
data class Item(val id: Int, val amount: Int)

@Serializable
data class ItemConfig(
    val id: Int,
    val name: String,
    val tradeable: Boolean,
    val ecoValue: Int?,
    val generalStore: Int?,
    val protectionValue: Int?,
) {
    fun getSellPrice() : Int {
        return generalStore ?: -99
    }
}

@Serializable
data class Location(val hash: Int) {
    constructor(x: Int, y: Int, z: Int) : this(y or (x shl 14) or (z shl 28))

    val x get() = (hash shr 14) and 16383
    val y get() = hash and 16383
    val z get() = (hash shr 28) and 3

    override fun toString(): String {
        return "${x}, ${y}, ${z}"
    }
}

@Serializable
data class ItemContainer(
    val policy: Policy = Policy.NORMAL,
    val items: SlotItemMap = emptyMap()
) {
    @Serializable
    enum class Policy {
        ALWAYS_STACK,
        NEVER_STACK,
        NORMAL
    }
}

@Serializable
data class ItemContainerWrapper(
    val container: ItemContainer,
) {
    constructor(policy: ItemContainer.Policy) : this(ItemContainer(policy))
}
@Serializable
enum class GrandExchangeOfferType {
    BUY,
    SELL
}

@Serializable
data class GrandExchangeOffer(
    val item: Item,
    val slot: Int,
    val price: Int,
    val type: GrandExchangeOfferType,
)


@Serializable
data class DeathResult(
    val inventory: SlotItemMap,
    val equipment: SlotItemMap,
    val lootingBag: SlotItemMap,
    val runePouch1: SlotItemMap,
    val runePouch2: SlotItemMap,
    val kept: SlotItemMap,
    val lost: SlotItemMap,
    val lostToKiller: List<Item>,
    val grave:  List<Item>,
)

@Serializable
enum class WorldLocation(val id: Int) {
    UNITED_STATES_OF_AMERICA(0),
    UNITED_KINGDOM(1),
    CANADA(2),
    AUSTRALIA(3),
    NETHERLANDS(4),
    SWEDEN(5),
    FINLAND(6),
    GERMANY(7);
}

@Serializable
enum class WorldType(val mask: Int) {
    MEMBERS(1),
    PVP(1 shl 2),
    BOUNTY(1 shl 5),
    PVP_ARENA(1 shl 6),
    SKILL_TOTAL(1 shl 7),
    QUEST_SPEEDRUNNING(1 shl 8),
    HIGH_RISK(1 shl 10),
    LAST_MAN_STANDING(1 shl 14),
    NOSAVE_MODE(1 shl 25),
    TOURNAMENT(1 shl 26),
    FRESH_START_WORLD(1 shl 27),
    DEADMAN(1 shl 29),
    SEASONAL(1 shl 30);

    companion object {
        fun toMask(types: Array<WorldType>) : Int {
            var mask = 0
            for (type in types) {
                mask = mask or type.mask
            }
            return mask
        }
    }
}

@Serializable
enum class WorldVisibility {

    /**
     * The world is only visible to developers.
     */
    DEVELOPER,

    /**
     * The world is only visible to beta testers.
     */
    BETA,

    /**
     * The world is visible to everyone.
     */
    PUBLIC
}
