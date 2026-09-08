package com.near_reality.cache_tool.packing.custom

import com.near_reality.cache_tool.packing.JagexColor
import com.zenyte.game.item.ids.*
import mgi.types.config.ObjectDefinitions
import mgi.types.config.items.ItemDefinitions
import mgi.types.config.npcs.NPCDefinitions
import mgi.types.worldmap.MapElementDefinitions
import java.util.*

fun NPCDefinitions.NPCDefinitionsBuilder.movementAnimations(anim: Int): NPCDefinitions.NPCDefinitionsBuilder = this
    .walkAnimation(anim)
    .standAnimation(anim)
    .rotate90Animation(anim)
    .rotate180Animation(anim)
    .rotate270Animation(anim)
    .rotateLeftAnimation(anim)
    .rotateRightAnimation(anim)

fun NPCDefinitions.NPCDefinitionsBuilder.movementAnimationsWithIdle(move: Int, idle: Int): NPCDefinitions.NPCDefinitionsBuilder = this
    .walkAnimation(move)
    .standAnimation(idle)
    .rotate90Animation(move)
    .rotate180Animation(move)
    .rotate270Animation(move)
    .rotateLeftAnimation(move)
    .rotateRightAnimation(move)


fun NPCDefinitions.NPCDefinitionsBuilder.combatOptions(): NPCDefinitions.NPCDefinitionsBuilder = this
    .options(arrayOf(null, "Attack", null, null, null))
    .filteredOptions(arrayOf(null, "Attack", null, null, null))
    .filterFlag(0)

fun NPCDefinitions.NPCDefinitionsBuilder.makePet(): NPCDefinitions.NPCDefinitionsBuilder = this
    .options(arrayOf("Talk-to", null, "Pick-up", null, null))
    .filteredOptions(arrayOf("Talk-to", null, "Pick-up", null, null))
    .filterFlag(0)
    .isFamiliar(true)
    .clickable(true)
    .clippedMovement(true)
    .minimapVisible(false)
    .direction(32)
    .combatLevel(0)
    .visible(false)

fun NPCDefinitions.NPCDefinitionsBuilder.makeRemnPetWithOps(op2: String? = null, op4: String? = null): NPCDefinitions.NPCDefinitionsBuilder = this
    .options(arrayOf("Talk-to", op2, "Pick-up", op4, null))
    .filteredOptions(arrayOf("Talk-to", op2, "Pick-up", op4, null))
    .filterFlag(0)
    .isFamiliar(true)
    .clickable(true)
    .clippedMovement(true)
    .minimapVisible(false)
    .direction(32)
    .combatLevel(0)
    .visible(false)

fun NPCDefinitions.NPCDefinitionsBuilder.optionList(vararg npcOptions: String?): NPCDefinitions.NPCDefinitionsBuilder = this
    .options(npcOptions.take(5).toTypedArray())
    .filteredOptions(npcOptions.take(5).toTypedArray())
    .filterFlag(0)

fun NPCDefinitions.NPCDefinitionsBuilder.makePassive(clip: Boolean = true): NPCDefinitions.NPCDefinitionsBuilder = this
    .isFamiliar(false)
    .clickable(true)
    .clippedMovement(clip)
    .minimapVisible(true)
    .direction(32)
    .visible(false)

fun NPCDefinitions.NPCDefinitionsBuilder.combatTarget(level: Int, direction: Int = 32): NPCDefinitions.NPCDefinitionsBuilder = this
    .clickable(true)
    .visible(false)
    .clippedMovement(true)
    .isFamiliar(false)
    .minimapVisible(true)
    .direction(direction)
    .combatLevel(level)


fun NPCDefinitions.NPCDefinitionsBuilder.named(name: String): NPCDefinitions.NPCDefinitionsBuilder = this
    .name(name)
    .lowercaseName(name.lowercase(Locale.getDefault()))

fun NPCDefinitions.NPCDefinitionsBuilder.rescale(resizeBase: Int): NPCDefinitions.NPCDefinitionsBuilder = this
    .resizeX(resizeBase)
    .resizeY(resizeBase)

fun NPCDefinitions.NPCDefinitionsBuilder.initialize(id: Int, size: Int, vararg models: Int): NPCDefinitions.NPCDefinitionsBuilder = this
    .id(id)
    .size(size)
    .models(models)

fun NPCDefinitions.NPCDefinitionsBuilder.packNew(): NPCDefinitions.NPCDefinitionsBuilder = this
    .build()
    .pack().run { return this@packNew }

fun Int.cloneObject(original: Int) = ObjectDefinitions(this).copy(original).toBuilder()
fun Int.newObject() = ObjectDefinitions(this).toBuilder()

fun ObjectDefinitions.ObjectDefinitionsBuilder.packNew() = this.build().pack()
fun ObjectDefinitions.ObjectDefinitionsBuilder.modelList(vararg models: Int) = this.models(models)
fun ObjectDefinitions.ObjectDefinitionsBuilder.modelScale(x: Int, y: Int, z: Int) = this.modelSizeX(x).modelSizeY(y).modelSizeHeight(z)
fun ObjectDefinitions.ObjectDefinitionsBuilder.sizeScale(x: Int, y: Int) = this.sizeX(x).sizeY(y)
fun ObjectDefinitions.ObjectDefinitionsBuilder.optionList(vararg options: String?): ObjectDefinitions.ObjectDefinitionsBuilder = this
    .options(options.take(10).toTypedArray())
fun ObjectDefinitions.ObjectDefinitionsBuilder.nullOps(): ObjectDefinitions.ObjectDefinitionsBuilder = this
    .options(arrayOfNulls(10))
fun ObjectDefinitions.ObjectDefinitionsBuilder.changeOption(index: Int, option: String?): ObjectDefinitions.ObjectDefinitionsBuilder = optionAt(index, option)
fun ObjectDefinitions.ObjectDefinitionsBuilder.named(name: String): ObjectDefinitions.ObjectDefinitionsBuilder = name(name)


fun ItemDefinitions.ItemDefinitionsBuilder.packNew() = this.build().pack()
fun ItemDefinitions.ItemDefinitionsBuilder.createPlaceholder(placeholder: Int) = run {
    val temp = this.build()
    ItemDefinitions.get(16551).toBuilder()
        .id(placeholder)
        .inventoryModelId(temp.inventoryModelId)
        .placeholderId(temp.id)
        .rotateInv(temp.modelPitch, temp.modelRoll, temp.modelYaw)
        .offsetInv(temp.offsetX, temp.offsetY)
        .zoom(temp.zoom)
        .placeholderTemplate(14401)
        .packNew()

    this.placeholderId(placeholder)
}
fun ItemDefinitions.createWhip(newId: Int, newName: String, newColor: Short) = this.toBuilder().id(newId).name(newName).originalColours(shortArrayOf(528)).replacementColours(shortArrayOf(newColor))
fun ItemDefinitions.ItemDefinitionsBuilder.clearNote() = this.notedId(-1).notedTemplate(-1)
fun ItemDefinitions.ItemDefinitionsBuilder.clearPlaceholder() = this.placeholderId(-1).placeholderTemplate(-1)
fun ItemDefinitions.ItemDefinitionsBuilder.rotateInv(pitch: Int, roll: Int, yaw: Int) = this.modelPitch(pitch).modelRoll(roll).modelYaw(yaw)
fun ItemDefinitions.ItemDefinitionsBuilder.offsetInv(offsetX: Int = 0, offsetY: Int = 0) = this.offsetX(offsetX).offsetY(offsetY)
fun ItemDefinitions.ItemDefinitionsBuilder.offsetEq(male: Int = 0, female: Int = 0) = this.maleOffset(male).femaleOffset(female)
fun ItemDefinitions.ItemDefinitionsBuilder.offsetFemaleEq(offsetX: Int = 0, offsetY: Int = 0, offsetZ: Int = 0) = this.offsetX(offsetX).offsetY(offsetY)
fun ItemDefinitions.ItemDefinitionsBuilder.invOps(vararg options: String?) = this.inventoryOptions(options.take(5).toTypedArray())
fun ItemDefinitions.ItemDefinitionsBuilder.groundOps(vararg options: String?) = this.groundOptions(options.take(5).toTypedArray())
fun ItemDefinitions.ItemDefinitionsBuilder.weaponOps() = this.invOps("Wield", null, null, null, "Drop").groundOps(null, null, "Take", null, null)
fun ItemDefinitions.ItemDefinitionsBuilder.equipmentOps() = this.invOps("Wear", null, null, null, "Drop").groundOps(null, null, "Take", null, null)
fun ItemDefinitions.ItemDefinitionsBuilder.petOps() = this.invOps(null, null, null, null, "Drop").groundOps(null, null, "Take", null, null)
fun ItemDefinitions.ItemDefinitionsBuilder.tradable() = this.grandExchange(true)
fun ItemDefinitions.ItemDefinitionsBuilder.named(name: String) = this.name(name)
fun ItemDefinitions.ItemDefinitionsBuilder.stackable() = this.isStackable(1)

fun ItemDefinitions.ItemDefinitionsBuilder.models(
    inventory: Int,
    primaryMale: Int = -1,
    primaryFemale: Int = primaryMale,
    secondaryMale: Int = 1.unaryMinus(),
    secondaryFemale: Int = 1.unaryMinus()
) = this.inventoryModelId(inventory)
    .primaryMaleModel(primaryMale)
    .secondaryMaleModel(secondaryMale)
    .primaryFemaleModel(primaryFemale)
    .secondaryFemaleModel(secondaryFemale)

fun Any.whipDefinition() = ItemDefinitions.get(ABYSSAL_WHIP)
fun Int.newItem() = ItemDefinitions(this).toBuilder()
fun Int.cloneThisTo(newId: Int) = ItemDefinitions.get(this).toBuilder().id(newId)

fun Int.createSmallMapLabel(label: String) = MapElementDefinitions.get(444)!!.toBuilder()
    .id(this)
    .text(label)
    .textSize(0)
    .build()
    .pack()

fun Int.createMediumMapLabel(label: String) = MapElementDefinitions.get(444)!!.toBuilder()
    .id(this)
    .text(label)
    .textSize(1)
    .build()
    .pack()

fun Int.createLargeMapLabel(label: String) = MapElementDefinitions.get(143)!!.toBuilder()
    .id(this)
    .text(label)
    .textSize(2)
    .build()
    .pack()

fun Int.toHSL() = JagexColor.rgbToHSL(this, JagexColor.BRIGHTNESS_HIGH)
