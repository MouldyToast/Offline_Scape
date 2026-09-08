package org.jesse.tools.backups.player

import org.jesse.tools.backups.Backup
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.login.LoginManager
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * Represents a type of backup for players.
 *
 * @author Stan van der Bend
 */
@Deprecated("This module is not used anymore, backups are handled through cronjob.")
sealed class PlayerBackup(
    period: Duration,
    fileExtension: String,
    folderNameSuffix: String,
) : Backup<Player>(period, fileExtension, folderNameSuffix, useCaching = true) {

    override fun toName(instance: Player): String =
        instance.username

    object Full : PlayerBackup(5.minutes, "json", "_characters") {
        override suspend fun serialise(instance: Player): ByteArray =
            LoginManager.gson.get().toJson(instance).toByteArray()
    }

    object ItemContainers : PlayerBackup(1.minutes, "dat", "_items") {
        override suspend fun serialise(instance: Player): ByteArray =
            PlayerItemsEncoder().encode(PlayerItems.of(instance))
    }
}
