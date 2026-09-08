package org.jesse.api.util

import org.jesse.api.model.ApiMemberRank
import org.jesse.api.model.ApiPrivilege
import org.jesse.game.world.entity.player.privilege.MemberRank
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege

internal fun ApiMemberRank.toMemberRank() = when(this) {
    ApiMemberRank.NONE -> MemberRank.NONE
    ApiMemberRank.TOPAZ -> MemberRank.TOPAZ
    ApiMemberRank.SAPPHIRE -> MemberRank.SAPPHIRE
    ApiMemberRank.EMERALD -> MemberRank.EMERALD
    ApiMemberRank.RUBY -> MemberRank.RUBY
    ApiMemberRank.DIAMOND -> MemberRank.DIAMOND
    ApiMemberRank.DRAGONSTONE -> MemberRank.DRAGONSTONE
    ApiMemberRank.ONYX -> MemberRank.ONYX
    ApiMemberRank.ZENYTE -> MemberRank.ZENYTE
    ApiMemberRank.ENCHANTED -> MemberRank.ENCHANTED
    ApiMemberRank.GOLD -> MemberRank.GOLD
    ApiMemberRank.ETERNAL -> MemberRank.ETERNAL
    ApiMemberRank.NEBULA -> MemberRank.NEBULA
    ApiMemberRank.CATALYTIC -> MemberRank.CATALYTIC
}

internal fun ApiPrivilege.toPrivilege() = when(this) {
    ApiPrivilege.PLAYER -> PlayerPrivilege.PLAYER
    ApiPrivilege.YOUTUBER -> PlayerPrivilege.YOUTUBER
    ApiPrivilege.MEMBER -> PlayerPrivilege.MEMBER
    ApiPrivilege.FORUM_MODERATOR -> PlayerPrivilege.FORUM_MODERATOR
    ApiPrivilege.SUPPORT -> PlayerPrivilege.SUPPORT
    ApiPrivilege.MODERATOR -> PlayerPrivilege.MODERATOR
    ApiPrivilege.SENIOR_MODERATOR -> PlayerPrivilege.SENIOR_MODERATOR
    ApiPrivilege.ADMINISTRATOR -> PlayerPrivilege.ADMINISTRATOR
    ApiPrivilege.DEVELOPER -> PlayerPrivilege.DEVELOPER
    ApiPrivilege.HIDDEN_ADMINISTRATOR -> PlayerPrivilege.HIDDEN_ADMINISTRATOR
    ApiPrivilege.TRUE_DEVELOPER -> PlayerPrivilege.TRUE_DEVELOPER
}
