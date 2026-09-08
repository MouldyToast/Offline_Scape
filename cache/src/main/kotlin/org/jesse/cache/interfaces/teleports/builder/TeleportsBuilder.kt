package org.jesse.cache.interfaces.teleports.builder

import org.jesse.cache.interfaces.teleports.Category
import org.jesse.cache.interfaces.teleports.Teleports
import it.unimi.dsi.fastutil.objects.ObjectArrayList

/**
 * @author Jire
 */
internal class TeleportsBuilder : Teleports {

    override val categories: MutableList<CategoryBuilder> = ObjectArrayList()

    operator fun String.invoke(enumID: Int, build: CategoryBuilder.() -> Unit): Category {
        val builder = CategoryBuilder(this, enumID, nextCategoryId++)
        builder.build()

        categories.add(builder)
        return builder
    }

    companion object {

        var nextStructID = 10000
        var nextCategoryId = 0

        fun teleports(build: TeleportsBuilder.() -> Unit): Teleports {
            val builder = TeleportsBuilder()
            builder.build()

            return builder
        }

    }

}