package org.jesse.scripts.item.actions

import org.jesse.game.item.Item
import org.jesse.game.model.item.pluginextensions.ItemPlugin.ItemStatusOnDeath
import org.jesse.game.world.entity.player.Player
import org.jesse.game.model.item.pluginextensions.ItemPlugin.ItemDeathPlugin as ZenyteItemDeathPlugin

class ItemDeathPlugin(
    val player: Player,
    val item: Item,
    val protectedCount: Int,
    val deepWilderness: Boolean,
    val pvp: Boolean
) {

    private var kept: (suspend SequenceScope<Item>.() -> Unit)? = null
    private var lost: (suspend SequenceScope<Item>.() -> Unit)? = null
    private var status: (ItemStatusOnDeath)? = null
    private var afterDeath: (() -> Unit)? = null
    private var alwaysLostOnDeath: Boolean = false
    private var alwaysKeptOnDeath: Boolean = false

    fun kept(block: suspend SequenceScope<Item>.() -> Unit) {
        this.kept = block
    }

    fun lost(block: suspend SequenceScope<Item>.() -> Unit) {
        this.lost = block
    }

    fun status(block: ItemStatusOnDeath) {
        this.status = block
    }

    fun afterDeath(block: () -> Unit) {
        this.afterDeath = block
    }

    fun setAlwaysLostOnDeath() {
        this.alwaysLostOnDeath = true
    }

    fun setAlwaysKeptOnDeath() {
        this.alwaysKeptOnDeath = true
    }

    fun build() = ZenyteItemDeathPlugin().apply {
        kept {
            sequence {
                val kept = this@ItemDeathPlugin.kept ?: return@sequence
                kept()
            }.toList()
        }

        lost {
            sequence {
                val lost = this@ItemDeathPlugin.lost ?: return@sequence
                lost()
            }.toList()
        }

        if (alwaysLostOnDeath) {
            this@apply.setAlwaysLostOnDeath()
        }

        if (alwaysKeptOnDeath) {
            this@apply.setAlwaysKeptOnDeath()
        }

        status(this@ItemDeathPlugin.status)

        this@apply.afterDeath {
            afterDeath?.invoke()
        }
    }

}