package diploma.engines;

import diploma.processors.Processor;
import diploma.spark.CustomReceiver;
import org.apache.spark.*;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import javax.swing.event.InternalFrameEvent;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
        SparkConf conf = new SparkConf().setAppName("twitter-test-2")
            .setMaster("spark://localhost.localdomain:7077");
            //.setMaster("local[2]");
//            .setJars(new String[]{
//                    "~/diploma/streaming-comparison-engines/target/streaming-engines-comparison-1.0-jar-with-dependencies.jar"
//            });
        Map<String, Integer> topics = new HashMap<>();
        topics.put("my-replicated-topic", 1);
        JavaStreamingContext ssc = new JavaStreamingContext(conf, Durations.seconds(1));
        JavaPairReceiverInputDStream<String, String> messages =
                KafkaUtils.createStream(ssc, "localhost:2181", "tweets-consumer", topics, StorageLevel.MEMORY_ONLY());

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
