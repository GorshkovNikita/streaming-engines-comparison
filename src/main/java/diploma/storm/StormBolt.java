package diploma.storm;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import diploma.processors.Processor;
import twitter4j.TwitterException;
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
            processor.process(TwitterObjectFactory.createStatus(tuple.getStringByField("status")));
            collector.emit(tuple.getValues());
        }
        catch (TwitterException ex) {
            // cannot parse json while process it in bolt
            //System.out.println("cannot parse json while process it in bolt");
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("status", "msgid"));
    }
}
