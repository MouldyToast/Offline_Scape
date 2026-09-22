package org.jesse.game.world.`object`

import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import mgi.types.config.ObjectDefinitions
import org.jesse.game.util.Utils
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.pathfinding.events.player.ObjectEvent
import org.jesse.game.world.entity.pathfinding.strategy.ObjectStrategy
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

object ObjectHandler {
    private val log: Logger = LoggerFactory.getLogger(ObjectHandler::class.java)
        private val idToAction: Int2ObjectMap<ObjectAction?> = Int2ObjectOpenHashMap<ObjectAction?>()
        private val nameToAction: Object2ObjectMap<String?, ObjectAction?> =
            Object2ObjectOpenHashMap<String?, ObjectAction?>()

        /**
         * A temporary method for until we rewrite region object management to allow up to 5 objects per location. This
         * fixes the generic gourd tree issue for now.
         */
        @JvmStatic fun verifyObject(tile: Location, id: Int): WorldObject? {
            val obj = World.getObjectWithId(tile, id)
            if (obj == null && id == 29772) {
                val overlappingObject = World.getObjectWithType(tile, 9)
                if (overlappingObject != null && overlappingObject.id == 29781) {
                    return WorldObject(id, 10, 1, tile)
                }
            }
            return obj
        }

        @JvmStatic fun handle(
            player: Player, id: Int, tile: Location, forcerun: Boolean,
            option: Int
        ) {
            val obj: WorldObject? = verifyObject(tile, id)
            if (obj == null || player.isLocked() || obj.isLocked) {
                return
            }
            val defs: ObjectDefinitions = obj.definitions ?: return
            val transformedId = player.getTransmogrifiedId(defs, obj.id)
            val transformedDefinitions: ObjectDefinitions =
                Utils.getOrDefault<ObjectDefinitions>(ObjectDefinitions.get(transformedId), defs)
            val op: String? = transformedDefinitions.getOption(option)
            val name: String = transformedDefinitions.getName()
            var action: ObjectAction? = idToAction.get(id)
            if (action == null) {
                action = nameToAction.get(name.lowercase(Locale.getDefault()))
            }

            if (action == null) {
                if (obj is ObjectAction) {
                    action = obj as ObjectAction
                }
            }

            val isDebugEnabled: Boolean = log.isDebugEnabled()
            if (isDebugEnabled && action == null) {
                log.debug("[" + name + "], id=" + transformedId + (if (transformedId != id) ("(real id: " + id + ")") else "") + ", type=" + obj.type + ", rotation=" + obj.rotation + ", option=" + (if (op == null) "null" else op) + "(" + option + "), tile=" + obj.x + ", " + obj.y + ", " + obj.plane + ", varbit=" + defs.getVarbit() + ", varp=" + defs.getVarp())
            }
            player.setFacedInteractableEntity(obj)
            player.stopAll()
            player.getPacketDispatcher().sendMapFlag(player.getXInScene(obj), player.getYInScene(obj))
            if (forcerun && player.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
                // player.setNextWorldTile(new WorldTile(object));
                if (isDebugEnabled) log.debug("[" + name + "], id=" + transformedId + (if (transformedId != id) ("(real id: " + id + ")") else "") + ", type=" + obj.type + ", rotation=" + obj.rotation + ", option=" + (if (op == null) "null" else op) + "(" + option + "), tile=" + obj.x + ", " + obj.y + ", " + obj.plane)
                player.sendMessage("Object: <col=C22731>" + obj.name + "</col> - <col=C22731>" + obj.id + "</col>, coords: " + obj.x + ", " + obj.y + ", " + obj.plane + ", face: " + obj.rotation)
                return
            } else if (forcerun) {
                player.setRun(true)
            }
            if (isDebugEnabled && action != null) {
                log.debug("[" + name + ", " + action.javaClass.getSimpleName() + "], id=" + transformedId + (if (transformedId != id) "(real id: " + id + ")" else "") + ", type=" + obj.type + ", rotation=" + obj.rotation + ", option=" + (if (op == null) "null" else op) + "(" + option + "), tile=" + obj.x + ", " + obj.y + ", " + obj.plane + ", varbit=" + defs.getVarbit() + ", varp=" + defs.getVarp())
            }
            if (action == null) {
                player.setRouteEvent(ObjectEvent(player, ObjectStrategy(obj), Runnable {
                    if (World.getObjectWithId(obj, obj.id) == null || player.getPlane() != obj.plane) {
                        return@Runnable
                    }
                    player.stopAll()
                    player.faceObject(obj)
                    if (!handleOptionClick(player, option, obj)) {
                        return@Runnable
                    }
                    player.sendMessage("Nothing interesting happens.")
                }))
                return
            }
            action.handle(player, obj, name, option, if (op == null) "null" else op)
        }

        @JvmStatic fun handleOptionClick(player: Player, option: Int, obj: WorldObject?): Boolean {
            if (option == 1) {
                return player.getControllerManager().processObjectClick1(obj)
            } else if (option == 2) {
                return player.getControllerManager().processObjectClick2(obj)
            } else if (option == 3) {
                return player.getControllerManager().processObjectClick3(obj)
            } else if (option == 4) {
                return player.getControllerManager().processObjectClick4(obj)
            } else if (option == 5) {
                return player.getControllerManager().processObjectClick5(obj)
            }
            return true
        }

        fun add(c: Class<*>) {
            val isErrorEnabled: Boolean = log.isErrorEnabled()
            try {
                val action = c.getDeclaredConstructor().newInstance() as ObjectAction
                action.init()
                for (obj in action.getObjects()) {
                    val previous: ObjectAction? = if (obj is String)
                        nameToAction.put(obj.lowercase(Locale.getDefault()), action)
                    else
                        idToAction.put(obj as Int, action)
                    if (previous != null && isErrorEnabled) {
                        log.error(
                            "OVERLAPPING object handler for object {}: {} overrides {} - only {} will handle it; "
                                    + "remove the object from one of the two plugins.",
                            obj, action.javaClass.getSimpleName(), previous.javaClass.getSimpleName(),
                            action.javaClass.getSimpleName()
                        )
                    }
                }
            } catch (e: Exception) {
                if (isErrorEnabled) log.error(
                    ("Failed to register object handler " + c.getName()
                            + " - its object actions will not work."), e
                )
            }
        }

        fun getPlugin(id: Int): ObjectAction? {
            return idToAction.get(id)
        }

    fun getPlugin(name: String): ObjectAction? {
        return nameToAction.get(name.lowercase(Locale.getDefault()))
    }
}
