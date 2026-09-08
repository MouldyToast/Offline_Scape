package org.jesse.game.model.item.degrading

import org.jesse.game.item.Item
import org.jesse.game.model.item.degradableitems.DegradeType
import java.util.function.Function

interface Degradeable {

    val type: DegradeType
    val itemId: Int
    val nextId: Int
    val maximumCharges: Int
    val minimumCharges: Int

    val function: Function<Item, Array<Item>>?
}
