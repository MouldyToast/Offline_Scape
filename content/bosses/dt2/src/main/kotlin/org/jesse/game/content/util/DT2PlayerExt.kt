package org.jesse.game.content.util

import org.jesse.game.world.entity.persistentAttribute
import org.jesse.game.world.entity.player.Player


var Player.hasReceivedBloodQuartz by persistentAttribute("dt2_hasReceivedBloodQuartz", false)
var Player.hasReceivedIceQuartz by persistentAttribute("dt2_hasReceivedIceQuartz", false)
var Player.hasReceivedSmokeQuartz by persistentAttribute("dt2_hasReceivedSmokeQuartz", false)
var Player.hasReceivedShadowQuartz by persistentAttribute("dt2_hasReceivedShadowQuartz", false)

var Player.hasKilledVardorvisNormal by persistentAttribute("dt2_hasKilledVardorvisNormal", false)
var Player.hasKilledDukeNormal by persistentAttribute("dt2_hasKilledDukeNormal", false)
var Player.hasKilledWhispererNormal by persistentAttribute("dt2_hasKilledWhispererNormal", false)
var Player.hasKilledLeviathanNormal by persistentAttribute("dt2_hasKilledLeviathanNormal", false)

var Player.hasKilledWhispererAwakened by persistentAttribute("dt2_hasKilledWhispererAwakened", false)
var Player.hasKilledVardorvisAwakened by persistentAttribute("dt2_hasKilledVardorvisAwakened", false)
var Player.hasKilledDukeAwakened by persistentAttribute("dt2_hasKilledDukeAwakened", false)
var Player.hasKilledLeviathanAwakened by persistentAttribute("dt2_hasKilledLeviathanAwakened", false)

fun Player.playerHasKilledAllNormalBossesOnce() : Boolean {
    return hasKilledVardorvisNormal && hasKilledDukeNormal && hasKilledWhispererNormal && hasKilledLeviathanNormal
}

fun Player.playerHasKilledAllAwakenedBossesOnce() : Boolean {
    return hasKilledLeviathanAwakened && hasKilledWhispererAwakened && hasKilledVardorvisAwakened && hasKilledDukeAwakened
}