package mgi.tools.parser.writers;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.1.2025
 */
public class TomlPrinter {
    public static void printTomlBlock(String type, Map<String, Object> properties) {
        String name = properties.containsKey("name") ? properties.get("name").toString() : null;
        System.out.println("[[" + type + "]]" + (name != null ? "  # " + name : ""));

        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            String formattedValue;
            if (value instanceof String) {
                formattedValue = "\"" + value.toString().replace("\"", "\\\"") + "\"";
            } else if (value instanceof List) {
                formattedValue = ((List<?>) value).stream()
                        .filter(Objects::nonNull)
                        .map(v -> v instanceof String ? "\"" + v + "\"" : v.toString())
                        .collect(java.util.stream.Collectors.joining(", ", "[", "]"));
            } else {
                formattedValue = value.toString();
            }

            System.out.println(key + " = " + formattedValue);
        }

        System.out.println(); // spacing between blocks
    }
}
