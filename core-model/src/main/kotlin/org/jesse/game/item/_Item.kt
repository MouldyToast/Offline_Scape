package org.jesse.game.item

import kotlinx.serialization.Serializable
import java.util.Objects

@Serializable
open class _Item(@JvmField var id: Int, @JvmField var amount: Int) {

    @kotlinx.serialization.Transient
    @JvmField
    var attributes: MutableMap<String, Any>? = null

    open fun getId(): Int {
        return id
    }

    open fun getAmount(): Int {
        return amount
    }

    override fun equals(other: Any?): Boolean {
        if (other !is _Item) {
            return false
        }
        return other.getId() == id && other.getAmount() == amount && Objects.equals(other.getAttributes(), attributes)
    }

    open fun getAttributes(): MutableMap<String, Any>? {
        return attributes
    }

    open fun setAttributes(attributes: MutableMap<String, Any>?) {
        this.attributes = attributes
    }
}
