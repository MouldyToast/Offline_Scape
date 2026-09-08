package org.jesse.api.facade

import org.jesse.api.model.VoteSite
import org.jesse.api.model.VoteSiteStatus
import org.jesse.api.responses.CreateVoteResponse

interface VoteFacade {

    /**
     * Gets a list of [VoteSiteStatus] instances specific to the [GameAccount] associated with the [accountId].
     */
    suspend fun voteSiteStatuses(accountId: Long): List<VoteSiteStatus>

    /**
     * Creates a [Vote] of [voteSite] for the [GameAccount] associated with the [userId].
     */
    suspend fun createVote(voteSite: VoteSite, userId: Long, userIp: String) : CreateVoteResponse

    suspend fun claimVote(voteId: Int): Boolean
}
