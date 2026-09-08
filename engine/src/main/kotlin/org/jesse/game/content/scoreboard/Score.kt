package org.jesse.game.content.scoreboard

import org.jesse.utils.TimeUnit

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-17
 */
open class Score(
	open var globalKillCount: Long = 0,
	open var globalDeathCount: Long = 0,
	open var globalBestKillTimeSeconds: Int = 0
) {

	fun updateStatistics(time: Long) {
		globalKillCount++
		val duration = System.currentTimeMillis() - time
		val timeInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration).toInt()
		if (timeInSeconds == 0 || timeInSeconds < globalBestKillTimeSeconds)
			globalBestKillTimeSeconds = timeInSeconds
	}

	fun increaseDeathCount() {
		globalDeathCount++
	}

}