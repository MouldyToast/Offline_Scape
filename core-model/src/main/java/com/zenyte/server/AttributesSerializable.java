package com.zenyte.server;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Kryeus / John J. Woloszyk
 */
public abstract class AttributesSerializable extends Attributes {

    public static <T extends AttributesSerializable> T getFromFile(
            String file,
            T attributesSerializable) throws IOException {
        final Path path = Path.of(file);
        if (Files.exists(path)) {
            T attributes = new Gson().fromJson(
                    Files.readString(path),
                    attributesSerializable.getType()
            );
            if (attributes == null) {
                return attributesSerializable;
            }
            attributes.setFile(file);
            return attributes;
        } else {
            return attributesSerializable;
        }
    }

    public abstract Type getType();

    private transient String file;

    public AttributesSerializable(String file) {
        this.file = file;
    }

    void setFile(String file) {
        this.file = file;
    }

    public void write() {
        JsonUtil.toJson(this, file);
    }

    public void set(String key, Object value) {
        super.set(key, value);
        write();
    }

    public void remove(Object key) {
        super.remove(key);
        write();
    }

    public void setInt(String key, int set) {
        super.setInt(key, set);
        write();
    }

    public void removeInt(String key) {
        super.removeInt(key);
        write();
    }

    public void removeDouble(String key) {
        super.removeDouble(key);
        write();
    }

    public void setBoolean(String key, boolean set) {
        super.setBoolean(key, set);
        write();
    }

    public void removeBoolean(String key) {
        super.removeBoolean(key);
        write();
    }

    public void setLong(String key, long set) {
        super.setLong(key, set);
        write();
    }

    public void removeLong(String key) {
        super.removeLong(key);
        write();
    }

    public void setString(String key, String set) {
        super.setString(key, set);
        write();
    }

    public void removeString(String key) {
        super.removeString(key);
        write();
    }

    public void setList(String key, List list) {
        super.setList(key, list);
        write();
    }

}

