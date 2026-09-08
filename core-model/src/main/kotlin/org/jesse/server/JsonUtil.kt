package org.jesse.server

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.apache.commons.io.FileUtils
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

object JsonUtil {
    @JvmField
    val JSON_MAPPER: ObjectMapper = ObjectMapper().registerModule(JavaTimeModule())

    @JvmField
    val JSON_WRITER: ObjectWriter

    private val gson: Gson = GsonBuilder().create()
    private val prettyGson: Gson = GsonBuilder().setPrettyPrinting().create()

    init {
        JSON_MAPPER.registerModule(KotlinModule.Builder().build())
        JSON_MAPPER.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
        JSON_MAPPER.setVisibility(JSON_MAPPER.serializationConfig
                .defaultVisibilityChecker
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE))
        JSON_WRITER = JSON_MAPPER.writerWithDefaultPrettyPrinter()
    }

    @Deprecated("")
    @JvmStatic
    fun <T> toJson(t: T, filePath: String) {
        val prettyGson = GsonBuilder()
                .setPrettyPrinting()
                .create()
        val prettyJson = prettyGson.toJson(t)
        try {
            val bw = BufferedWriter(FileWriter(File(filePath)))
            bw.write(prettyJson)
            bw.flush()
            bw.close()
        } catch (e: Exception) {
            e.printStackTrace(System.err)
        }
    }

    @JvmStatic
    fun <T> toJson(t: T): String {
        return prettyGson.toJson(t)
    }

    @JvmStatic
    fun <T> toSimpleJson(t: T): String {
        return gson.toJson(t)
    }

    @JvmStatic
    fun <T> fromJsonText(text: String, typeToken: TypeToken<T>): T {
        return prettyGson.fromJson(text, typeToken.type)
    }

    @Deprecated("")
    @JvmStatic
    @Throws(IOException::class)
    fun <T> fromJson(filePath: String): T {
        return fromJson(filePath, object : TypeToken<T>() {})
    }

    @Deprecated("")
    @JvmStatic
    @Throws(IOException::class)
    fun <T> fromJson(filePath: String, typeToken: TypeToken<T>): T {
        return Gson().fromJson(FileUtils.readFileToString(File(filePath)), typeToken.type)
    }

    @Deprecated("")
    @JvmStatic
    @Throws(IOException::class)
    fun <T> fromJsonOrDefault(filePath: String, typeToken: TypeToken<T>, defaultObject: T): T {
        if (!File(filePath).exists()) {
            return defaultObject
        }

        return fromJson(filePath, typeToken)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun <T> toJacksonJson(t: T, filePath: String) {
        JSON_WRITER.writeValue(File(filePath), t)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun fromJacksonJson(file: File): JsonNode {
        return JSON_MAPPER.readTree(file)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun <T> fromJacksonJson(file: File, clazz: TypeReference<T>): T {
        return fromJacksonJson(file.path, clazz)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun <T> fromJacksonJsonString(json: String, clazz: TypeReference<T>): T {
        return JSON_MAPPER.readValue(json, clazz)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun <T> fromJacksonJson(filePath: String, clazz: TypeReference<T>): T {
        return JSON_MAPPER.readValue(File(filePath), clazz)
    }

    @JvmStatic
    fun pretty(): Gson {
        return prettyGson
    }

    @JvmStatic
    fun normal(): Gson {
        return gson
    }
}
