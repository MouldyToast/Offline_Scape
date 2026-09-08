package org.jesse.api.facade

import org.jesse.api.model.Skill
import org.jesse.api.responses.UserUpdateHiScoresResponse

interface HiscoresFacade {

    suspend fun updateHiscores(userId: Long, skills: List<Skill>): UserUpdateHiScoresResponse
}
