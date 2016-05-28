package diploma.engines;

import diploma.processors.Processor;
import diploma.storm.StringRandomSpout;
import org.apache.storm.*;
import diploma.storm.StormBolt;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.*;
import org.apache.storm.kafka.trident.GlobalPartitionInformation;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Никита on 03.04.2016.
 */
public class StormEngine extends AbstractEngine {
    private static final Logger LOG = LoggerFactory.getLogger(StormEngine.class);
    private int numWorkers;

    public StormEngine(Processor processor, int numWorkers) {
        super(processor);
        this.numWorkers = numWorkers;
    }

    @Override
    public void run() throws Exception {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        String topicName = "my-replicated-topic";
        BrokerHosts hosts = new ZkHosts("192.168.1.21:2181");
        SpoutConfig spoutConfig = new SpoutConfig(hosts, topicName, "/" + topicName, "kafkastorm");
        // игнорируем смещение, записанное в zookeeper,
        // чтобы при каждом новом сабмите топологии сообщения читались заново
        spoutConfig.ignoreZkOffsets = true;
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
        topologyBuilder.setSpout("spout", kafkaSpout, 1);
//        topologyBuilder.setSpout("spout", new StringRandomSpout());
        topologyBuilder.setBolt("bolt", new StormBolt(this.processor), 6).shuffleGrouping("spout");
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
