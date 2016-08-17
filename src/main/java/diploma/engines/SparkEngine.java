package diploma.engines;

import diploma.Config;
import diploma.nlp.NGrams;
import diploma.processors.NGramsProcessor;
import diploma.processors.Processor;
import diploma.spark.CustomReceiver;
import diploma.Utilities;
import kafka.serializer.StringDecoder;
import org.apache.commons.lang3.StringEscapeUtils;
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
                .set("spark.streaming.kafka.maxRatePerPartition", Config.RATE)
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

        // Распараллеливаем наши RDD на 2 потока
        JavaPairDStream<String, String> partitionedMessages = messages.repartition(2);

        // Получаем статусы из сообщений
//        JavaDStream<Status> statuses = partitionedMessages.map((status) -> {
//            try {
//                return TwitterObjectFactory.createStatus(status._2());
//            } catch (TwitterException ex) {
//                return null;
//            }
//        });

        // фильтруем, забирая только сам твит, без id из Kafka
        JavaDStream<String> statuses = partitionedMessages.map((status) -> StringEscapeUtils.unescapeJava(status._2()));

        // Фильтруем статусы, убирая null-объекты
        //JavaDStream<Status> filteredStatuses = statuses.filter((status) -> status != null);

        JavaDStream<String> ngrams = statuses.flatMap(
                (status) -> nGramsProcessor.process(status)
        );

        ngrams.print();

        ngrams.foreachRDD((rdd) -> {
            System.out.println(rdd.count());
//            rdd.foreach((ngram) -> {
//
//            });
        });

        //------------------------------------------------------------------------------------------------------

//        JavaPairDStream<String, Integer> mapNgrams = ngrams.mapToPair((ngram) -> new Tuple2<>(ngram, 1));
//
//        JavaPairDStream<String, Integer> reducedMapNgrams = mapNgrams.reduceByKeyAndWindow(
//                (value1, value2) -> value1 + value2, Durations.seconds(20), Durations.seconds(20));
//
//        reducedMapNgrams.foreachRDD((windowrdd) -> {
//            Map<String, Integer> map = windowrdd.collectAsMap();
//            List<Map.Entry<String, Integer>> entries = Utilities.entriesSortedByValues(map);
//            LOG.info("---------------------------------НОВОЕ ОКНО---------------------------------------------------------");
//            for (int i = 0; i < 50; i++) {
//                LOG.info(entries.get(i).getKey() + " " + entries.get(i).getValue() + " раз");
//            }
//        });

        //------------------------------------------------------------------------------------------------------

        ssc.start();
        ssc.awaitTermination();
    }
}
