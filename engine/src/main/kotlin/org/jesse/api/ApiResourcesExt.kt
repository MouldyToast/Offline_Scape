package org.jesse.api

import org.jesse.api.dao.UserEntity
import org.jesse.api.util.toPrivilege
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege

fun UserEntity.isAuthorizedForBeta() = true

//    privilege.toPrivilege().inherits(PlayerPrivilege.FORUM_MODERATOR) ||
//    privilege.toPrivilege().inherits(PlayerPrivilege.MODERATOR)

fun UserEntity.hasValidMFA() =
    twoFactorActivated && twoFactorSecret != null