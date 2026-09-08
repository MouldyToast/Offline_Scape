package org.jesse.server

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.nio.file.Path

/**
 * @author Kryeus / John J. Woloszyk
 */
class ServerAttributes : AttributesSerializable(getSaveFile()) {

    override fun getType(): Type {
        return object : TypeToken<ServerAttributes>() {}.type
    }

    companion object {
        @JvmStatic
        fun getSaveFile(): String {
            return Path.of("data", "server_attributes.json").toString()
        }
    }
}
