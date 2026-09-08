package org.jesse.api.service.vote

import org.jesse.game.world.entity.persistentAttribute
import org.jesse.game.world.entity.player.Player

var Player.lastVoteClaimTime: Long by persistentAttribute("vote_claim_time", 0L)
var Player.totalVoteCredits: Int by persistentAttribute("vote_points", 0)
