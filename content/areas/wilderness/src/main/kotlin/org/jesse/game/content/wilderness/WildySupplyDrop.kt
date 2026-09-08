//package org.jesse.game.content.wilderness
//
//import org.jesse.game.item.ids.*
//import org.jesse.game.item.Item
//import org.jesse.game.model.item.pluginextensions.ItemPlugin
//import org.jesse.game.util.Utils
//import org.jesse.game.world.entity.player.Player
//
//class WildySupplyDrop {
//    sealed interface Table {
//        fun loots() : Collection<TableItem>
//        fun item() : Item = loots().shuffled().take(1).last().create()
//    }
//
//    data class TableItem(val itemId : Int, val min : Int = 1, val max : Int = 1, val noted : Boolean = false) {
//        private fun quantity() = Utils.random(min, max)
//        fun create(): Item = if(noted) Item(itemId, quantity()).toNote() else Item(itemId, quantity())
//    }
//
//    data object SupplyTable: Table {
//        override fun loots(): Collection<TableItem> =
//            listOf(
//                TableItem(3025)
//
//            )
//    }
//
//
//
//    @Suppress("unused")
//    object Action : ItemPlugin() {
//        override fun handle() {
//            bind("Open") { p: Player, item: Item, _: Int ->
//
//            }
//        }
//
//        override fun getItems(): IntArray = intArrayOf(ItemId.WILDY_SUPPLY_SACK, ItemId.WILDY_SUPPLY_CRATE, ItemId.WILDY_SUPPLY_HOARD)
//    }
//}