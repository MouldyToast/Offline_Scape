package org.jire.wmpacker

import org.jesse.game.world.entity.Location
import mgi.types.worldmap.MapElement
import mgi.types.worldmap.MapElementDefinitions
import mgi.types.worldmap.WorldMapDefinitions
import java.util.concurrent.ExecutorService
import java.util.concurrent.ForkJoinPool
import java.util.function.Consumer

/**
 * @author Jire
 */
object WorldMapAreas {

    const val DEFAULT_LABEL_COLOUR = 16777215
    const val DEFAULT_LABEL_TEXT_SIZE = 1
    const val DEFAULT_LABEL_HORIZONTAL_ALIGNMENT = 1
    const val DEFAULT_LABEL_VERTICAL_ALIGNMENT = 1
    const val DEFAULT_LABEL_TOOLTIP_ID = 1129

    @JvmStatic
    @JvmOverloads
    fun WorldMapDefinitions.region(regionID: Int, plane: Int = 0) = update(regionID, plane)

    @JvmStatic
    inline fun WorldMapDefinitions.addElement(
        text: String,
        location: Location,
        colour: Int = DEFAULT_LABEL_COLOUR,
        textSize: Int = DEFAULT_LABEL_TEXT_SIZE,
        horizontalAlignment: Int = DEFAULT_LABEL_HORIZONTAL_ALIGNMENT,
        verticalAlignment: Int = DEFAULT_LABEL_VERTICAL_ALIGNMENT,
        tooltipId: Int = DEFAULT_LABEL_TOOLTIP_ID,
        customize: MapElement.() -> Unit
    ) {
        val newID = elements.maxOf { it.id } + 1
        MapElementDefinitions().apply {
            id = newID
            spriteId = -1
            opcode2 = -1
            this.text = text
            this.colour = colour
            this.textSize = textSize
            options = arrayOfNulls(5)
            optionName = null
            field3312 = null
            field3313 = 2147483647
            field3314 = 2147483647
            field3315 = -2147483648
            field3316 = -2147483648
            this.horizontalAlignment = horizontalAlignment
            this.verticalAlignment = verticalAlignment
            field3307 = null
            field3320 = null
            this.tooltipId = tooltipId
            pack()
        }
        val element = MapElement(false, newID, location)
        element.customize()
        elements.add(element)
    }

    @JvmStatic
    @JvmOverloads
    fun WorldMapDefinitions.addElement(
        text: String,
        location: Location,
        colour: Int = DEFAULT_LABEL_COLOUR,
        textSize: Int = DEFAULT_LABEL_TEXT_SIZE,
        horizontalAlignment: Int = DEFAULT_LABEL_HORIZONTAL_ALIGNMENT,
        verticalAlignment: Int = DEFAULT_LABEL_VERTICAL_ALIGNMENT,
        tooltipId: Int = DEFAULT_LABEL_TOOLTIP_ID
    ) = addElement(
        text, location,
        colour, textSize, horizontalAlignment, verticalAlignment, tooltipId
    ) {}

    @JvmStatic
    @JvmOverloads
    fun WorldMapDefinitions.addElement(
        text: String,
        location: Location,
        colour: Int = DEFAULT_LABEL_COLOUR,
        textSize: Int = DEFAULT_LABEL_TEXT_SIZE,
        horizontalAlignment: Int = DEFAULT_LABEL_HORIZONTAL_ALIGNMENT,
        verticalAlignment: Int = DEFAULT_LABEL_VERTICAL_ALIGNMENT,
        tooltipId: Int = DEFAULT_LABEL_TOOLTIP_ID,
        customize: Consumer<MapElement>
    ) = addElement(text, location, colour, textSize, horizontalAlignment, verticalAlignment, tooltipId) {
        customize.accept(this)
    }

    @JvmStatic
    inline fun WorldMapDefinitions.addElement(
        text: String,
        x: Int, y: Int, z: Int = 0,
        colour: Int = DEFAULT_LABEL_COLOUR,
        textSize: Int = DEFAULT_LABEL_TEXT_SIZE,
        horizontalAlignment: Int = DEFAULT_LABEL_HORIZONTAL_ALIGNMENT,
        verticalAlignment: Int = DEFAULT_LABEL_VERTICAL_ALIGNMENT,
        tooltipId: Int = DEFAULT_LABEL_TOOLTIP_ID,
        customize: MapElement.() -> Unit
    ) = addElement(
        text, Location(x, y, z),
        colour, textSize, horizontalAlignment, verticalAlignment, tooltipId,
        customize
    )

    @JvmStatic
    @JvmOverloads
    fun WorldMapDefinitions.addElement(
        text: String,
        x: Int, y: Int, z: Int = 0,
        colour: Int = DEFAULT_LABEL_COLOUR,
        textSize: Int = DEFAULT_LABEL_TEXT_SIZE,
        horizontalAlignment: Int = DEFAULT_LABEL_HORIZONTAL_ALIGNMENT,
        verticalAlignment: Int = DEFAULT_LABEL_VERTICAL_ALIGNMENT,
        tooltipId: Int = DEFAULT_LABEL_TOOLTIP_ID,
    ) = addElement(
        text, Location(x, y, z),
        colour, textSize, horizontalAlignment, verticalAlignment, tooltipId
    ) {}

    @JvmStatic
    @JvmOverloads
    fun WorldMapDefinitions.addElement(
        text: String,
        x: Int, y: Int, z: Int = 0,
        colour: Int = DEFAULT_LABEL_COLOUR,
        textSize: Int = DEFAULT_LABEL_TEXT_SIZE,
        horizontalAlignment: Int = DEFAULT_LABEL_HORIZONTAL_ALIGNMENT,
        verticalAlignment: Int = DEFAULT_LABEL_VERTICAL_ALIGNMENT,
        tooltipId: Int = DEFAULT_LABEL_TOOLTIP_ID,
        customize: Consumer<MapElement>
    ) = addElement(text, x, y, z, colour, textSize, horizontalAlignment, verticalAlignment, tooltipId) {
        customize.accept(this)
    }

    @JvmStatic
    inline fun WorldMapDefinitions.changeElement(text: String, change: MapElement.() -> Unit) {
        elements.first { MapElementDefinitions.get(it.id)!!.text == text }.change()
    }

    @JvmStatic
    fun WorldMapDefinitions.changeElement(text: String, change: Consumer<MapElement>) =
        changeElement(text) { change.accept(this) }

    @JvmStatic
    fun WorldMapDefinitions.removeElement(text: String) {
        elements.removeIf { MapElementDefinitions.get(it.id)!!.text == text }
    }

    @JvmStatic
    inline fun changeArea(
        service: ExecutorService,

        areaName: String,
        apply: WorldMapDefinitions.() -> Unit
    ) {
        val def = WorldMapDefinitions.decode(areaName)
        def.apply()
        def.encode(service, areaName)
    }

    @JvmStatic
    @JvmOverloads
    fun changeArea(
        service: ExecutorService = ForkJoinPool.commonPool(),

        areaName: String,
        apply: Consumer<WorldMapDefinitions>
    ) = changeArea(service, areaName) { apply.accept(this) }

}
