package mgi.tools.parser;

import mgi.types.Definitions;

import java.util.Map;

/**
 * Implement this interface to support writing definition types (e.g. Item, NPC, Object) to a property map.
 *
 * @param <T> the type of Definitions this writer handles
 *
 * @author John J. Woloszyk / Kryeus
 * @date 7.1.2025
 */
public interface TypeWriter<T extends Definitions> {

    /**
     * Converts the definition object into a map of serializable properties.
     *
     * @param def the definition instance
     * @return a property map compatible with serialization formats (e.g. TOML, JSON)
     */
    Map<String, Object> write(T def);

    /**
     * Returns the type string associated with this writer, e.g., "item", "npc", etc.
     */
    String getType();
}