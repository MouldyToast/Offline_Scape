@file:JvmName("PuzzleBoxKeys")

package com.zenyte.game.content.treasuretrails.clues

import com.zenyte.game.world.entity.player.Player
import org.rsmod.api.attr.AttributeKey

/**
 * Transient puzzle-box session state. No persistenceKey — the box never
 * persisted (the legacy Player field was transient) and still doesn't.
 */
@JvmField
val PUZZLE_BOX_KEY: AttributeKey<PuzzleBox> = AttributeKey()

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new PuzzleBox(player)) on first access. Never returns null.
 */
fun puzzleBox(player: Player): PuzzleBox {
    player.attr[PUZZLE_BOX_KEY]?.let { return it }
    val box = PuzzleBox(player)
    player.attr[PUZZLE_BOX_KEY] = box
    return box
}
