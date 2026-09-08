package org.jesse.api.facade

import org.jesse.api.model.Sanction
import org.jesse.api.model.SanctionLevel
import org.jesse.api.responses.SanctionRevokeResponse
import org.jesse.api.responses.SanctionSubmitResponse

interface SanctionFacade {

    suspend fun submitSanction(sanction: Sanction) : SanctionSubmitResponse

    suspend fun revokeSanction(sanctionId: Long, level: SanctionLevel) : SanctionRevokeResponse
}
