package com.near_reality.cache_tool.packing.custom

import com.near_reality.cache.DBTableIndexDefinitionGroup
import com.near_reality.game.content.universalshop.UnivShopCategory
import com.near_reality.game.content.universalshop.UniversalShopTable
import mgi.types.config.DBRowDefinition
import mgi.types.config.db.Column
import mgi.types.config.db.ColumnValue
import net.runelite.cache.util.ScriptVarType
import kotlin.enums.EnumEntries

object UniversalShopPacker {

    fun postPack() {
        for(table in UniversalShopTable.tables.sortedBy { it.category.dbTable }) {
            table.toDbRowDefinitions().createIndexes(table.category.dbTable)
        }
        UnivShopCategory.entries.toDbRowDefinitions().createIndexes(1002)
    }
}

fun List<DBRowDefinition>.createIndexes(dbTable: Int) {
    val group = DBTableIndexDefinitionGroup()
    group.tableId = dbTable
    group.masterIndex.buildMaster(this)
    group.columnIndexes.addAll(group.masterIndex.buildColumns(this, false, 0, 1))
    group.pack()
}
var dbrowId = 5000
fun UniversalShopTable.toDbRowDefinitions(): List<DBRowDefinition> {
    val tableItems = mutableListOf<DBRowDefinition>()
    this.items.forEachIndexed { idx, item ->
        val def = DBRowDefinition(dbrowId)
        dbrowId += 1

        def.tableId = category.dbTable

        def.columnCount = 7

        for(i in 0..4) {
            def.typeCountMap[i] = 1
            def.fieldCountMap[i] = 1
        }

        def.types = arrayOfNulls(7)
        def.types[0] = arrayOf(ScriptVarType.INTEGER)
        def.types[1] = arrayOf(ScriptVarType.INTEGER)
        def.types[2] = arrayOf(ScriptVarType.OBJ)
        def.types[3] = arrayOf(ScriptVarType.INTEGER)
        def.types[4] = arrayOf(ScriptVarType.INTEGER)


        def.columns[0] = Column(0).withValue(ColumnValue(ScriptVarType.INTEGER, idx))
        def.columns[1] = Column(1).withValue(ColumnValue(ScriptVarType.INTEGER, this.category.index))
        def.columns[2] = Column(2).withValue(ColumnValue(ScriptVarType.OBJ, item.id))
        def.columns[3] = Column(3).withValue(ColumnValue(ScriptVarType.INTEGER, item.id))
        def.columns[4] = Column(4).withValue(ColumnValue(ScriptVarType.INTEGER, item.buyPrice))

        def.pack()
        tableItems.add(def)
    }
    return tableItems
}

fun EnumEntries<UnivShopCategory>.toDbRowDefinitions() : List<DBRowDefinition> {
    val tableItems = mutableListOf<DBRowDefinition>()

    this.forEachIndexed { idx, category ->
        val def = DBRowDefinition(dbrowId)
        dbrowId += 1
        def.tableId = 1002

        def.columnCount = 6

        for(i in 0..5) {
            def.typeCountMap[i] = 1
            def.fieldCountMap[i] = 1
        }

        def.types = arrayOfNulls(6)
        def.types[0] = arrayOf(ScriptVarType.INTEGER)
        def.types[1] = arrayOf(ScriptVarType.INTEGER)
        def.types[2] = arrayOf(ScriptVarType.STRING)
        def.types[3] = arrayOf(ScriptVarType.OBJ)
        def.types[4] = arrayOf(ScriptVarType.BOOLEAN)
        def.types[5] = arrayOf(ScriptVarType.BOOLEAN)


        def.columns[0] = Column(0).withValue(ColumnValue(ScriptVarType.INTEGER, idx))
        def.columns[1] = Column(1).withValue(ColumnValue(ScriptVarType.INTEGER, category.index))
        def.columns[2] = Column(2).withValue(ColumnValue(ScriptVarType.STRING, category.prettyName))
        def.columns[3] = Column(3).withValue(ColumnValue(ScriptVarType.OBJ, category.itemId))
        def.columns[4] = Column(4).withValue(ColumnValue(ScriptVarType.BOOLEAN, 1))
        def.columns[5] = Column(4).withValue(ColumnValue(ScriptVarType.BOOLEAN, 1))
        def.pack()
        tableItems.add(def)
    }
    return tableItems
}