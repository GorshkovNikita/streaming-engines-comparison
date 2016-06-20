package diploma.storm;

import diploma.Utilities;
import diploma.client.TwitterQueueRestClient;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by ngorshkov on 5/16/16.
 */
public class TwitterQueueRestSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private int msgId = 0;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void nextTuple() {
        try {
            String msg = TwitterQueueRestClient.nextMessage();
            if (msg != null)
                collector.emit(new Values(msg, ++msgId), msgId);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("status", "msgid"));
    }
}
