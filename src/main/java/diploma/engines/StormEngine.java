package diploma.engines;

import diploma.processors.NGramsProcessor;
import diploma.processors.PrinterStringProcessor;
import diploma.processors.Processor;
import diploma.storm.*;
import org.apache.spark.streaming.Durations;
import org.apache.storm.*;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.*;
import org.apache.storm.kafka.trident.GlobalPartitionInformation;
import org.apache.storm.shade.org.joda.time.Duration;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
        String topicName = "my-replicated-topic";
        // Указываем ip и порт zookeeper-сервера
        BrokerHosts hosts = new ZkHosts("192.168.1.21:2181");
        SpoutConfig spoutConfig = new SpoutConfig(hosts, topicName, "/" + topicName, "kafkastorm");
        // игнорируем смещение, записанное в zookeeper,
        // чтобы при каждом новом сабмите топологии сообщения читались заново
        spoutConfig.ignoreZkOffsets = true;
        // Указываем десериализатор
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
        // Создаем Spout
        topologyBuilder.setSpout("spout", kafkaSpout, 1);
//        topologyBuilder.setSpout("spout", new StringRandomSpout());
        topologyBuilder.setBolt("bolt", new StormBolt(this.processor), 2)
                .shuffleGrouping("spout");
        topologyBuilder.setBolt("ngram-detection-bolt", new NGramDetectionBolt(new NGramsProcessor()), 2)
                .shuffleGrouping("bolt");
        topologyBuilder.setBolt("window-bolt", new NGramsCountWindowBolt()
                .withWindow(
                        new BaseWindowedBolt.Duration(1, TimeUnit.MILLISECONDS),
                        new BaseWindowedBolt.Duration(1, TimeUnit.MILLISECONDS))
                , 2);
//        topologyBuilder.setBolt("ngram-printer-bolt", new NGramPrinterBolt(new PrinterStringProcessor()), 2)
//                .shuffleGrouping("ngram-detection-bolt");

        // TODO: сделать нормальное создание цепочки обработчиков
        //topologyBuilder.setBolt("bolt2", new StormBolt(new CharCountProcessor())).shuffleGrouping("bolt");
        Config conf = new Config();
        conf.setDebug(false);

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
