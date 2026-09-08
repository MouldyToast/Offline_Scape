package com.zenyte.server

/**
 * @author Kryeus / John J. Woloszyk
 */
open class Attributes {

    private val attributes = HashMap<String, Any?>()

    private val intMap = HashMap<String, Int>()

    private val doubleMap = HashMap<String, Double>()

    private val booleanMap = HashMap<String, Boolean>()

    private val longMap = HashMap<String, Long>()

    private val stringMap = HashMap<String, String?>()

    private val listMap = HashMap<String, List<*>?>()

    private val hashSetMap = HashMap<String, HashSet<*>?>()

    open fun get(key: Any?): Any? {
        return attributes[key]
    }

    open fun get(key: Any?, fail: Any?): Any? {
        return attributes[key] ?: fail
    }

    open fun set(key: String, value: Any?) {
        attributes.remove(key)

        attributes[key] = value
    }

    open fun contains(key: Any?): Boolean {
        return attributes.containsKey(key)
    }

    open fun remove(key: Any?) {
        attributes.remove(key)
    }

    open fun setInt(key: String, set: Int) {
        intMap[key] = set
    }

    open fun getIntAndIncr(key: String, n: Int): Int {
        val num = intMap.getOrDefault(key, 0)
        intMap[key] = num + n
        return num
    }

    open fun getIntAndDecr(key: String, n: Int): Int {
        val num = intMap.getOrDefault(key, 0)
        intMap[key] = num - n
        return num
    }

    open fun incrAndGetInt(key: String, n: Int): Int {
        val num = intMap.getOrDefault(key, 0)
        intMap[key] = num + n
        return getInt(key)
    }

    open fun decrAndGetInt(key: String, n: Int): Int {
        val num = intMap.getOrDefault(key, 0)
        intMap[key] = num - n
        return getInt(key)
    }

    open fun removeInt(key: String) {
        intMap.remove(key)
    }

    open fun getInt(key: String): Int {
        return intMap.getOrDefault(key, -1)
    }

    open fun getInt(key: String, fail: Int): Int {
        return intMap.getOrDefault(key, fail)
    }

    open fun containsInt(key: String): Boolean {
        return intMap.containsKey(key)
    }

    open fun setDouble(key: String, set: Double) {
        doubleMap[key] = set
    }

    open fun removeDouble(key: String) {
        doubleMap.remove(key)
    }

    open fun getDouble(key: String): Double {
        return doubleMap.getOrDefault(key, -1.0)
    }

    open fun getDouble(key: String, fail: Double): Double {
        return doubleMap.getOrDefault(key, fail)
    }

    open fun containsDouble(key: String): Boolean {
        return doubleMap.containsKey(key)
    }

    open fun setBoolean(key: String, set: Boolean) {
        booleanMap[key] = set
    }

    open fun flipBoolean(key: String): Boolean {
        if (getBoolean(key)) {
            setBoolean(key, false)
        } else {
            setBoolean(key, true)
        }

        return getBoolean(key)
    }

    open fun removeBoolean(key: String) {
        booleanMap.remove(key)
    }

    open fun getBoolean(key: String): Boolean {
        return booleanMap.getOrDefault(key, false)
    }

    open fun getBoolean(key: String, fail: Boolean): Boolean {
        return booleanMap.getOrDefault(key, fail)
    }

    open fun containsBoolean(key: String): Boolean {
        return booleanMap.containsKey(key)
    }

    open fun setLong(key: String, set: Long) {
        longMap[key] = set
    }

    open fun removeLong(key: String) {
        longMap.remove(key)
    }

    open fun getLong(key: String): Long {
        return longMap.getOrDefault(key, -1L)
    }

    open fun getLong(key: String, fail: Long): Long {
        return longMap.getOrDefault(key, fail)
    }

    open fun containsLong(key: String): Boolean {
        return longMap.containsKey(key)
    }

    open fun setString(key: String, set: String?) {
        stringMap[key] = set
    }

    open fun removeString(key: String) {
        stringMap.remove(key)
    }

    open fun getString(key: String): String? {
        return stringMap.getOrDefault(key, null)
    }

    open fun getString(key: String, fail: String?): String? {
        return stringMap.getOrDefault(key, fail)
    }

    open fun getList(key: String): List<*>? {
        return listMap[key]
    }

    open fun getList(key: String, fail: List<*>?): List<*>? {
        return listMap.getOrDefault(key, fail)
    }

    open fun setList(key: String, list: List<*>?) {
        listMap[key] = list
    }

    open fun getHashSet(key: String): HashSet<*>? {
        return hashSetMap[key]
    }

    open fun setHashSet(key: String, hashSet: HashSet<*>?) {
        hashSetMap[key] = hashSet
    }

    open fun containsString(key: String): Boolean {
        return stringMap.containsKey(key)
    }
}
