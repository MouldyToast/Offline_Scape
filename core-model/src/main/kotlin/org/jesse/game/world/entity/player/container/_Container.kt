package org.jesse.game.world.entity.player.container

import org.jesse.game.item._Item
import org.jesse.game.world.entity.player.container.impl.ContainerType
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
open class _Container<T : _Item> @JvmOverloads constructor(
    @JvmField var type: ContainerType,
    @JvmField var policy: ContainerPolicy,
    @Transient @JvmField var items: Int2ObjectLinkedOpenHashMap<T> = Int2ObjectLinkedOpenHashMap()
) {

    open fun getType(): ContainerType {
        return type
    }

    open fun setType(type: ContainerType) {
        this.type = type
    }

    open fun getPolicy(): ContainerPolicy {
        return policy
    }

    open fun setPolicy(policy: ContainerPolicy) {
        this.policy = policy
    }

    open fun getItems(): Int2ObjectLinkedOpenHashMap<T> {
        return items
    }

    open fun setItems(items: Int2ObjectLinkedOpenHashMap<T>) {
        this.items = items
    }
}
