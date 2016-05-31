package diploma.engines;

import diploma.nlp.NGrams;
import diploma.processors.NGramsProcessor;
import diploma.processors.Processor;
import diploma.spark.CustomReceiver;
import kafka.serializer.StringDecoder;
import org.apache.spark.*;
import org.apache.spark.network.protocol.Encoders;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;
import scala.collection.DefaultMap;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import javax.swing.event.InternalFrameEvent;
import java.io.Serializable;
import java.util.*;

/**
 * Created by Никита on 06.04.2016.
 */
public class SparkEngine extends AbstractEngine implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(SparkEngine.class);
    public SparkEngine() {
        super();
    }

    public SparkEngine(Processor processor) {
        super(processor);
    }

    @Override
    public void run() throws Exception {
        Processor nGramsProcessor = new NGramsProcessor();
        // Создаем конфигурацию Spark
        SparkConf conf = new SparkConf()
                .setAppName("twitter-test")
                .setMaster("spark://192.168.1.21:7077");

        /*
            Входящий поток делится на части по времени Duration
            При одном ядре и numPartitions == 2 обработка будет происходить следующим образом:
            Если предположить, что поток делится таким образом, что в каждой RDD находится 4 элемента,
            то сначала будут обработаны (выведены на экран) 2 элемента, которые были назначены одному потоку,
            а потом 2 элемента, назначенные второму потоку. Далее будет происходить тоже самое для
            следующей RDD и тд.
        */

        // Создаем главную точку входа движка Spark Streaming
        JavaStreamingContext ssc = new JavaStreamingContext(conf, Durations.seconds(1));

        // TODO: сделать так, чтобы Spark читал сообщения с начала (свойство kafka consumer auto.offset.reset smallest)
//        Map<String, Integer> topics = new HashMap<>();
//        topics.put("my-replicated-topic", 1);
//        JavaPairReceiverInputDStream<String, String> messages =
//                KafkaUtils.createStream(ssc, "192.168.1.21:2181", "tweets-consumer", topics, StorageLevel.MEMORY_ONLY());

        // Множество строк, соответствующим темам в Kafka, из которых берутся данные
        Set<String> topics = new HashSet<>();
        topics.add("my-replicated-topic");

        // Настраиваем Kafka consumer
        Map<String, String> kafkaParams = new HashMap<>();
        // id группы потребителей
        kafkaParams.put("group.id", "spark-consumer");
        // при каждом запуске приложения сообщения из Kafka читать заново
        kafkaParams.put("auto.offset.reset", "smallest");
        //kafkaParams.put("zookeeper.connect", "192.168.1.21:2181");
        // список брокеров Kafka
        kafkaParams.put("metadata.broker.list", "192.168.1.26:9092,192.168.1.23:9092");

        // Создаем DStream, забирающий данные из Kafka
        JavaPairInputDStream<String, String> messages =
                KafkaUtils.createDirectStream(ssc,
                        String.class, String.class, // типы пары ключ - значение данных из Kafka
                        StringDecoder.class, StringDecoder.class, // классы десериализаторов
                        kafkaParams, topics);

        // Распараллеливаем наши RDD на 4 потока
        JavaPairDStream<String, String> partitionedMessages = messages.repartition(2);

        // Получаем статусы из сообщений
        JavaDStream<Status> statuses = partitionedMessages.map((status) -> {
            try {
                return TwitterObjectFactory.createStatus(status._2());
            } catch (TwitterException ex) {
                return null;
            }
        });

        // Фильтруем статусы, убирая null-объекты
        JavaDStream<Status> filteredStatuses = statuses.filter((status) -> status != null);

//        filteredStatuses = filteredStatuses.repartition(2);

        // Обрабатываем каждую RDD из потока
        // processor::process equivalent to (status) -> processor.process(status)
//        filteredStatuses.foreachRDD((rdd) -> {
//            LOG.info("Количество объектов в RDD-шке = " + rdd.count());
//            rdd.foreach(processor::process);
//        });

        JavaDStream<String> ngrams = filteredStatuses.flatMap(
                (status) -> (List<String>) nGramsProcessor.process(status.getText())
        );

        ngrams.foreachRDD((rdd) -> {
            rdd.foreach(processor::process);
        });

        ssc.start();
        ssc.awaitTermination();
    }
}
