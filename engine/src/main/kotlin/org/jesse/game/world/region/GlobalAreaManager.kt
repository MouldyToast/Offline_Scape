package org.jesse.game.world.region

import org.jesse.tools.collections.Tree
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.Position
import org.jesse.game.world.World
import org.jesse.game.world.entity._Location.Companion.getRegionIDByRegion
import org.jesse.game.world.entity._Location.Companion.getRegionX
import org.jesse.game.world.entity._Location.Companion.getRegionY
import org.jesse.game.world.entity.npc.spawns.NPCSpawnLoader
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.region.area.plugins.CycleProcessPlugin
import org.jesse.game.world.region.areatype.AreaType.Companion.regionHash
import org.jesse.logger.NearRealityLogger
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.slf4j.Logger
import java.lang.reflect.Modifier
import java.util.*
import java.util.function.Predicate

/**
 * @author Jire
 * @author Kris
 * @author Stan van der Bend
 */
object GlobalAreaManager {

    private val log: Logger = NearRealityLogger.getLogger(GlobalAreaManager::class.java)

    private val topmostAreas: ObjectSet<RegionArea> = ObjectOpenHashSet()
    private val areaToSuperclass: Object2ObjectMap<RegionArea, Class<*>> = Object2ObjectOpenHashMap()

    private val regionToAreas: Int2ObjectMap<ObjectList<RegionArea>> = Int2ObjectOpenHashMap()

    private val areaTree = Tree(RegionArea::class)
    private val classToArea: Object2ObjectMap<Class<out RegionArea>, RegionArea> = Object2ObjectOpenHashMap()
    private val nameToArea: Object2ObjectMap<String, RegionArea> = Object2ObjectOpenHashMap()

    private fun add(areaClass: Class<out RegionArea>) {
        try {
            val area = areaClass.kotlin.objectInstance ?: areaClass.getDeclaredConstructor().newInstance()
            if (area.isDynamicArea)
                throw IllegalArgumentException("Area $areaClass cannot be a dynamic area!")

            val superClass = superClass(areaClass)
            if (superClass == null) {
                if (!topmostAreas.add(area))
                    throw IllegalStateException("Topmost area $areaClass was already mapped!")
            } else {
                if (areaToSuperclass.containsKey(area))
                    throw IllegalStateException("Area $areaClass was already mapped to super class!")
                areaToSuperclass[area] = superClass
            }

            if (classToArea.containsKey(areaClass))
                throw IllegalStateException("Area $areaClass was already mapped!")
            classToArea[areaClass] = area

            val areaName = area.name()
            val existingNamedArea = nameToArea[areaName]
            if (existingNamedArea != null)
                throw IllegalStateException(
                    "Area by name \"$areaName\" was already mapped to " +
                            "${existingNamedArea.javaClass} ($areaClass)"
                )
            nameToArea[areaName] = area

            if (area.hasPolygons() && area is PolygonRegionArea) {
                for (poly in area.polygons) {
                    mapPoly(area, poly ?: error("Polygon is null for area $area"))
                }
            }
        } catch (e: Exception) {
            log.error("Failed to register area ${areaClass.name} - it will not exist at runtime and " +
                    "getArea() calls for it will throw.", e)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun addUnsafe(areaClass: Class<*>) {
        val kClass = (areaClass as Class<out RegionArea>).kotlin
        areaTree.add(kClass)
    }

    private fun mapPoly(area: RegionArea, poly: RSPolygon) {
        var minX = Int.MAX_VALUE
        var minY = Int.MAX_VALUE

        var maxX = Int.MIN_VALUE
        var maxY = Int.MIN_VALUE

        for (point in poly.points) {
            val x = point[0]
            val y = point[1]

            if (x < minX) minX = x
            if (y < minY) minY = y

            if (x > maxX) maxX = x
            if (y > maxY) maxY = y
        }

        for (y in getRegionY(minY)..getRegionY(maxY)) {
            for (x in getRegionX(minX)..getRegionX(maxX)) {
                val region = getRegionIDByRegion(x, y)

                for (plane in poly.planes) {
                    map(area, region, plane)
                }
            }
        }
    }

    private fun map(area: RegionArea, region: Int, plane: Int): Boolean {
        val regionKey = regionKey(region, plane)
        var list = regionToAreas.get(regionKey)
        if (list == null) {
            list = ObjectArrayList()
            regionToAreas.put(regionKey, list)
        }
        return if (!list.contains(area)) {
            list.add(area)
        } else
            false
    }

    @JvmStatic
    operator fun get(name: String) =
        nameToArea[name] ?: throw Exception("Area by the name of \"$name\" does not exist.")

    @JvmStatic
    fun <T : RegionArea> getArea(clazz: Class<T>): T =
        getNullableArea(clazz)
            ?: throw Exception("Area by the type of \"${clazz.simpleName}\" does not exist.")

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T : RegionArea> getNullableArea(clazz: Class<T>): T? =
        classToArea[clazz] as? T

    @JvmStatic
    fun setInheritance() {
        areaTree.resolvePendingNodes()
        val rootNodes = areaTree.getRootNodes()
        fun recursiveAdd(leaf: Tree.Node<RegionArea>, depth: Int) {
            if (depth == 100)
                error("Reached max depth $depth could not add ${leaf.value}")
            add(leaf.value.java)
            leaf.children.forEach { recursiveAdd(it, depth + 1) }
        }
        rootNodes.forEach { recursiveAdd(it, 0) }
        log.trace("Printing nested areas:")
        rootNodes
            .filter { it.children.isNotEmpty() }
            .forEach { log.trace(it.toString()) }
        for ((area, superClass) in areaToSuperclass) {
            val superArea = classToArea[superClass]!!
            area.addSuper(superArea)
            superArea.addSub(area)
        }
    }

    @JvmStatic
    fun superClass(areaClass: Class<out RegionArea>): Class<*>? {
        var clazz = areaClass.superclass
        while (clazz != null && RegionArea::class.java.isAssignableFrom(clazz)) {
            if (Modifier.isAbstract(clazz.modifiers)) {
                clazz = clazz.superclass
                continue
            }
            return clazz
        }

        return null
    }

    fun map() {
        WorldTasksManager.scheduleCreation {
            for (player in World.getPlayers()) {
                update(player, login = false, logout = false)
            }

            NPCSpawnLoader.populateAreaMap()
        }
    }

    private fun regionKey(region: Int, plane: Int) = region or (plane shl 16)

    @JvmStatic
    fun add(area: DynamicArea) {
        if (topmostAreas.contains(area))
            remove(area)
        if (!topmostAreas.add(area))
            log.error("Area $area was already mapped!")
        for (y in (area.chunkY shr 3)..((area.chunkY + area.sizeY) shr 3)) {
            for (x in (area.chunkX shr 3)..((area.chunkX + area.sizeX) shr 3)) {
                val region = getRegionIDByRegion(x, y)
                for (plane in 0..3) {
                    map(area, region, plane)
                }
            }
        }
    }

    @JvmStatic
    fun remove(area: DynamicArea) {
        if (!topmostAreas.remove(area))
            log.error("Area $area was not mapped!")
        for (regionToAreas in regionToAreas.values) {
            regionToAreas.remove(area)
        }
    }

    @JvmStatic
    fun update(player: Player, login: Boolean, logout: Boolean) {
        val lastArea = player.area
        val newArea = getArea(player.location)
        if (logout || login || lastArea != newArea) {
            (lastArea as? AbstractRegionArea)?.removePlayer(player, logout)
            if (!logout && newArea is AbstractRegionArea) newArea.addPlayer(player, login)
        }
    }

    @JvmStatic
    fun getArea(position: Position): RegionArea? {
        val tile = position.position
        val regionKey = regionKey(tile.regionId, tile.plane)
        val areas = regionToAreas.get(regionKey) ?: return null
        for (i in 0..areas.lastIndex) {
            val area = areas[i] ?: continue
            val subMost = area.getSubmost(tile, true)
            if (subMost != null) return subMost
        }
        return null
    }

    @JvmStatic
    fun getArea(position: Position, clazz: Class<out RegionArea?>): RegionArea? {
        val tile = position.getPosition()
        val areas = regionToAreas.get(regionHash(tile.x, tile.y, tile.plane)) ?: return null
        for (a in areas) {
            if (clazz.isAssignableFrom(a.javaClass)) {
                val extension = a.getSubmost(position, false)
                if (extension.inside(position.getPosition())) return extension
            }
        }
        return null
    }

    @JvmStatic
    fun findArea(position: Position) =
        Optional.ofNullable(getArea(position))

    private val removalPredicate = Predicate<Player> { it.isNulled || !it.isAllocated }

    @JvmStatic
    fun process() {
        process(topmostAreas)
    }

    private fun process(areas: Collection<RegionArea>) {
        if (areas.isEmpty()) return
        for (area in areas) {
            process(area)
            process(area.subAreas)
        }
    }

    private fun process(area: RegionArea) {
        try {
            if (++area.areaTimer % 10 == 0) {
                val players = area.players
                if (players.isNotEmpty())
                    players.removeIf(removalPredicate)
            }
            (area as? CycleProcessPlugin)?.process()
        } catch (e: Exception) {
            log.error("Error processing area ${area.javaClass.simpleName}", e)
        }
    }

    @JvmStatic
    fun postProcess() = postProcess(topmostAreas)

    private fun postProcess(areas: Collection<RegionArea>) {
        if (areas.isEmpty()) return
        for (area in areas) {
            postProcess(area)
            postProcess(area.subAreas)
        }
    }

    private fun postProcess(area: RegionArea) {
        try {
            (area as? CycleProcessPlugin)?.postProcess()
        } catch (e: Exception) {
            log.error("Error post-processing area ${area.javaClass.simpleName}", e)
        }
    }

}
