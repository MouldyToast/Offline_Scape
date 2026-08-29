package mgi.tools.parser.writers;


import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mgi.tools.parser.TypeProperty;
import mgi.tools.parser.TypeWriter;
import mgi.types.config.items.ItemDefinitions;

import java.lang.reflect.Field;
import java.util.*;
/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.1.2025
 */
public class ItemWriter implements TypeWriter<ItemDefinitions> {

    private static final ItemDefinitions DEFAULT = ItemDefinitions.DEFAULT;

    @Override
    public Map<String, Object> write(ItemDefinitions def) {
        Map<String, Object> properties = new LinkedHashMap<>();

        for (Field field : def.getClass().getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                continue;

            field.setAccessible(true);
            TypeProperty property = TypeProperty.get(field.getName());
            if (property == null)
                continue;

            try {
                Object value = field.get(def);
                Object defaultValue = field.get(DEFAULT);

                if (Objects.deepEquals(value, defaultValue))
                    continue;

                if (value == null)
                    continue;

                if (value instanceof Integer || value instanceof Boolean || value instanceof String) {
                    properties.put(property.getIdentifier(), value);
                } else if (value instanceof int[]) {
                    List<Long> list = new ArrayList<>();
                    for (int i : (int[]) value) list.add((long) i);
                    properties.put(property.getIdentifier(), list);
                } else if (value instanceof short[]) {
                    List<Long> list = new ArrayList<>();
                    for (short s : (short[]) value) list.add((long) s);
                    properties.put(property.getIdentifier(), list);
                } else if (value instanceof String[]) {
                    List<String> list = new ArrayList<>(Arrays.asList((String[]) value));
                    properties.put(property.getIdentifier(), list);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error comparing field: " + field.getName(), e);
            }
        }

        // Item options (op1-op5)
        for (int i = 0; i < 5; i++) {
            String val = def.getOption(i);
            String defaultVal = DEFAULT.getOption(i);
            if (!Objects.equals(val, defaultVal) && val != null) {
                properties.put("op" + (i + 1), val);
            }
        }

        // Parameters
        if (def.getParameters() != null && !def.getParameters().equals(DEFAULT.getParameters())) {
            Map<String, Object> paramMap = new HashMap<>();
            for (Int2ObjectMap.Entry<Object> entry : def.getParameters().int2ObjectEntrySet()) {
                paramMap.put(String.valueOf(entry.getIntKey()), entry.getValue());
            }
            properties.put("parameters", paramMap);
        }

        return properties;
    }

    @Override
    public String getType() {
        return "item";
    }
}