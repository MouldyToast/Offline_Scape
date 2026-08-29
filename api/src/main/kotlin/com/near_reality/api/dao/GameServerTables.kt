package com.near_reality.api.dao

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.transactions.transaction

object GlobalAttributesTable : IdTable<String>("global_attributes") {
    override val id = varchar("key", 255).entityId() // primary key
    val value = text("value")

    override val primaryKey = PrimaryKey(id)
}

class GlobalAttribute(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, GlobalAttribute>(GlobalAttributesTable)

    var value by GlobalAttributesTable.value
}

object DaoGlobalAttributes {
    private val json = Json { prettyPrint = true; encodeDefaults = true; ignoreUnknownKeys = true }

    fun get(key: String): GlobalValue? = transaction {
        GlobalAttribute.findById(key)?.let {
            try {
                json.decodeFromString<GlobalValue>(it.value)
            } catch (e: Exception) {
                println("Failed to decode global value for key=$key: ${e.message}")
                null
            }
        }
    }

    fun set(key: String, value: GlobalValue?) = transaction {
        if (value == null) {
            GlobalAttribute.findById(key)?.delete()
        } else {
            val encoded = json.encodeToString(value)
            val attr = GlobalAttribute.findById(key) ?: GlobalAttribute.new(key) {}
            attr.value = encoded
        }
    }


}
