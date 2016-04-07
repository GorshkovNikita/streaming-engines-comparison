package diploma.engines;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;
import diploma.processors.Processor;
import diploma.storm.StormBolt;
import diploma.storm.TwitterSpout;

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
        topologyBuilder.setSpout("spout", new TwitterSpout());
        topologyBuilder.setBolt("bolt", new StormBolt(this.processor)).shuffleGrouping("spout");
        // TODO: сделать нормальное создание цепочки обработчиков
        //topologyBuilder.setBolt("bolt2", new StormBolt(new CharCountProcessor())).shuffleGrouping("bolt");
        Config conf = new Config();
        conf.setDebug(false);

//        if (args != null && args.length > 0) {
//            conf.setNumWorkers(1);
//            StormSubmitter.submitTopologyWithProgressBar(args[0], conf, topologyBuilder.createTopology());
//        } else {
        LocalCluster cluster = new LocalCluster();
        StormTopology topology = topologyBuilder.createTopology();
        cluster.submitTopology("test", conf, topology);
        Utils.sleep(10000);
        cluster.killTopology("test");
        cluster.shutdown();
//        }
    }
}
