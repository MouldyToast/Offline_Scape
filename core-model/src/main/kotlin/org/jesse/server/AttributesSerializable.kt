package org.jesse.server

import com.google.gson.Gson
import java.io.IOException
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.Path

/**
 * @author Kryeus / John J. Woloszyk
 */
abstract class AttributesSerializable(@field:Transient private var file: String) : Attributes() {

    abstract fun getType(): Type

    internal fun setFile(file: String) {
        this.file = file
    }

    open fun write() {
        JsonUtil.toJson(this, file)
    }

    override fun set(key: String, value: Any?) {
        super.set(key, value)
        write()
    }

    override fun remove(key: Any?) {
        super.remove(key)
        write()
    }

    override fun setInt(key: String, set: Int) {
        super.setInt(key, set)
        write()
    }

    override fun removeInt(key: String) {
        super.removeInt(key)
        write()
    }

    override fun removeDouble(key: String) {
        super.removeDouble(key)
        write()
    }

    override fun setBoolean(key: String, set: Boolean) {
        super.setBoolean(key, set)
        write()
    }

    override fun removeBoolean(key: String) {
        super.removeBoolean(key)
        write()
    }

    override fun setLong(key: String, set: Long) {
        super.setLong(key, set)
        write()
    }

    override fun removeLong(key: String) {
        super.removeLong(key)
        write()
    }

    override fun setString(key: String, set: String?) {
        super.setString(key, set)
        write()
    }

    override fun removeString(key: String) {
        super.removeString(key)
        write()
    }

    override fun setList(key: String, list: List<*>?) {
        super.setList(key, list)
        write()
    }

    companion object {
        @JvmStatic
        @Throws(IOException::class)
        fun <T : AttributesSerializable> getFromFile(
                file: String,
                attributesSerializable: T): T {
            val path = Path.of(file)
            if (Files.exists(path)) {
                val attributes = Gson().fromJson<T>(
                        Files.readString(path),
                        attributesSerializable.getType()
                )
                if (attributes == null) {
                    return attributesSerializable
                }
                attributes.setFile(file)
                return attributes
            } else {
                return attributesSerializable
            }
        }
    }
}
