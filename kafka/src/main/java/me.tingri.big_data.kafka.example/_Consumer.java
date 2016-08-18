package me.tingri.big_data.kafka.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by sandeepkunkunuru on 8/15/16.
 */
public class _Consumer {

    public static void main(String[] args) {
        int numConsumers = 3;
        String groupId = "consumer-tutorial-group";
        List<String> topics = Arrays.asList(args[0]);
        ExecutorService executor = Executors.newFixedThreadPool(numConsumers);

        final List<ConsumerLoop> consumers = new ArrayList<>();
        for (int i = 0; i < numConsumers; i++) {
            ConsumerLoop consumer = new ConsumerLoop(i, groupId, topics);
            consumers.add(consumer);
            executor.submit(consumer);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                for (ConsumerLoop consumer : consumers) {
                    consumer.shutdown();
                }
                executor.shutdown();
                try {
                    executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static class ConsumerLoop implements Runnable {
        private final KafkaConsumer<String, String> consumer;
        private final List<String> topics;
        private final int id;

        public ConsumerLoop(int id,
                            String groupId,
                            List<String> topics) {
            this.id = id;
            this.topics = topics;
            Properties props = new Properties();
            props.put("bootstrap.servers", "localhost:9092");
            props.put("group.id", groupId);
            props.put("key.deserializer", StringDeserializer.class.getName());
            props.put("value.deserializer", StringDeserializer.class.getName());
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            this.consumer = new KafkaConsumer<>(props);
        }

        @Override
        public void run() {
            try {
                consumer.subscribe(topics);

                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
                    for (ConsumerRecord<String, String> record : records) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("partition", record.partition());
                        data.put("offset", record.offset());
                        data.put("value", record.value());
                        System.out.println(this.id + ": " + data);
                    }
                }
            } catch (WakeupException e) {
                // ignore for shutdown
            } finally {
                consumer.close();
            }
        }

        public void shutdown() {
            consumer.wakeup();
        }
    }
}
