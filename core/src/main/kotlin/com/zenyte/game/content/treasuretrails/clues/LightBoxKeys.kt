@file:JvmName("LightBoxKeys")

package com.zenyte.game.content.treasuretrails.clues

import com.zenyte.game.world.entity.player.Player
import org.rsmod.api.attr.AttributeKey

/**
 * Transient light-box session state. No persistenceKey — the box never
 * persisted (the legacy Player field was transient) and still doesn't.
 */
@JvmField
val LIGHT_BOX_KEY: AttributeKey<LightBox> = AttributeKey()

/**
 * Typed accessor. Mirrors the legacy field-initializer semantics
 * (new LightBox(player)) on first access. Never returns null.
 */
fun lightBox(player: Player): LightBox {
    player.attr[LIGHT_BOX_KEY]?.let { return it }
    val box = LightBox(player)
    player.attr[LIGHT_BOX_KEY] = box
    return box
}
