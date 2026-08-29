package com.zenyte.game.net.packet

import com.zenyte.game.content.skills.magic.Magic
import com.zenyte.game.content.skills.magic.SpellDefinitions
import com.zenyte.game.content.skills.magic.spells.FloorItemSpell
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.ItemOnFloorItemHandler
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.flooritem.FloorItemAction
import net.rsprot.protocol.game.incoming.objs.OpObj
import net.rsprot.protocol.game.incoming.objs.OpObj6
import net.rsprot.protocol.game.incoming.objs.OpObjT

internal fun PacketConsumer.opobj() {
    addListener<OpObj> {
        val player = player
        val x = it.x
        val y = it.z
        val itemId = it.id
        val option = it.op
        val run = it.controlKey

        FloorItemAction.handle(player, itemId, Location(x, y, player.getPlane()), option, run)
    }
}

internal fun PacketConsumer.opobj6() {
    addListener<OpObj6> {
        val player = player
        val x = it.x
        val y = it.z
        val itemId = it.id

        FloorItemAction.handle(player, itemId, Location(x, y, player.getPlane()), 6, true)
    }
}

internal fun PacketConsumer.opobjt() {
    addListener<OpObjT> {
        val player = player

        val floorItemId = it.id
        val slotId = it.selectedSub

        if (player.isNulled || player.isFinished || player.isDead || player.isLocked) {
            return@addListener
        }
        val location = Location(it.x, it.z, player.getPlane())
        val floorItem = World.getRegion(location.regionId).getFloorItem(floorItemId, location, player) ?: return@addListener

        if (it.selectedInterfaceId == 218) {
            val spell = Magic.getSpell<FloorItemSpell>(
                player.combatDefinitions.spellbook, SpellDefinitions.getSpellName(it.selectedComponentId),
                FloorItemSpell::class.java
            ) ?: return@addListener
            spell.execute(player, floorItem)
        } else {
            val item: Item = player.getInventory().getItem(slotId) ?: return@addListener
            player.stopAll()
//        player.setRun(run);
            //        player.setRun(run);
            ItemOnFloorItemHandler.handleItemOnFloorItem(player, item, floorItem)
        }
    }
}
