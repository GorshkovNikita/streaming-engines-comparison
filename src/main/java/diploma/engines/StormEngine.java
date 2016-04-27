package diploma.engines;

import diploma.processors.Processor;
import org.apache.storm.*;
import diploma.storm.StormBolt;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.*;
import org.apache.storm.kafka.trident.GlobalPartitionInformation;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Никита on 03.04.2016.
 */
public class StormEngine extends AbstractEngine {
    public StormEngine(Processor processor) {
        super(processor);
    }

    @Override
    public void run() throws Exception {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        String topicName = "test";
//        Broker broker = new Broker("localhost");//localhost:9092
//        Broker broker1 = new Broker("localhost", 9093);
//        Broker broker2 = new Broker("localhost", 9094);
//        GlobalPartitionInformation partitionInfo = new GlobalPartitionInformation(topicName);
//        partitionInfo.addPartition(0, broker);//mapping from partition 0 to brokerForPartition0
//        partitionInfo.addPartition(0, broker1);
//        partitionInfo.addPartition(0, broker2);
//        StaticHosts hosts = new StaticHosts(partitionInfo);
        BrokerHosts hosts = new ZkHosts("localhost:2181");
        SpoutConfig spoutConfig = new SpoutConfig(hosts, topicName, "/" + topicName, "kafkastorm");
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
        topologyBuilder.setSpout("spout", kafkaSpout);
        //topologyBuilder.setSpout("spout", new TwitterSpout());
        topologyBuilder.setBolt("bolt", new StormBolt(this.processor)).shuffleGrouping("spout");
        // TODO: сделать нормальное создание цепочки обработчиков
        //topologyBuilder.setBolt("bolt2", new StormBolt(new CharCountProcessor())).shuffleGrouping("bolt");
        Config conf = new Config();
        conf.setDebug(false);

        LocalCluster cluster = new LocalCluster();
        StormTopology topology = topologyBuilder.createTopology();
        cluster.submitTopology("test", conf, topology);
        Utils.sleep(10000);
        cluster.killTopology("test");
        cluster.shutdown();

//        conf.setNumWorkers(1);
//        StormSubmitter.submitTopology("test", conf, topologyBuilder.createTopology());
    }
}
