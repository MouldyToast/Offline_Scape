package com.near_reality.cache_tool.packing.custom

import com.near_reality.cache_tool.packing.assetsBase
import com.zenyte.ContentConstants
import com.zenyte.game.item.ids.*
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import mgi.tools.parser.TypeParser
import mgi.types.config.InventoryDefinitions
import mgi.types.config.items.ItemDefinitions
import mgi.types.config.npcs.NPCDefinitions
import java.io.File


/**
 * Handles the packing of custom Near Reality items into the cache.
 *
 * @author Stan van der Bend
 */
object NearRealityCustomItemPacker {

    @JvmStatic
    fun pack() = assetsBase("assets/osnr/custom_items/") {
        defaultModels()

        TypeParser.parse(File(folder("item_config")))

        ItemDefinitions.get(RING_OF_LEVITATION).apply {
            name = "Ring of levitation"
            lowercaseName = name.lowercase()
            isGrandExchange = true
            pack()
        }
        ItemDefinitions.get(20608).apply {
            this.name = "Chaos Key (active)"
            this.notedId = -1
            this.notedTemplate = -1
            this.placeholderId = -1
            this.placeholderTemplate = -1
            pack()
        }
        ItemDefinitions.get(20608).apply {
            this.id = 20609
            this.name = "Chaos Key"
            this.notedId = -1
            this.notedTemplate = -1
            this.placeholderId = -1
            this.placeholderTemplate = -1
            pack()
        }
        ItemDefinitions.get(DIVINE_RUNE_POUCH).apply {
            inventoryOptions[4] = "Extra Slot"
            pack()
        }
        NPCDefinitions.get(12166).apply {
            options[1] = "Attack"
            pack()
        }
        val barrowsSets = listOf(TORAGS_ARMOUR_SET, DHAROKS_ARMOUR_SET, KARILS_ARMOUR_SET, AHRIMS_ARMOUR_SET, GUTHANS_ARMOUR_SET, VERACS_ARMOUR_SET)
        for(id in barrowsSets) {
            ItemDefinitions.get(id).apply{
                inventoryOptions[1] = "Unpack"
                pack()
            }
        }
        InventoryDefinitions.get(ContainerType.COLLECTION_LOG.id)?.apply {
            size = 2500
            pack()
        }
        makeTradeable(LARRANS_KEY)
        makeTradeable(26649) // skis
        makeTradeable(SLED_4084, SLED_25282)
        makeTradeable(SKELETON_MASK, SKELETON_BOOTS, SKELETON_GLOVES, SKELETON_SHIRT, SKELETON_LEGGINGS)
        makeTradeable(ATTACKER_ICON, DEFENDER_ICON, COLLECTOR_ICON, HEALER_ICON)
        makeTradeable(DHAROKS_HELM, DHAROKS_PLATEBODY, DHAROKS_PLATELEGS, DHAROKS_GREATAXE)
        makeTradeable(AHRIMS_HOOD, AHRIMS_ROBETOP, AHRIMS_ROBESKIRT, AHRIMS_STAFF)
        makeTradeable(ECHO_AHRIMS_HOOD, ECHO_AHRIMS_ROBETOP, ECHO_AHRIMS_ROBESKIRT, ECHO_AHRIMS_STAFF)
        makeTradeable(GUTHANS_HELM, GUTHANS_PLATEBODY, GUTHANS_CHAINSKIRT, GUTHANS_WARSPEAR)
        makeTradeable(KARILS_COIF, KARILS_LEATHERTOP, KARILS_LEATHERSKIRT, KARILS_CROSSBOW)
        makeTradeable(VERACS_HELM, VERACS_PLATESKIRT, VERACS_BRASSARD, VERACS_FLAIL)
        makeTradeable(TORAGS_HELM, TORAGS_PLATEBODY, TORAGS_PLATELEGS, TORAGS_HAMMERS)
    }

    private fun makeTradeable(vararg itemIds: Int) = itemIds.forEach { itemId ->
        var def = ItemDefinitions.get(itemId)
        if (def == null) return
        def.apply {
            isGrandExchange = true
            pack()
        }
    }



    @JvmStatic
    fun main(args: Array<String>) {
        val customItemIds = mutableListOf<String>()
        val placeHolders = mutableListOf<String>()

        var id = 32142
        val parts = listOf("platebody", "platelegs")
        val inheritMap = mapOf(
            "platebody" to BANDOS_CHESTPLATE,
            "platelegs" to BANDOS_TASSETS,
        )
        val renameMap = mapOf(
            "platebody" to "Bandos chestplate (or)",
            "platelegs" to "Bandos tassets (or)"
        )
        for (part in parts) {
            val name = renameMap[part]!!
            val drop = getModelId("bandos_${part}_drop")
            val maleEquip = getModelId("bandos_${part}_male_equip")
            val femaleEquip = getModelId("bandos_${part}_female_equip")
            println(buildString {
                appendLine("#${ContentConstants.SERVER_NAME} $name")
                appendLine("[[item]]")
                appendLine("inherit=${inheritMap[part]!!}")
                appendLine("id=$id")
                appendLine("name=\"$name\"")
                appendLine("invmodel=$drop")
                appendLine("primarymalemodel=$maleEquip")
                appendLine("primaryfemalemodel=$femaleEquip")
                appendLine("placeholderid=${id + 1000}")
                appendLine("stackable=0")
                appendLine("notedid=${id + 1}")
            })
            customItemIds += buildString {
                appendLine(
                    "\tpublic static final int ${
                        name.replace("'", "").uppercase().replace(" ", "_")
                    } = $id;"
                )
            }
            placeHolders += buildString {
                appendLine("#${ContentConstants.SERVER_NAME} $name (placeholder)")
                appendLine("[[item]]")
                appendLine("inherit=${inheritMap[part]!!}")
                appendLine("id=${id + 1000}")
                appendLine("name=\"$name\"")
                appendLine("invmodel=$drop")
                appendLine("placeholderid=${id}")
                appendLine("placeholdertemplate=14401")
            }
            id++
            println(buildString {
                appendLine("#${ContentConstants.SERVER_NAME} $name (noted)")
                appendLine("[[item]]")
                appendLine("inherit=${inheritMap[part]!! + 1}")
                appendLine("id=$id")
                appendLine("name=\"$name\"")
                appendLine("invmodel=$drop")
                appendLine("notedid=${id - 1}")
            })
            id++
        }
        for (placeHolder in placeHolders)
            println(placeHolder)
        for (customItemId in customItemIds)
            println(customItemId)
    }

    private fun getModelId(name: String) = (map[name] as CustomDefinition.Model).modelId

    sealed class CustomDefinition {

        open class Model(val modelId: Int) : CustomDefinition()

        class Recolor(val recolorMap: Map<Int, Int>) : CustomDefinition()
    }

}
