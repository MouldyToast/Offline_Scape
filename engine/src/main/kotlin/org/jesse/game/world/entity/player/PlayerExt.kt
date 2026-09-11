package org.jesse.game.world.entity.player

import org.jesse.game.item.Item
import org.jesse.game.model.ui.InterfacePosition
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.cutscene.FadeScreen
import org.jesse.game.world.region.area.plugins.DeathPlugin
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author Kris | 16/06/2022
 */
fun varp(id: Int) = object : ReadWriteProperty<Player, Int> {
    override fun getValue(thisRef: Player, property: KProperty<*>): Int = thisRef.varManager.getValue(id)
    override fun setValue(thisRef: Player, property: KProperty<*>, value: Int) {
        thisRef.varManager.sendVarInstant(id, value)
    }
}

fun varbit(id: Int) = object : ReadWriteProperty<Player, Int> {
    override fun getValue(thisRef: Player, property: KProperty<*>): Int = thisRef.varManager.getBitValue(id)
    override fun setValue(thisRef: Player, property: KProperty<*>, value: Int) {
        thisRef.varManager.sendBitInstant(id, value)
    }
}

fun booleanVarp(id: Int) = object : ReadWriteProperty<Player, Boolean> {
    override fun getValue(thisRef: Player, property: KProperty<*>): Boolean = thisRef.varManager.getValue(id) == 1

    override fun setValue(thisRef: Player, property: KProperty<*>, value: Boolean) {
        thisRef.varManager.sendVarInstant(id, if (value) 1 else 0)
    }
}

fun booleanVarbit(id: Int) = object : ReadWriteProperty<Player, Boolean> {
    override fun getValue(thisRef: Player, property: KProperty<*>): Boolean = thisRef.varManager.getBitValue(id) == 1

    override fun setValue(thisRef: Player, property: KProperty<*>, value: Boolean) {
        thisRef.varManager.sendBitInstant(id, if (value) 1 else 0)
    }
}

inline fun<reified T> typeVarbit(id: Int, crossinline get: (Int) -> T, crossinline set: (T) -> Int): ReadWriteProperty<Player, T> {
    val varbitProperty  = varbit(id)
    return object : ReadWriteProperty<Player, T> {
        override fun getValue(thisRef: Player, property: KProperty<*>): T =
            get(varbitProperty.getValue(thisRef, property))

        override fun setValue(thisRef: Player, property: KProperty<*>, value: T) {
            set(value).let { varbitProperty.setValue(thisRef, property, it) }
        }
    }
}

fun Player.handleAdminHealthEvent(killer: Player) {
    // get the players inventory and Equipment to a list of items
    val itemsToDrop = inventory.container.itemsAsList + equipment.container.itemsAsList
    // for each item in the list, spawn it on the floor for the killer
    for (item in itemsToDrop)
        World.spawnFloorItem(item, killer, position)
    // reset and respawn the player
    sendMessage("Oh dear, you have died.")
    temporaryAttributes["admin_hp_event"] = false
    music.playJingle(90)
    reset()
    blockIncomingHits(2)
    setAnimation(Animation.STOP)
    variables.setSkull(false)
    val area = getArea()
    val plugin = if (area is DeathPlugin) area as DeathPlugin else null
    val respawnLocation = plugin?.getRespawnLocation()
    setLocation(respawnLocation ?: respawnPoint.location)
}

fun Player.canOverrideSecurity() = this.username.equals("jesse", ignoreCase = true)
fun Player.fadeRelocate(
    animation: Animation,
    location: Location,
    ticks: Int = 2
) {
    this.animation = animation
    this.lock(ticks)
    WorldTasksManager.schedule(ticks) {
        this.teleport(location)
    }
    FadeScreen(this).fade(ticks + 1)
}
