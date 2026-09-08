package org.jesse.game.content.araxxor.items.venom

import org.jesse.game.content.seq
import org.jesse.game.content.consumables.ConsumableAnimation
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.pluginextensions.ItemPlugin
import org.jesse.game.model.item.pluginextensions.bindKt
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.variables.TickVariable

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-11-01
 */
class VenomSack : ItemPlugin() {
    override fun handle() {
        bindKt("Eat") {
            if (player.inventory.deleteItem(Item(ARAXYTE_VENOM_SACK, 1)).result == RequestResult.SUCCESS) {
                player seq ConsumableAnimation.EAT_ANIM.id
                player.applyHit(Hit(null, 4, HitType.VENOM))
                player.toxins.resetVenom()
                player.variables.schedule(1170, TickVariable.POISON_IMMUNITY)
                player.variables.schedule(18, TickVariable.VENOM_IMMUNITY)
            }
        }
    }

    override fun getItems(): IntArray = intArrayOf(ARAXYTE_VENOM_SACK)
}