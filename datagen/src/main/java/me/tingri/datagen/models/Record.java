package me.tingri.datagen.models;

import java.util.Arrays;

/**
 * Created by skunkunuru on 8/27/2016.
 */
public class Record {
    private String[] colValues;

    public Record(String ... values){
        this.colValues = values;
    }

    @Override
    public String toString() {
        return Arrays.toString(colValues);
    }

    public String value(String delimiter) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(String.join(delimiter,colValues));

        return strBuilder.toString();
    }
}
