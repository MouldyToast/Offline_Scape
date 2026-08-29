package com.near_reality.game.content.slayer

import com.zenyte.game.world.region.RegionArea

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.29.2025
 */
class Task @SafeVarargs constructor(
    val slayerMaster: SlayerMaster,
    val weight: Int,
    var minimumAmount: Int,
    var maximumAmount: Int,
    val areas: MutableSet<Class<out RegionArea?>> = mutableSetOf()
) {

    constructor(master: SlayerMaster, weight: Int, minimumAmount: Int, maximumAmount: Int) : this(
        master,
        weight,
        minimumAmount,
        maximumAmount,
        mutableSetOf()
    )

    constructor(master: SlayerMaster, weight: Int, minimumAmount: Int, maximumAmount: Int, vararg areaList: Class<out RegionArea?>) : this(
        master,
        weight,
        minimumAmount,
        maximumAmount,
        areaList.toMutableSet()
    )
}
