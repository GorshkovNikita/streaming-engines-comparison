package diploma.engines;

import diploma.Config;
import diploma.Utilities;
import diploma.processors.NGramsProcessor;
import diploma.processors.Processor;
import diploma.processors.StatusFilterProcessor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Никита on 03.04.2016.
 */
public class DefaultEngine extends AbstractEngine {
    private int statusesPerSecond = 0;

    public DefaultEngine(Processor processor) {
        super(processor);
    }

    @Override
    public void run() throws Exception {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println(statusesPerSecond);
                    statusesPerSecond = 0;
                }
            }, 1000);
        process();
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        Future<String> future = executor.submit(() -> {
//                process();
//                return "Ready!";
//            }
//        );
//        try {
//            System.out.println(future.get(10000, TimeUnit.MILLISECONDS));
//        } catch (TimeoutException e) {
//            future.cancel(true);
//        }
//        executor.shutdownNow();
    }

    public void process() throws Exception {
        StatusFilterProcessor statusFilterProcessor = new StatusFilterProcessor();
        NGramsProcessor ngramsProcessor = new NGramsProcessor();
        Properties props = new Properties();
        props.put("bootstrap.servers", Config.KAFKA_BROKER_LIST);
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("my-replicated-topic"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
//                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
                Status status = statusFilterProcessor.process(record.value());
                List<String> ngrams = ngramsProcessor.process(status.getText());
                System.out.println(ngrams.size());
                statusesPerSecond++;
            }
        }
    }
}
