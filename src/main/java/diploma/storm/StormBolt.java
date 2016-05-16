package diploma.storm;

import diploma.processors.Processor;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import twitter4j.TwitterObjectFactory;

/**
 * Created by Никита on 05.04.2016.
 */
public class StormBolt extends BaseBasicBolt {
    private Processor processor;

    public StormBolt(Processor processor) {
        this.processor = processor;
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        try {
            //processor.process(tuple.getStringByField("status"));
            processor.process(TwitterObjectFactory.createStatus(tuple.getStringByField("status")));
            collector.emit(tuple.getValues());
        }
        catch (Exception ex) {//TwitterException ex) {
            // cannot parse json while process it in bolt
            //System.out.println("cannot parse json while process it in bolt");
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("status", "msgid"));
    }
}
