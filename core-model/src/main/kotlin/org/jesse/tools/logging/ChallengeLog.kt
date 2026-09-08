package org.jesse.tools.logging

sealed interface ChallengeLog {

    data class DroppedItem(
        val username: String,
        val itemId: Int
    ): ChallengeLog

    data class MaxedAccount(
        val username: String,
        val gamemode: String,
        val ironman: Boolean
    ): ChallengeLog

    data class AchievementCape(
        val username: String
    ): ChallengeLog

    data class SlayerStatue(
        val username: String
    ): ChallengeLog

    data class SoloCOXCMClear(
        val username: String,
        val clearTimeInSeconds: Int
    ): ChallengeLog

}