package diploma.engines;

import diploma.processors.Processor;
import diploma.spark.CustomReceiver;
import kafka.serializer.StringDecoder;
import org.apache.spark.*;
import org.apache.spark.network.protocol.Encoders;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;
import scala.collection.DefaultMap;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import javax.swing.event.InternalFrameEvent;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Никита on 06.04.2016.
 */
public class SparkEngine extends AbstractEngine implements Serializable {
    public SparkEngine() {
        super();
    }

    public SparkEngine(Processor processor) {
        super(processor);
    }

    @Override
    public void run() throws Exception {
        SparkConf conf = new SparkConf()
                .setAppName("twitter-test")
                .setMaster("spark://192.168.1.21:7077");
            //.setMaster("local[2]");
//            .setJars(new String[]{
//                    "~/diploma/streaming-comparison-engines/target/streaming-engines-comparison-1.0-jar-with-dependencies.jar"
//            });

        JavaStreamingContext ssc = new JavaStreamingContext(conf, Durations.seconds(1));

        // TODO: сделать так, чтобы Spark читал сообщения с начала (свойство kafka consumer auto.offset.reset smallest)
//        Map<String, Integer> topics = new HashMap<>();
//        topics.put("my-replicated-topic", 1);
//        JavaPairReceiverInputDStream<String, String> messages =
//                KafkaUtils.createStream(ssc, "192.168.1.21:2181", "tweets-consumer", topics, StorageLevel.MEMORY_ONLY());

        Set<String> topics = new HashSet<>();
        topics.add("my-replicated-topic");

        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("auto.offset.reset", "smallest");
        kafkaParams.put("metadata.broker.list", "192.168.1.26:9092,192.168.1.23:9092");

        JavaPairInputDStream<String, String> messages =
                KafkaUtils.createDirectStream(ssc, String.class, String.class, StringDecoder.class, StringDecoder.class,
                        kafkaParams, topics);

        JavaDStream<Status> statuses = messages.map((status) -> {
            try {
                return TwitterObjectFactory.createStatus(status._2());
            }
            catch (TwitterException ex) {
                return null;
            }
        });

        JavaDStream<Status> filteredStatuses = statuses.filter((status) -> status != null);

        // processor::process equivalent to (status) -> processor.process(status)
        filteredStatuses.foreachRDD((rdd) -> {
            System.out.println("Количество объектов в RDD-шке = " + rdd.count());
            rdd.foreach(processor::process);
        });

        ssc.start();
        ssc.awaitTermination();
    }
}
