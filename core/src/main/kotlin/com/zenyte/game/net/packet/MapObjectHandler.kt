package com.zenyte.game.net.packet

import com.zenyte.game.content.skills.magic.Magic
import com.zenyte.game.content.skills.magic.SpellDefinitions
import com.zenyte.game.content.skills.magic.spells.ObjectSpell
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.ItemOnObjectHandler
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy
import com.zenyte.game.world.entity.player.MessageType
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.game.world.`object`.ObjectExamineLoader
import com.zenyte.game.world.`object`.ObjectHandler
import mgi.types.config.ObjectDefinitions
import net.rsprot.protocol.game.incoming.locs.OpLoc
import net.rsprot.protocol.game.incoming.locs.OpLoc6
import net.rsprot.protocol.game.incoming.locs.OpLocT

internal fun PacketConsumer.oploc() {
    addListener<OpLoc> {
        val player = player
        val id = it.id
        val x = it.x
        val y = it.z
        val option = it.op
        val run = it.controlKey

        ObjectHandler.handle(player, id, Location(x, y, player.getPlane()), run, option)
    }
}

internal fun PacketConsumer.oploc6() {
    addListener<OpLoc6> {
        val player = player
        val id = it.id

        val examine = ObjectExamineLoader.DEFINITIONS[id] ?: return@addListener
        val def = ObjectDefinitions.get(id)
        if (player.privilege.inherits(PlayerPrivilege.ADMINISTRATOR)) {
            if (def != null) {
                player.sendMessage("Object: <col=C22731>" + def.name + "</col> - " + id + "</col>")
            }
        }
        player.sendMessage(examine.examine, MessageType.EXAMINE_OBJECT)
    }
}

internal fun PacketConsumer.oploct() {
    addListener<OpLocT> {
        val player = player
        val objectId = it.id
        val x = it.x
        val y = it.z
        val interfaceId = it.selectedInterfaceId
        val componentId = it.selectedComponentId
        val slotId = it.selectedSub
        val itemId = it.selectedObj
        val run = it.controlKey

        val location = Location(x, y, player.getPlane())
        val `object` = World.getObjectWithId(location, objectId) ?: return@addListener
        if (run && player.eligibleForShiftTeleportation()) {
            player.setLocation(Location(`object`))
            return@addListener
        } else if (run) {
            player.setRun(true)
        }
        player.stopAll()
        if (interfaceId == 149) {
            val item: Item = player.getInventory().getItem(slotId) ?: return@addListener
            ItemOnObjectHandler.handleItemOnObject(player, item, slotId, `object`)
            return@addListener
        }
        player.setRouteEvent(ObjectEvent(player, ObjectStrategy(`object`), Runnable {
            player.stopAll()
            player.faceObject(`object`)
            if (interfaceId == 218) {
                val spell = Magic.getSpell<ObjectSpell>(
                    player.getCombatDefinitions().getSpellbook(), SpellDefinitions.getSpellName(componentId),
                    ObjectSpell::class.java
                ) ?: return@Runnable
                spell.execute(player, `object`)
            }
        }))
    }
}
