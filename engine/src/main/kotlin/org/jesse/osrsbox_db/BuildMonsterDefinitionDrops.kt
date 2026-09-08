package org.jesse.osrsbox_db

import org.jesse.game.item.ids.*
import org.jesse.game.world.entity.npc.drop.matrix.Drop
import org.jesse.game.world.entity.npc.drop.matrix.NPCDrops
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import mgi.types.config.items.ItemDefinitions
import kotlin.math.roundToInt

fun MonsterDefinition.buildFromTable(table: List<MonsterDrop>) {
    val oldDrops: ObjectList<Drop> = ObjectArrayList(table.size)
    for (drop in table) {
        val itemID = drop.id ?: continue
        val rarity = drop.rarity?.toDoubleOrNull() ?: continue
        val quantityBase = drop.quantity ?: continue

        val requirements = drop.dropRequirements
        if (requirements != null) {
            /* ignore all drops with requirements, like quests etc. */
            continue
        }

        val noted = drop.noted

        fun defineQuantity(quantityMin: Int, quantityMax: Int): Boolean {
            var actualItemID = itemID
            when (actualItemID) {
                LARRANS_KEY,
                BASILISK_BONE,
                BASILISK_BONE_7901,
                LOOTING_BAG,
                LOOTING_BAG_22586,
                BRIMSTONE_KEY,
                DARK_TOTEM_BASE,
                DARK_TOTEM_MIDDLE,
                DARK_TOTEM_TOP,
                ANCIENT_SHARD,
                    -> return false
            }
            val itemDef = ItemDefinitions.get(actualItemID) ?: return false
            if (itemDef.name.contains("Clue scroll", true)) {
                return false // should convert these in the actual drop area and remove the existing clue drop system.
            }
            if (noted) {
                if (itemDef.isNoted) return false
                val noteID = itemDef.notedId
                if (noteID >= 0 && ItemDefinitions.get(noteID) != null) {
                    actualItemID = noteID
                } else {
                    logger.warn("No note for {} ({})", itemID, itemDef)
                    return false
                }
            }

            val rate = if (rarity < 1)
                Drop.GUARANTEED_RATE / (1.0 / rarity).roundToInt() // gets our 1/rate
            else Drop.GUARANTEED_RATE
            val oldDrop = Drop(actualItemID, rate, quantityMin, quantityMax)
            return oldDrops.add(oldDrop)
        }

        fun defineQuantityBase(quantityBase: String) {
            when {
                quantityBase.contains(',') -> {
                    val quantities = quantityBase.split(",")
                    for (quantity in quantities) {
                        defineQuantityBase(quantity)
                    }
                }

                quantityBase.contains('-') -> {
                    val split = quantityBase.split("-")
                    defineQuantity(split[0].toInt(), split[1].toInt())
                }

                else -> {
                    val quantity = quantityBase.toInt()
                    defineQuantity(quantity, quantity)
                }
            }
        }

        defineQuantityBase(quantityBase)
    }

    if (oldDrops.isNotEmpty()) {
        val oldTable = NPCDrops.DropTable(id, 0, oldDrops.toTypedArray())
        NPCDrops.initDropTable(oldTable)
    }
}
