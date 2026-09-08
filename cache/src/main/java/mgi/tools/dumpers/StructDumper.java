package mgi.tools.dumpers;

import org.jesse.CacheManager;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.Group;
import mgi.types.config.StructDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.items.ItemDefinitions;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.*;
import java.util.Map;

public class StructDumper {
    public static void main(String[] args) throws IOException {
        CacheManager.loadCache(Cache.openCache("cache/data/cache"));
        new StructDefinitions().load();
        new ItemDefinitions().load();
        new EnumDefinitions().load();
        try {
            for (final EnumDefinitions t : EnumDefinitions.definitions.values()) {
                if (t == null) {
                    continue;
                }
                if (t.getId() == 10025) {
                    System.out.println("[" + t.getId() + "]\r\n");
                    System.out.println("inputtype=" + EnumDefinitions.TYPE_MAP.get(t.getKeyType()) + "\r\n");
                    System.out.println("outputtype=" + EnumDefinitions.TYPE_MAP.get(t.getValueType()) + "\r\n");
                    if (t.getValueType() == 's') {
                        System.out.println("default=" + EnumDefinitions.getPrettyValue(t.getValueType(), t.getDefaultString()) + "\r\n");
                    } else {
                        System.out.println("default=" + EnumDefinitions.getPrettyValue(t.getValueType(), t.getDefaultInt()) + "\r\n");
                    }

                    for (final Map.Entry<Integer, Object> param : t.getValues().entrySet()) {
                        System.out.println("val=" + EnumDefinitions.getPrettyValue(t.getKeyType(), param.getKey()) + "," + EnumDefinitions.getPrettyValue(t.getValueType(), param.getValue()) + "\r\n");
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        try {
            for (final StructDefinitions t : StructDefinitions.definitions) {
                if (t == null) {
                    continue;
                }
                if (t.getId() == 10300) {
                    System.out.println(t.printParams());
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
