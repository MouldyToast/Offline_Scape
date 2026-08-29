package com.near_reality.api

import com.near_reality.api.dao.UserEntity
import com.near_reality.api.util.toPrivilege
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege

fun UserEntity.isAuthorizedForBeta() = true

//    privilege.toPrivilege().inherits(PlayerPrivilege.FORUM_MODERATOR) ||
//    privilege.toPrivilege().inherits(PlayerPrivilege.MODERATOR)

fun UserEntity.hasValidMFA() =
    twoFactorActivated && twoFactorSecret != null