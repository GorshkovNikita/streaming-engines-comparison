package diploma.storm;

import org.apache.storm.*;
import diploma.TwitterStreamConnection;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * Created by Никита on 04.04.2016.
 */
public class TwitterSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private int msgId = 0;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("status", "msgid"));
    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void nextTuple() {
        collector.emit(new Values(TwitterStreamConnection.getNextMessage(), ++msgId), msgId);
    }
}
