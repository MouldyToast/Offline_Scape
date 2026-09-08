package com.near_reality.game.content.skills.hunter.herbiboar

/**
 * @author Andys1814
 * @since 1/26/2025
 */
enum class HerbiboarStart(val objectId: Int) {
    MIDDLE(objectId=30519),
    LEPRECHAUN(objectId=30522),
    CAMP_ENTRANCE(objectId=30521),
    GHOST_MUSHROOM(objectId=30520),
    DRIFTWOOD(objectId=30523);

    companion object {

        val OBJECT_IDS = entries.map { it.objectId }.toTypedArray()

        fun withObjectId(id: Int) = entries.firstOrNull { it.objectId == id }

    }
}