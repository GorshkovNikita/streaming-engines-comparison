package diploma.storm;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import diploma.TwitterStreamConnection;

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
