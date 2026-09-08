package org.jesse.scripts.npc.drops.table.dsl

import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.player.Player

/**
 * Represents a [DropTableBuilder] that are rebuild on every drop for the [player] and [npc].
 *
 * The [player] and [npc] objects can be used to construct conditional drop tables.
 *
 * @author Stan van der Bend
 */
class DynamicDropTableBuilder(val player: Player, val npc: NPC) : DropTableBuilder()
