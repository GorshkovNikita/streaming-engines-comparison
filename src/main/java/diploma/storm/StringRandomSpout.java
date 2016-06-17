package diploma.storm;

import diploma.Utilities;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by ngorshkov on 5/16/16.
 */
public class StringRandomSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private int msgId = 0;
    private int counter = 0;
    private int currentSecond = 0;
    private final int tuplesPerSecond = 5;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void nextTuple() {
        //Utils.sleep(1000);
        if(counter == tuplesPerSecond) {
            int newSecond = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
            if(newSecond <= currentSecond) {
                return;
            }

            counter = 0;
            currentSecond = newSecond;
        }

        ++counter;
        collector.emit(new Values(Utilities.generateRandomString(), ++msgId), msgId);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("status", "msgid"));
    }
}
