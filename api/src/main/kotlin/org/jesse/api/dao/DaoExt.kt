package org.jesse.api.dao

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun daoIntListAttribute(key: String, default: List<Int> = emptyList()) = object : ReadWriteProperty<Any?, List<Int>> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): List<Int> {
        return (DaoGlobalAttributes.get(key) as? GlobalValue.IntListValue)?.value ?: default
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: List<Int>) {
        DaoGlobalAttributes.set(key, GlobalValue.IntListValue(value))
    }
}

fun daoStringListAttribute(key: String, default: List<String> = emptyList()) = object :
    ReadWriteProperty<Any?, List<String>> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): List<String> {
        return (DaoGlobalAttributes.get(key) as? GlobalValue.StringListValue)?.value ?: default
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: List<String>) {
        DaoGlobalAttributes.set(key, GlobalValue.StringListValue(value))
    }
}

fun daoBooleanAttribute(key: String, default: Boolean = false) = object : ReadWriteProperty<Any?, Boolean> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean {
        return (DaoGlobalAttributes.get(key) as? GlobalValue.BooleanValue)?.value ?: default
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
        DaoGlobalAttributes.set(key, GlobalValue.BooleanValue(value))
    }
}

fun daoIntAttribute(key: String, default: Int = 0) = object : ReadWriteProperty<Any?, Int> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return (DaoGlobalAttributes.get(key) as? GlobalValue.IntValue)?.value ?: default
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        DaoGlobalAttributes.set(key, GlobalValue.IntValue(value))
    }
}
