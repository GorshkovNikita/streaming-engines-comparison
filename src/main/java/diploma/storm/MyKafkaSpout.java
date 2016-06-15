package diploma.storm;

import com.google.common.base.Strings;
import org.apache.storm.Config;
import org.apache.storm.kafka.*;
import org.apache.storm.metric.api.IMetric;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by Никита on 16.06.2016.
 */
public class MyKafkaSpout extends KafkaSpout {
    public MyKafkaSpout(SpoutConfig spoutConf) {
        super(spoutConf);
    }

    @Override
    public void nextTuple() {
        Utils.sleep(1000);
        super.nextTuple();
    }
}
