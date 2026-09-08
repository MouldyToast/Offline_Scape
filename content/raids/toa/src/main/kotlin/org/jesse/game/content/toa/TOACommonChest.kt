package org.jesse.game.content.toa

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.31.2025
 */
enum class TOACommonChest(
    val id: Int,
    val varbit: Int
) {
    ONE(29994, 14356),
    TWO(44545, 14357),
    THREE(44547, 14358),
    FOUR(46215, 14359),
    FIVE(46216, 14360),
    SIX(46217, 14370),
    SEVEN(46218, 14371),
    EIGHT(46219, 14372);

    companion object {
        val chestIds = entries.map { it.id }
        val chestIdToDataMap = entries.associateBy { it.id }
    }

}