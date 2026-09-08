@file:Suppress("unused")

package org.jesse.tools.backups.player

import com.google.common.eventbus.Subscribe
import org.jesse.game.world.WorldEvent
import org.jesse.plugins.events.ServerLaunchEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext
import java.nio.file.Paths
import java.time.format.DateTimeFormatter
import kotlin.time.ExperimentalTime

/**
 * Handles automatic backups of (online) players.
 *
 * @author Stan vam der Bend
 */
@DelicateCoroutinesApi
@ExperimentalTime
@Deprecated("This module is not used anymore, backups are handled through cronjob.")
object PlayerBackupsModule {

    private val scope = CoroutineScope(newFixedThreadPoolContext(2, "PlayerBackups"))
    private val backups = listOf(PlayerBackup.Full, PlayerBackup.ItemContainers)

    private val backupsDir = Paths.get("data", "backups")

    /**
     * Used for formatting of the file name of the created zip archive.
     */
    private val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

}
