package com.zenyte.game.world.entity

import com.google.gson.annotations.Expose

open class _Location(@field:Expose @JvmField protected var hash: Int) {

    constructor(x: Int, y: Int, z: Int) : this(y or (x shl 14) or (z shl 28))

    open val x: Int
        get() = (hash shr 14) and 16383

    open val y: Int
        get() = hash and 16383

    open val plane: Int
        get() = (hash shr 28) and 3

    open val chunkX: Int
        get() = x shr 3

    open val chunkY: Int
        get() = y shr 3

    open val xInRegion: Int
        get() = x and 63

    open val yInRegion: Int
        get() = y and 63

    open val xInChunk: Int
        get() = x and 7

    open val yInChunk: Int
        get() = y and 7

    open val positionHash: Int
        get() = hash

    override fun hashCode(): Int {
        return positionHash
    }

    override fun equals(other: Any?): Boolean {
        if (other !is _Location) {
            return false
        }
        return other.positionHash == positionHash
    }

    open val regionX: Int
        get() = getRegionX(x)

    open val regionY: Int
        get() = getRegionY(y)

    open val regionId: Int
        get() = getRegionId(x, y)

    override fun toString(): String {
        return "Tile: " + x + ", " + y + ", " + plane + ", region[" + regionId + ", " + regionX + ", " + regionY + "], chunk[" + chunkX + ", " + chunkY + "], hash [" + positionHash + "]"
    }

    companion object {
        @JvmStatic
        fun getRegionId(x: Int, y: Int): Int {
            return getRegionIDByRegion(getRegionX(x), getRegionY(y))
        }

        @JvmStatic
        fun getRegionX(x: Int): Int {
            return x shr 6
        }

        @JvmStatic
        fun getRegionY(y: Int): Int {
            return y shr 6
        }

        @JvmStatic
        fun getRegionIDByRegion(regionX: Int, regionY: Int): Int {
            return (regionX shl 8) or regionY
        }
    }
}
