package me.tingri.datagen.util;

import me.tingri.datagen.models.Record;
import me.tingri.datagen.models.Type;

public class RecordGenerator {

    public static Record[] records(String header, String delimiter, int numOfRecords){
        String[] colNames = header.split(delimiter);
        Record[] records = new Record[numOfRecords];

        for(int i =0; i < numOfRecords; i++){
            String[] colValues = new String[colNames.length];
            for (int j = 0; j < colNames.length; j++) {
                Type type = Dictionary.get(colNames[j]);
                colValues[j] = RandomTextGenerator.generateText(Integer.parseInt(type.get("length")),
                        type.get("characters").toCharArray());
            }
            records[i] = new Record(colValues);
        }

        return records;
    }
}