@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package org.jesse.api.service.sanction

import org.jesse.api.model.Sanction
import org.jesse.api.util.toPrivilege
import org.jesse.game.util.Colour
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege


fun Sanction.format() = buildString {
    append(Colour.RS_GREEN.wrap(type.actionName))
    if (reporter != null) {
        append(" by ")
        append(Colour.RS_GREEN.wrap(reporter))
    } else
        append(Colour.RS_GREEN.wrap("automatically"))
    if (expirationDuration != null)
        append(" expires in $expirationDuration")
    else
        append(" permanently")
    if (reason != null) {
        append(" because of ")
        append(Colour.RS_GREEN.wrap(reason))
    }
}

fun Sanction.eligible(privilege: PlayerPrivilege) =
    privilege.inherits(type.privilege.toPrivilege()) && privilege.inherits(level.privilege.toPrivilege())
