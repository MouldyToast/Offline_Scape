package org.jesse.api.requests

import org.jesse.api.model.Sanction
import kotlinx.serialization.Serializable

@Serializable
data class SanctionSubmit(val sanction: Sanction)

@Serializable
data class SanctionRevoke(val sanction: Sanction)
