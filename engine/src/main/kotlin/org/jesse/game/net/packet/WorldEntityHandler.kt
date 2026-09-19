package org.jesse.game.net.packet

import net.rsprot.protocol.game.incoming.worldentities.OpWorldEntity
import net.rsprot.protocol.game.incoming.worldentities.OpWorldEntity6
import net.rsprot.protocol.game.incoming.worldentities.OpWorldEntityT

internal fun PacketConsumer.opWorldEntity() {
    addListener<OpWorldEntity> {
        val index = it.index
        val op = it.op
        val controlKey = it.controlKey
    }
}

internal fun PacketConsumer.opWorldEntity6() {
    addListener<OpWorldEntity6> {
        val id = it.id
    }
}

internal fun PacketConsumer.opWorldEntityT() {
    addListener<OpWorldEntityT> {
        val index = it.index
        val controlKey = it.controlKey
        val selectedInterfaceId = it.selectedInterfaceId
        val selectedComponentId = it.selectedComponentId
        val selectedSub = it.selectedSub
        val selectedObj = it.selectedObj
    }
}
