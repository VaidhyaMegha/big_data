package me.tingri.datagen.util;

import me.tingri.datagen.models.Type;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * Created by skunkunuru on 8/27/2016.
 */
public class Dictionary {
    private static Map<String, Type> types = new HashMap<>();
    private static Properties prop = new Properties();

    static {
        load(Dictionary.class.getClassLoader().getResourceAsStream(Constants.DEFAULT_DICTIONARY));
    }

    public static void load(InputStream input){
        try{
            if (input == null) return;
            prop.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void load(String path){
        //load a properties file from class path, inside static block
        try (InputStream input = new FileInputStream(new File(path));) {
            load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Type get(String arg) {
        if (types.get(arg) == null) types.put(arg, new Type(prop.getProperty(arg)));

        return types.get(arg);
    }
}

