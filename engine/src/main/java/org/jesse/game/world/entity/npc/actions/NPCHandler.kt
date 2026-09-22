package org.jesse.game.world.entity.npc.actions

import mgi.types.config.npcs.NPCDefinitions
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.npc.actions.NPCPlugin.NPCOption
import org.jesse.game.world.entity.pathfinding.events.player.EntityEvent
import org.jesse.game.world.entity.pathfinding.strategy.EntityStrategy
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege
import org.jesse.logger.NearRealityLogger
import org.slf4j.Logger

object NPCHandler {
    private val logger: Logger = NearRealityLogger.getLogger(NPCHandler::class.java)


        fun add(c: Class<*>) {
            val action: NPCPlugin
            try {
                action = c.getDeclaredConstructor().newInstance() as NPCPlugin
            } catch (e: ReflectiveOperationException) {
                throw RuntimeException(e)
            }
            action.handle()
        }

        fun handle(
            player: Player, npc: NPC, forcerun: Boolean,
            option: Int
        ) {
            if (npc.isFinished()) return
            val id = npc.getId()
            val baseDefinitions = NPCDefinitions.getOrThrow(id)
            val transmogrifiedId = player.getTransmogrifiedId(baseDefinitions, id)
            if (transmogrifiedId == -1) {
                return
            }
            val transformedDefinitions = NPCDefinitions.getOrThrow(transmogrifiedId)
            val name = transformedDefinitions.getName()
            val op = transformedDefinitions.getOption(option)
            var plugin = NPCPlugin.getHandler(transmogrifiedId, Utils.getOrDefault(op, "null"))
            if (plugin == null) {
                plugin = NPCPlugin.getHandler(id, Utils.getOrDefault(op, "null"))
            }
            val pluginName =
                if (plugin == null) "Absent" else if (plugin.getPlugin() == null) "Default" else plugin.getPlugin().javaClass.getSimpleName()
            if (logger.isDebugEnabled()) logger.debug(
                "[" + pluginName + "] " + name + "(base: " + id + (if (baseDefinitions.getTransmogrifiedIds() == null) "" else (", visible: " + transmogrifiedId)) + "), option: " + op + "(" + option + "), index: " + npc.getIndex() + ", tile: [" + npc.getX() + ", " + npc.getY() + ", " + npc.getPlane() + "], varbit: " + npc.getDefinitions()
                    .getVarbit()
            )
            if (op == null || player.isLocked() || !player.isVisibleInViewport(npc) || player.isFullMovementLocked()) {
                return
            }
            if (player.isStunned()) {
                player.sendFilteredMessage("You're stunned.")
                return
            }
            player.stopAll()
            //TODO: Rewrite game locks into multiple types and create scrying as a possible type.
            if (player.getTemporaryAttributes().get("Scrying") != null) {
                return
            }
            if (forcerun) {
                if (player.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
                    player.sendMessage("NPC: <col=C22731>" + npc.getName(player) + "</col> - <col=C22731>" + transmogrifiedId + "</col>, coords: " + npc.getX() + ", " + npc.getY() + ", " + npc.getPlane())
                    player.setLocation(Location(npc.location))
                    return
                }
                player.setRun(true)
            }
            if (plugin != null) {
                plugin.getOption().click(player, npc, NPCOption(option, Utils.getOrDefault(op, "null")))
                return
            }
            player.setRouteEvent(EntityEvent(player, EntityStrategy(npc), Runnable {
                player.stopAll()
                player.setFaceEntity(npc)
                if (npc.getRadius() > 0) npc.setInteractingWith(player)
                player.sendMessage("Nothing interesting happens.")
            }, true))
        }
}
