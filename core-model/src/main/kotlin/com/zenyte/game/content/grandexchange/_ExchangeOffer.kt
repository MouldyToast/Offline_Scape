package com.zenyte.game.content.grandexchange

import com.google.gson.annotations.Expose
import com.zenyte.game.item._Item
import com.zenyte.game.world.entity.player.container._Container
import kotlinx.serialization.Serializable

// Test Commit - @William Fuhrman
@Serializable
open class _ExchangeOffer<T : _Item, C : _Container<T>>(
    @field:Expose @JvmField var username: String,
    @field:Expose @JvmField var dbUsername: String,
    @field:Expose @JvmField val item: T,
    @field:Expose @JvmField var container: C,
    @field:Expose @JvmField val slot: Int,
    @field:Expose @JvmField val price: Int,
    @field:Expose @JvmField val type: ExchangeType
) {

    @field:Expose
    @JvmField
    var amount: Int = 0

    @field:Expose
    @JvmField
    protected var updated: Boolean = false

    @field:Expose
    @JvmField
    protected var aborted: Boolean = false

    @field:Expose
    @JvmField
    protected var cancelled: Boolean = false

    open fun getUsername(): String {
        return username
    }

    open fun getDbUsername(): String {
        return dbUsername
    }

    open fun getItem(): T {
        return item
    }

    open fun getSlot(): Int {
        return slot
    }

    open fun getPrice(): Int {
        return price
    }

    open fun getAmount(): Int {
        return amount
    }

    open fun setAmount(amount: Int) {
        this.amount = amount
    }

    open fun getType(): ExchangeType {
        return type
    }

    open var isUpdated: Boolean
        get() = updated
        set(updated) {
            this.updated = updated
        }

    open var isAborted: Boolean
        get() = aborted
        set(aborted) {
            this.aborted = aborted
        }

    open var isCancelled: Boolean
        get() = cancelled
        set(cancelled) {
            this.cancelled = cancelled
        }

    open fun getContainer(): C {
        return container
    }

    open fun setContainer(container: C) {
        this.container = container
    }

    open fun setUsername(username: String) {
        this.username = username
    }
}
