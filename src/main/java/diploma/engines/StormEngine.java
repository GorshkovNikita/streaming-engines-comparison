package diploma.engines;

import diploma.processors.*;
import diploma.storm.*;
import org.apache.storm.*;
import org.apache.storm.Config;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.*;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by Никита on 03.04.2016.
 */
public class StormEngine extends AbstractEngine {
    private static final Logger LOG = LoggerFactory.getLogger(StormEngine.class);
    private int numWorkers;

    // TODO: такой подход не работает при количестве обработчиков больше одного
    public StormEngine(Processor processor, int numWorkers) {
        super(processor);
        this.numWorkers = numWorkers;
    }

    @Override
    public void run() throws Exception {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        // Указываем название темы Kafka, из которой берутся данные
        String topicName = diploma.Config.KAFKA_TOPIC;
        // Указываем ip и порт zookeeper-сервера
        BrokerHosts hosts = new ZkHosts(diploma.Config.ZOOKEEPER_IP + ":" + diploma.Config.ZOOKEEPER_PORT);
        SpoutConfig spoutConfig = new SpoutConfig(hosts, topicName, "/" + topicName, "kafkastorm");
        // игнорируем смещение, записанное в zookeeper,
        // чтобы при каждом новом сабмите топологии сообщения читались заново
        spoutConfig.ignoreZkOffsets = true;
        // Указываем десериализатор
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
        // Создаем Spout
        topologyBuilder.setSpout("spout", kafkaSpout, 1);
//        topologyBuilder.setSpout("spout", new TwitterQueueRestSpout(), 1);

        // Bolt-фильтр, нужен обязательно! Работает точно также, как в Spark
        topologyBuilder.setBolt("bolt", new StatusFilterBolt(new StatusFilterProcessor()), 2)
                .shuffleGrouping("spout");

        // Bolt определения N-gram
        topologyBuilder.setBolt("ngram-detection-bolt", new NGramDetectionBolt(new NGramsProcessor()), 2)
                .shuffleGrouping("bolt");

        //----------------------------------------------------------------------------------------

//        topologyBuilder.setBolt("window-bolt", new NGramsCountWindowBolt()
//                .withWindow(
//                        new BaseWindowedBolt.Duration(20, TimeUnit.SECONDS),
//                        new BaseWindowedBolt.Duration(20, TimeUnit.SECONDS))
//                , 2).shuffleGrouping("ngram-detection-bolt");

        //----------------------------------------------------------------------------------------

        Config conf = new Config();
        conf.setDebug(false);
//        conf.registerMetricsConsumer(MyMetricConsumer.class);
        //conf.setMaxSpoutPending(15);
        //conf.put(Config.TOPOLOGY_SLEEP_SPOUT_WAIT_STRATEGY_TIME_MS, 1000);

        StormTopology topology = topologyBuilder.createTopology();

        // start local cluster
//        LocalCluster cluster = new LocalCluster();
//        cluster.submitTopology("test", conf, topology);
//        Utils.sleep(10000);
//        cluster.killTopology("test");
//        cluster.shutdown();

//        submit topology on cluster
        conf.setNumWorkers(numWorkers);
        StormSubmitter.submitTopology("mytopology", conf, topology);
        LOG.info("topology submitted!!!");
    }
}
