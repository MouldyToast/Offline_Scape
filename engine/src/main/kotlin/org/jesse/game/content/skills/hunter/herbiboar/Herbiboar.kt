package org.jesse.game.content.skills.hunter.herbiboar

import org.jesse.game.world.entity.player.Player

/**
 * @author Andys1814
 * @since 1/26/2025
 */
object Herbiboar {

    private const val TRAIL_ATTRIBUTE = "herbiboar_trail"

    private const val UNHARVESTED_HERBIBOAR_ATTRIBUTE = "herbiboar_unharvested"

    const val UNHARVESTED_WARNING_ATTRIBUTE = "herbiboar_unharvested_warning"

    var Player.currentHerbiboarPath: HerbiboarPath?
        get() = temporaryAttributes.getOrDefault(TRAIL_ATTRIBUTE, null) as HerbiboarPath?
        set(value) {
            temporaryAttributes[TRAIL_ATTRIBUTE] = value
        }

    var Player.unharvestedHerbiboar: Int?
        get() = temporaryAttributes.getOrDefault(UNHARVESTED_HERBIBOAR_ATTRIBUTE, null) as Int?
        set(value) {
            temporaryAttributes[UNHARVESTED_HERBIBOAR_ATTRIBUTE] = value
        }

    val REGIONS = listOf(
        14652,
        14651,
        14908,
        14907
    )

    val varbitMap = mapOf(
        31303 to 5737,
        31306 to 5738,
        31309 to 5739,
        31312 to 5740,
        31315 to 5741,
        31318 to 5742,
        31321 to 5743,
        31324 to 5744,
        31327 to 5745,
        31330 to 5746,
        31333 to 5768,
        31336 to 5769,
        31339 to 5770,
        31342 to 5771,
        31345 to 5772,
        31348 to 5773,
        31351 to 5774,
        31354 to 5775,
        31357 to 5776,
        31360 to 5777,
        31363 to 5747,
        31366 to 5748,
        31369 to 5749,
        31372 to 5750,
    )

    val HB_TRAIL_31303 = 5737
    val HB_TRAIL_31306 = 5738
    val HB_TRAIL_31309 = 5739
    val HB_TRAIL_31312 = 5740
    val HB_TRAIL_31315 = 5741
    val HB_TRAIL_31318 = 5742
    val HB_TRAIL_31321 = 5743
    val HB_TRAIL_31324 = 5744
    val HB_TRAIL_31327 = 5745
    val HB_TRAIL_31330 = 5746
    val HB_TRAIL_31333 = 5768
    val HB_TRAIL_31336 = 5769
    val HB_TRAIL_31339 = 5770
    val HB_TRAIL_31342 = 5771
    val HB_TRAIL_31345 = 5772
    val HB_TRAIL_31348 = 5773
    val HB_TRAIL_31351 = 5774
    val HB_TRAIL_31354 = 5775
    val HB_TRAIL_31357 = 5776
    val HB_TRAIL_31360 = 5777
    val HB_TRAIL_31363 = 5747
    val HB_TRAIL_31366 = 5748
    val HB_TRAIL_31369 = 5749
    val HB_TRAIL_31372 = 5750

    val HB_FINISH = 5766
    val HB_STARTED = 5767

    fun Player.resetHerbiboarVars() {
        varManager.sendBit(HB_TRAIL_31303, 0)
        varManager.sendBit(HB_TRAIL_31306, 0)
        varManager.sendBit(HB_TRAIL_31309, 0)
        varManager.sendBit(HB_TRAIL_31312, 0)
        varManager.sendBit(HB_TRAIL_31315, 0)
        varManager.sendBit(HB_TRAIL_31318, 0)
        varManager.sendBit(HB_TRAIL_31321, 0)
        varManager.sendBit(HB_TRAIL_31324, 0)
        varManager.sendBit(HB_TRAIL_31327, 0)
        varManager.sendBit(HB_TRAIL_31330, 0)
        varManager.sendBit(HB_TRAIL_31333, 0)
        varManager.sendBit(HB_TRAIL_31336, 0)
        varManager.sendBit(HB_TRAIL_31339, 0)
        varManager.sendBit(HB_TRAIL_31342, 0)
        varManager.sendBit(HB_TRAIL_31345, 0)
        varManager.sendBit(HB_TRAIL_31348, 0)
        varManager.sendBit(HB_TRAIL_31351, 0)
        varManager.sendBit(HB_TRAIL_31354, 0)
        varManager.sendBit(HB_TRAIL_31357, 0)
        varManager.sendBit(HB_TRAIL_31360, 0)
        varManager.sendBit(HB_TRAIL_31363, 0)
        varManager.sendBit(HB_TRAIL_31366, 0)
        varManager.sendBit(HB_TRAIL_31369, 0)
        varManager.sendBit(HB_TRAIL_31372, 0)
        varManager.sendBit(HB_STARTED, 0)
        varManager.sendBit(HB_FINISH, 0)
    }

}