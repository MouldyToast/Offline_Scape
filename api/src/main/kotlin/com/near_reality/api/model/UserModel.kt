package com.near_reality.api.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val name: String,
    val dbName: String = "",
    val passwordHash: String? = null,
    val passwordAtRisk: Boolean,
    val email: String,
    val gameMode: ApiGameMode,
    val memberRank: ApiMemberRank,
    val privilege: ApiPrivilege,
    val twoFactorEnabled: Boolean,
    val twoFactorSecret: String? = null,
    val totalSpent: Int,
    val storeCredits: Int,
    val totalVotes: Int,
    val joinDate: LocalDateTime,
    val sanctions: List<Sanction>
) {
    fun stripCredentials() = copy(passwordHash = null, twoFactorSecret = null)
}

@Serializable
enum class Bond(val id: Int, val credits: Int, val amount: Int) {
    DONATOR_PIN_5(2759, 50, 5),
    DONATOR_PIN_10(32070, 130, 10),
    DONATOR_PIN_25(32071, 325, 25),
    DONATOR_PIN_35(2761, 400, 35),
    DONATOR_PIN_50(32072, 650, 50),
    DONATOR_PIN_100(32073, 1300, 100);

    companion object {
        operator fun get(id: Int) = entries.find { it.id == id }
    }
}


@Serializable
enum class ApiGameMode {
    REGULAR,
    STANDARD_IRON_MAN,
    ULTIMATE_IRON_MAN,
    HARDCORE_IRON_MAN,
    GROUP_IRON_MAN,
    GROUP_HARDCORE_IRON_MAN;

    val isIronMan: Boolean
        get() = this != REGULAR

    companion object {
        fun forId(id: Number) = when (id.toInt()) {
            1 -> REGULAR
            2 -> STANDARD_IRON_MAN
            3 -> ULTIMATE_IRON_MAN
            4 -> HARDCORE_IRON_MAN
            5 -> GROUP_IRON_MAN
            else -> error("Did not find ApiGameMode for id $id")
        }
    }
}

@Serializable
enum class ApiMemberRank(val formattedName: String, val requiredDonatedAmount: Int) {
    NONE("None", 0),
    TOPAZ("Topaz", 10),
    SAPPHIRE("Sapphire", 25),
    EMERALD("Emerald", 50),
    RUBY("Ruby", 100),
    DIAMOND("Diamond", 250),
    DRAGONSTONE("Dragonstone", 500),
    ONYX("Onyx", 1_000),
    ZENYTE("Zenyte", 2_500),
    ENCHANTED("Enchanted", 5_000),
    GOLD("Gold", 7_500),
    ETERNAL("Eternal", 10_000),
    NEBULA("Nebula", 15_000),
    CATALYTIC("Catalytic", 25_000),
    ;

    companion object {
        fun findForUserWithAmountSpent(username: String? = null, totalSpent: Int) =
            entries
                .filter { it.requiredDonatedAmount <= totalSpent }
                .maxBy { it.requiredDonatedAmount }
    }
}

@Serializable
enum class ApiPrivilege(val requires2FA: Boolean = false) {
    PLAYER,
    YOUTUBER,
    MEMBER,
    FORUM_MODERATOR,
    SUPPORT(true),
    MODERATOR(true),
    SENIOR_MODERATOR(true),
    ADMINISTRATOR(true),
    DEVELOPER(true),
    HIDDEN_ADMINISTRATOR(true),
    TRUE_DEVELOPER(true)
}

