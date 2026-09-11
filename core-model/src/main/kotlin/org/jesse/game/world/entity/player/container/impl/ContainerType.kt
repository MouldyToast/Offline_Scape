package org.jesse.game.world.entity.player.container.impl

/**
 * @author Kris | 4. mai 2018 : 02:16:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
enum class ContainerType(val id: Int, val interfaceId: Int = -1, val componentId: Int = 0) {

    /** If1 containers */
    INVENTORY(93),
    FALADOR_PARTY_CHEST_DEPOSIT(92),
    FALADOR_PARTY_CHEST_PUBLIC_DEPOSIT(91),
    FALADOR_PARTY_CHEST_PRIVATE_DEPOSIT(92),
    FALADOR_PARTY_CHEST_INVENTORY_DEPOSIT(93),

    CUSTOM_FUR_CLOTHING(477),

    /** If3 containers */
    MYSTERY_BOX(133),
    HERB_SACK(10), //custom
    GEM_BAG(12), //custom
    MAGIC_STORAGE(100),
    COLLECTION_LOG(620),
    STASH_UNIT_BUILD_STAGES(576),
    PRIVATE_STORAGE(583),
    SHARED_STORAGE(582, -2),
    MAXIMUM_SIZE_CONTAINER(582),
    RAID_REWARDS(581),
    EQUIPMENT(94),
    SPOILS_STAKE(541),
    TRADE(90),
    BANK(95),
    RUNE_POUCH(169),
    GE_COLLECTABLES_1(518),
    GE_COLLECTABLES_2(519),
    GE_COLLECTABLES_3(520),
    GE_COLLECTABLES_4(521),
    GE_COLLECTABLES_5(522),
    GE_COLLECTABLES_6(523),
    GE_COLLECTABLES_7(539),
    GE_COLLECTABLES_8(540),
    PRICE_CHECKER(90),
    SHOP(510),
    BARROWS_CHEST(141),
    ITEMS_KEPT_ON_DEATH(584),
    ITEMS_LOST_ON_DEATH(468),
    DEATHS_OFFICE_RETRIEVAL(636),
    PUZZLE_BOX(140),
    THEATRE_OF_BLOOD(612),
    FALADOR_PARTY_CHEST(91),
    ITEM_RETRIEVAL_SERVICE(525),
    DUEL_STAKE(134, -1),
    OPPONENT_STAKE(134, -2),
    SEED_BOX(150),
    SEED_VAULT(626),
    LOOTING_BAG(516),

    WILDERNESS_LOOT_KEY(797),
    WILDERNESS_LOOT_KEY_FAKE(797, -2),

    STORAGE_ROOM(637),
    TOA_SUPPLY_LIFE(807),
    TOA_SUPPLY_CHAOS(808),
    TOA_SUPPLY_POWER(809),
    TOA_SUPPLY_BAG(810),
    TOA_REWARD(811),

    GIM_BANK(659),
    GIM_INVENTORY(660),

    COLOSSEUM_REWARDS(843),
    COLOSSEUM_REWARDS_FUTURE(844),
    COLOSSEUM_REWARDS_PREVIOUS(845),

    LOYALTY_TITLES(2500),
    ;

    private val displayName: String = toString().lowercase().replace("_", " ")

    fun getName(): String {
        return displayName
    }

    companion object {
        @JvmField
        val VALUES: Array<ContainerType> = values()

        @JvmField
        val GE_COLLECTABLES_CONTAINERS: Array<ContainerType> = Array(8) { valueOf("GE_COLLECTABLES_" + (it + 1)) }
    }
}
