package me.tingri.datagen.util;

import me.tingri.big_data.util.RandomTextGenerator;
import me.tingri.datagen.models.Record;
import me.tingri.datagen.models.Type;

import java.util.stream.Stream;

public class RecordGenerator {

    public static Record[] records(String header, String delimiter, int numOfRecords){
        String[] colNames = header.split(delimiter);
        Record[] records = new Record[numOfRecords];

        for(int i =0; i < numOfRecords; i++){
            String[] colValues = new String[colNames.length];
            for (int j = 0; j < colNames.length; j++) {
                Type type = Dictionary.get(colNames[j]);
                colValues[j] = RandomTextGenerator.generateText(Integer.parseInt(type.get(Constants.LENGTH)),
                        type.get(Constants.CHARACTERS).toCharArray());
            }
            records[i] = new Record(colValues);
        }

        return records;
    }

    public static Stream<Record> records(String header, String delimiter, long numOfRecords){
        String[] colNames = header.split(delimiter);

        Stream<Record> records = Stream.generate(() -> {
            String[] colValues = new String[colNames.length];
            for (int j = 0; j < colNames.length; j++) {
                Type type = Dictionary.get(colNames[j]);
                colValues[j] = RandomTextGenerator.generateText(Integer.parseInt(type.get(Constants.LENGTH)),
                        type.get(Constants.CHARACTERS).toCharArray());
            }
            return new Record(colValues);
        }).limit(numOfRecords);

       return records;
    }

}