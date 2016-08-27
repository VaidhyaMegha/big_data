package me.tingri.datagen.models;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Created by skunkunuru on 8/27/2016.
 */
public class Type {
    private String info;

    public Type(String typeInfo) {
        this.info = typeInfo;
    }

    @Override
    public String toString() {
        return info;
    }

    public String get(String key) {
        try (JsonReader rdr = Json.createReader(new StringReader(info))) {
            JsonObject obj = rdr.readObject();
            return obj.getString(key);
        }
    }

}
