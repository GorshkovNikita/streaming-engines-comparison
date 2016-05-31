package diploma.storm;

import diploma.processors.Processor;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Никита on 05.04.2016.
 */
public class StormBolt extends AbstractBasicBolt {
    private static final Logger LOG = LoggerFactory.getLogger(StormBolt.class);

    public StormBolt(Processor processor) {
        super(processor);
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        try {
            // Имя поля обозначено в классе StringScheme
            Status status = TwitterObjectFactory.createStatus(tuple.getStringByField("str"));
            // processor.process(status);
            // отправляем твиты дальше по топологии
            collector.emit(new ArrayList<Object>(){{ add(status.getText());}});
        }
        catch (TwitterException e) {
            //LOG.info("Ignored status");
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd) {
        ofd.declare(new Fields("statusText"));
    }
}
