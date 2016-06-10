package diploma.storm;

import diploma.processors.Processor;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.util.ArrayList;

/**
 * Created by Никита on 10.06.2016.
 */
public class PrinterBolt extends AbstractBasicBolt {
    private static final Logger LOG = LoggerFactory.getLogger(PrinterBolt.class);

    public PrinterBolt(Processor processor) {
        super(processor);
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        System.out.println(tuple.getStringByField("status"));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("statusText"));
    }
}
