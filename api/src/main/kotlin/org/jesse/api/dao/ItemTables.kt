package org.jesse.api.dao

import org.jesse.api.model.ItemConfig
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

object ItemConfigs : LongIdTable("item_configs_v2") {
    val itemId = integer("item_id").uniqueIndex()
    val name = varchar("item_name", 128).index()
    val canTrade = bool("tradeable").default(true)
    val ecoValue = integer("custom_price").nullable()
    val generalStorePrice = integer("gen_store_price").nullable()
    val protectionValue = integer("protection_value").nullable()
}

class ItemConfigEntity(id: EntityID<Long>) : ModelEntity<ItemConfig>(id) {
    companion object : LongEntityClass<ItemConfigEntity>(ItemConfigs) {
        fun new(itemDetails: ItemConfig) = ItemConfigEntity.new {
            setFrom(itemDetails)
        }
        fun edit(itemDetails: ItemConfig) = ItemConfigEntity.findSingleByAndUpdate(ItemConfigs.itemId eq itemDetails.id) {
            it.setFrom(itemDetails)
        } ?: new(itemDetails)
    }
    var itemId by ItemConfigs.itemId
    var name by ItemConfigs.name
    var canTrade by ItemConfigs.canTrade
    var ecoValue by ItemConfigs.ecoValue
    var generalStorePrice by ItemConfigs.generalStorePrice
    var protectionValue by ItemConfigs.protectionValue

    fun setFrom(itemDetails: ItemConfig) {
        itemId = itemDetails.id
        name = itemDetails.name
        canTrade = itemDetails.tradeable
        ecoValue = itemDetails.ecoValue
        generalStorePrice = itemDetails.generalStore
        protectionValue = itemDetails.protectionValue
    }

    override fun toModel(): ItemConfig {
        return ItemConfig(
            id = itemId,
            name = name,
            tradeable = canTrade,
            ecoValue = ecoValue,
            generalStore = generalStorePrice,
            protectionValue = protectionValue
        )
    }
}
