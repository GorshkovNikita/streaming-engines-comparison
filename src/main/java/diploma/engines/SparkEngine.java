package diploma.engines;

import diploma.processors.Processor;
import diploma.spark.CustomReceiver;
import org.apache.spark.*;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.*;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.io.Serializable;

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
        SparkConf conf = new SparkConf().setAppName("twitter-test").setMaster("local[2]");
        JavaStreamingContext ssc = new JavaStreamingContext(conf, Durations.seconds(1));
        JavaDStream<String> customReceiverStream = ssc.receiverStream(new CustomReceiver(StorageLevel.MEMORY_ONLY()));

        JavaDStream<Status> statuses = customReceiverStream.map((status) -> {
            try {
                return TwitterObjectFactory.createStatus(status);
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
        ssc.awaitTerminationOrTimeout(10000);
    }
}
