package org.jesse.game.world.info

import kotlinx.serialization.Serializable

/**
 * @author Jire
 */
@Serializable
data class DatabaseProfile(
    val enabled: Boolean = false,
    val create: Boolean = false,

    val databaseUrl: String,
    val databasePort: Int = 5432,
    val databaseName: String,
    val databaseUser: String = "postgres",
    val databasePassword: String = "",
)
