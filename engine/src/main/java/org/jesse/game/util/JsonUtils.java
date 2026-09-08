package org.jesse.game.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;

import java.time.LocalDate;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-20
 */
public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Gson getGson() {
        return new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
                LocalDate.parse(json.getAsString()))

            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                new JsonPrimitive(src.toString()))
            .create();
    }

    public static String toJson(Object object) {
        return getGson().toJson(object);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(json, clazz);
    }
}
