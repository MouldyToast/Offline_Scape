package org.jesse.game.content.wilderness.event.hot_zone

import org.jesse.game.world.Position
import org.jesse.game.world.region.area.wilderness.WildernessArea


data object LowLevelWildernessHotZoneArea : WildernessHotZoneArea(
    wildernessLevelRange = 1..30
)

data object HighLevelWildernessHotZoneArea : WildernessHotZoneArea(
    wildernessLevelRange = 30..60
)

sealed class WildernessHotZoneArea(
    val wildernessLevelRange: IntRange,
) {
    fun contains(position: Position) =
        WildernessArea.getWildernessLevel(position.position).orElse(-1) in wildernessLevelRange
}
