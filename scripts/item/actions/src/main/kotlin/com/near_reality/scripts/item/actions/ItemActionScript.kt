package com.near_reality.scripts.item.actions

import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import it.unimi.dsi.fastutil.ints.IntArraySet
import it.unimi.dsi.fastutil.ints.IntSet
import kotlin.script.experimental.annotations.KotlinScript
import com.near_reality.scripts.item.actions.ItemDeathPlugin as KotlinItemDeathPlugin

/**
 * @author Jire
 */
@KotlinScript(
    "Item Action Script",
    fileExtension = "itemaction.kts",
    compilationConfiguration = ItemActionCompilation::class
)
abstract class ItemActionScript : ItemPlugin() {

    private val items: IntSet = IntArraySet()

    fun items(vararg ids: Int) {
        for (id in ids) items.add(id)
    }

    fun items(ids: Collection<Int>) {
        items.addAll(ids)
    }

    override fun getItems(): IntArray = items.toIntArray()

    override fun handle() {
        // covered by `init` in this
    }

    operator fun String.invoke(handler: OptionHandler) = bind(this, handler)

    operator fun String.invoke(handler: BasicOptionHandler) = bind(this, handler)

    operator fun String.invoke(handle: ItemOptionHandler.() -> Unit) =
        invoke { player, item, container, slotId ->
            ItemOptionHandler(player, item, container, slotId).handle()
        }

    fun death(block: KotlinItemDeathPlugin.() -> Unit) {
        onDeath { player, item, protectedCount, deepWilderness, pvp ->
            KotlinItemDeathPlugin(player, item, protectedCount, deepWilderness, pvp).run {
                block()
                build()
            }
        }
    }

}