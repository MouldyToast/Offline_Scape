package org.jesse.game.content.skills.hunter.herbiboar

data class HerbiboarPath(val start: HerbiboarStart, val trails: List<HerbiboarTrail>, val tunnel: HerbiboarTunnel, val tunnelVarbitId: Int, val tunnelVarbitValue: Int) {

    var currentSpotIndex = 0

    fun getCurrentTrail(): HerbiboarTrail? {
        if (currentSpotIndex >= trails.size) {
            return null
        }
        return trails[currentSpotIndex]
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(start)
        sb.append(" -> ")
        trails.forEach {
            sb.append(it)
            sb.append(" -> ")
        }
        sb.append(tunnel)
        return sb.toString()
    }

}

class HerbiboarPathBuilder {

    private var start: HerbiboarStart? = null

    private val trails = mutableListOf<HerbiboarTrail>()

    private var tunnel: HerbiboarTunnel? = null

    private var tunnelVarbitId: Int? = null

    private var tunnelVarbitValue: Int? = null

    fun start(start: HerbiboarStart) = apply {
        this.start = start
    }

    fun addTrail(trail: HerbiboarTrail) = apply {
        trails.add(trail)
    }

    fun tunnel(tunnel: HerbiboarTunnel, tunnelVarbitId: Int, tunnelVarbitValue: Int) = apply {
        this.tunnel = tunnel
        this.tunnelVarbitId = tunnelVarbitId
        this.tunnelVarbitValue = tunnelVarbitValue
    }

    fun build(): HerbiboarPath {
        checkNotNull(start) {
            "You must provide non-null start to this path"
        }
        checkNotNull(tunnel) {
            "You must provide non-null tunnel end to this path"
        }
        checkNotNull(tunnelVarbitId) {
            "You must provide non-null tunnel varbit id"
        }
        checkNotNull(tunnelVarbitValue) {
            "You must provide non-null tunnel varbit value"
        }
        return HerbiboarPath(start!!, trails, tunnel!!, tunnelVarbitId!!, tunnelVarbitValue!!)
    }

}