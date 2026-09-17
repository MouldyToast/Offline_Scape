package org.jesse.game.net.packet

import org.jesse.game.world.entity.player.PlayerActionPlugin.Companion.findHandler
import org.jesse.game.GameInterface
import org.jesse.game.content.skills.magic.Magic
import org.jesse.game.content.skills.magic.SpellDefinitions
import org.jesse.game.content.skills.magic.spells.NPCSpell
import org.jesse.game.content.skills.magic.spells.PlayerSpell
import org.jesse.game.model.item.ItemOnNPCHandler
import org.jesse.game.model.item.ItemOnPlayerHandler
import org.jesse.game.parser.impl.NPCExamineLoader
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.npc.actions.NPCHandler
import org.jesse.game.world.entity.player.GameSetting
import org.jesse.game.world.entity.player.MessageType
import org.jesse.game.world.entity.player.PlayerHandler
import org.jesse.game.world.region.area.plugins.SpellPlugin
import net.rsprot.protocol.game.incoming.npcs.OpNpcV2
import net.rsprot.protocol.game.incoming.npcs.OpNpc6
import net.rsprot.protocol.game.incoming.npcs.OpNpcT
import net.rsprot.protocol.game.incoming.players.OpPlayer
import net.rsprot.protocol.game.incoming.players.OpPlayerT

fun PacketConsumer.opplayer() {
    addListener<OpPlayer> {
        val player = player
        val option = it.op
        val run = it.controlKey
        val index = it.index

        val target = World.getPlayers().get(index)
        if (target == null || target == player || target.isFinished || !target.isInitialized) {
            return@addListener
        }

        PlayerHandler.handle(player, target, run, option)
    }
}

fun PacketConsumer.opplayert() {
    addListener<OpPlayerT> {
        val player = player
        val run = it.controlKey
        val interfaceId = it.selectedInterfaceId
        val componentId = it.selectedComponentId
        val componentIndex = it.selectedSub
        val itemId = it.selectedObj
        val index = it.index

        val target = World.getPlayers().get(index)
        if (target == null || target === player || target.isFinished || !target.isInitialized) return@addListener

        if (!player.isLocked) {
            if (interfaceId == GameInterface.FORM_GIM_TAB.id) {
                findHandler("invite")
                    .ifPresent { handler ->
                        handler.optionHandler.handle(
                            player,
                            target
                        )
                    }
                return@addListener
            } else if (interfaceId == GameInterface.INVENTORY_TAB.id) {
                val item = player.getInventory().getItem(componentIndex) ?: return@addListener
                if (run) {
                    if (player.eligibleForShiftTeleportation()) {
                        player.setLocation(Location(target.location))
                        return@addListener
                    }
                    player.setRun(true)
                }
                if (item.id != itemId) return@addListener
                ItemOnPlayerHandler.handleItemOnPlayer(player, item, componentIndex, target)
                return@addListener
            }
        }
        if (interfaceId == GameInterface.SPELLBOOK.id) {
            val spell = Magic.getSpell<PlayerSpell>(
                player.getCombatDefinitions().getSpellbook(),
                SpellDefinitions.getSpellName(componentId),
                PlayerSpell::class.java
            ) ?: return@addListener
            val area = player.area
            if (area is SpellPlugin && !(area as SpellPlugin).canCast(player, spell)) return@addListener
            spell.execute(player, target)
        }
    }
}

fun PacketConsumer.opnpc() {
    addListener<OpNpcV2> {
        val player = player
        val option = it.op
        val run = it.controlKey
        val index = it.index

        val npc = World.getNPCs().get(index)
        if (npc != null) {
            NPCHandler.handle(player, npc, run, option)
        }
    }
}

fun PacketConsumer.opnpc6() {
    addListener<OpNpc6> {
        val player = player
        val npcId = it.id

        val examine = NPCExamineLoader.get(npcId) ?: return@addListener
        player.sendMessage(examine.examine, MessageType.EXAMINE_NPC)
    }
}

fun PacketConsumer.opnpct() {
    addListener<OpNpcT> {
        val player = player
        val run = it.controlKey
        val interfaceId = it.selectedInterfaceId
        val componentId = it.selectedComponentId
        val itemId = it.selectedObj
        val slot = it.selectedSub
        val index = it.index

        if (player.isLocked) {
            return@addListener
        }
        val target = World.getNPCs()[index] ?: return@addListener
        if (run && player.eligibleForShiftTeleportation()) {
            player.setLocation(Location(target.location))
            return@addListener
        } else if (run) {
            player.setRun(true)
        }
        if (interfaceId == 149) {
            val item = player.getInventory().getItem(slot) ?: return@addListener
            if (item.id != itemId) return@addListener
            ItemOnNPCHandler.handleItemOnNPC(player, item, slot, target)
            return@addListener
        }
        if (interfaceId == 218) {
            val spell = Magic.getSpell(
                player.getCombatDefinitions().getSpellbook(), SpellDefinitions.getSpellName(componentId),
                NPCSpell::class.java
            ) ?: return@addListener
            spell.execute(player, target)
        }
    }
}
