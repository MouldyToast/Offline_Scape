package org.jesse.api.model

import kotlinx.serialization.Serializable

@Serializable
data class GameWorldCredentials(val username: String, val password: String)
