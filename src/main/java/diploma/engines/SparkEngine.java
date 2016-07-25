package diploma.engines;

import diploma.Config;
import diploma.nlp.NGrams;
import diploma.processors.NGramsProcessor;
import diploma.processors.Processor;
import diploma.spark.CustomReceiver;
import kafka.serializer.StringDecoder;
import org.apache.spark.*;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.network.protocol.Encoders;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Duration;
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
import java.util.function.Function;

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
        NGramsProcessor nGramsProcessor = new NGramsProcessor();
        // Создаем конфигурацию Spark
        SparkConf conf = new SparkConf()
                .setAppName("twitter-test")
                .set("spark.streaming.kafka.maxRatePerPartition", "500")
                ;

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

        // Множество строк, соответствующим темам в Kafka, из которых берутся данные
        Set<String> topics = new HashSet<>();
        topics.add(Config.KAFKA_TOPIC);

        // Настраиваем Kafka consumer
        Map<String, String> kafkaParams = new HashMap<>();
        // id группы потребителей
        kafkaParams.put("group.id", "spark-consumer");
        // при каждом запуске приложения сообщения из Kafka читать заново
        kafkaParams.put("auto.offset.reset", "smallest");
//        kafkaParams.put("zookeeper.connect", ":2181");
        // список брокеров Kafka
        kafkaParams.put("metadata.broker.list", Config.KAFKA_BROKER_LIST);

        // Создаем DStream, забирающий данные из Kafka
        JavaPairInputDStream<String, String> messages =
                KafkaUtils.createDirectStream(ssc,
                        String.class, String.class, // типы пары ключ - значение данных из Kafka
                        StringDecoder.class, StringDecoder.class, // классы десериализаторов
                        kafkaParams, topics);

//        JavaDStream<String> customReceiverStream = ssc.receiverStream(new CustomReceiver(StorageLevel.MEMORY_ONLY()));

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

        JavaDStream<String> ngrams = filteredStatuses.flatMap(
                (status) -> nGramsProcessor.process(status.getText())
        );

        //JavaDStream<Status> partitionedFilteredStatuses = filteredStatuses.repartition(2);

        // Обрабатываем каждую RDD из потока
        // processor::process equivalent to (status) -> processor.process(status)
//        filteredStatuses.foreachRDD((rdd) -> {
//            LOG.info("Количество объектов в RDD-шке = " + rdd.count());
//            rdd.foreach(processor::process);
//        });

        JavaPairDStream<String, Integer> mapNgrams = ngrams.mapToPair((ngram) -> new Tuple2<>(ngram, 1));

        JavaPairDStream<String, Integer> reducedMapNgrams = mapNgrams.reduceByKeyAndWindow(
                (value1, value2) -> value1 + value2, Durations.seconds(20), Durations.seconds(20));

        JavaPairDStream<String, Integer> reducedAndSortedMapNgrams = reducedMapNgrams.transformToPair((rdd) -> {
            rdd.foreach(item -> item.swap());
            rdd.sortByKey();
            rdd.foreach(item -> item.swap());
            return rdd;
        });

        //JavaDStream<Long> windowCount = reducedMapNgrams.count();
//        JavaDStream<Long> ngramsCount = ngrams.count();

//        count.foreachRDD((rdd) -> {
//            rdd.foreach((num) -> {
//                System.out.println(num);
//            });
//        });

        reducedAndSortedMapNgrams.foreachRDD((windowrdd) -> {
            LOG.info("---------------------------------НОВОЕ ОКНО---------------------------------------------------------");
            windowrdd.foreach((window) -> {
                if (window._2() >= 50)
                    LOG.info(window._1() + " " + window._2() + " раз");
            });
        });

//        ngramsCount.foreachRDD((rdd) -> {
//            rdd.foreach((ngram) -> {
//                System.out.println(ngram);
//                //System.out.println(ngram._1() + " " + ngram._2() + " раз.");
//            });
//        });

        ssc.start();
        ssc.awaitTermination();
    }
}
