package me.tingri.big_data.kafka.example;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 *  A Simple producer using API.
 *
 *  https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+Producer+Example
*/
public class _Producer {

    public static void main(String[] args) {
        long events = Long.parseLong(args[0]);
        Random rnd = new Random();
 
        Properties props = new Properties();
        props.put("metadata.broker.list", "localhost:9092");
        props.put("producer.type", "sync");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "me.tingri.big_data.kafka.example._Partitioner");
        props.put("request.required.acks", "1");
 
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<>(config);
 
        for (long nEvents = 0; nEvents < events; nEvents++) { 
        	   System.out.println("creating event "+nEvents);
               long runtime = new Date().getTime();  
               String ip = "192.168.2."+ rnd.nextInt(255); 
               String msg = runtime + ",tingri.me," + ip;
               KeyedMessage<String, String> data = new KeyedMessage<>(args[1], ip, msg);
               System.out.println(data.toString());
               try {
            	   producer.send(data);
               } catch(Exception e) {
            	 e.printStackTrace();
               }
        }

        producer.close();
    }
}
