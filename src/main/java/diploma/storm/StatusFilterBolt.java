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
 * Created by Никита on 05.04.2016.
 */
public class StatusFilterBolt extends AbstractBasicBolt {
    private static final Logger LOG = LoggerFactory.getLogger(StatusFilterBolt.class);

    public StatusFilterBolt(Processor processor) {
        super(processor);
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        // Имя поля обозначено в классе StringScheme
        Status status = (Status) processor.process(tuple.getStringByField("status"));
        if (status != null)
            collector.emit(new ArrayList<Object>(){{ add(status.getText());}});
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("statusText"));
    }
}
