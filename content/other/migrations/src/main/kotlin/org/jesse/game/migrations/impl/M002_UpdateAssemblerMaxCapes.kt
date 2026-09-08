package org.jesse.game.migrations.impl

import org.jesse.game.migrations.ActiveMigration
import org.jesse.game.migrations.GameMigration
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.world.entity.player.Player

@Suppress("unused", "ClassName")
@ActiveMigration
class M002_UpdateAssemblerMaxCapes : GameMigration {

    private var maxCapes: Int = 0

    override fun run(player: Player) {
//        var refreshNeeded = false
//        for(item in player.inventory.container.items.values)
//            if(checkCapes(item)) refreshNeeded = true
//        for(item in player.equipment.container.items.values)
//            if(checkCapes(item)) refreshNeeded = true
//        for(item in player.bank.container.items.values)
//            if(checkCapes(item)) refreshNeeded = true
//
//        if(refreshNeeded) {
//            for(item in player.inventory.container.items.values)
//                checkHoods(item)
//            for(item in player.equipment.container.items.values)
//                checkHoods(item)
//            for(item in player.bank.container.items.values)
//                checkHoods(item)
//            player.inventory.refreshAll()
//            player.bank.refreshContainer()
//            player.equipment.refreshAll()
//        }
    }

    private fun checkHoods(item: Item) {
        if(item.id == MAX_HOOD && maxCapes > 0) {
            item.id = ASSEMBLER_MAX_HOOD
            maxCapes--
        }
    }

    private fun checkCapes(item: Item) : Boolean {
        var found = false
        val attributes = item.attributes ?: return false
        if((item.id == MAX_CAPE) && attributes["vorkath head effect"] as Int == 1) {
            item.id = ASSEMBLER_MAX_CAPE
            maxCapes++
            found = true
        }
        return found
    }

    override fun id(): Int = 2
}