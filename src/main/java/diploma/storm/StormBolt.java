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

/**
 * Created by Никита on 05.04.2016.
 */
public class StormBolt extends BaseBasicBolt {
    private static final Logger LOG = LoggerFactory.getLogger(StormBolt.class);
    private Processor processor;

    public StormBolt(Processor processor) {
        this.processor = processor;
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        try {
            //processor.process(tuple.getStringByField("status"));
            LOG.info("Нахожусь в execute");
            LOG.info(processor.getClass().getTypeName());
            try {
                Status status = TwitterObjectFactory.createStatus(tuple.getStringByField("str"));
                LOG.info("Со статусом то все нормально = " + status.getText());
                processor.process(status);
                LOG.info("А сюда я видимо не попаду");
            }
            catch (TwitterException e) {
                LOG.info("Ignored status");
            }
            catch (Exception e) {
                LOG.info("Something went wrong in execute");
                LOG.info(e.getMessage());
                LOG.info(e.getClass().getTypeName());
            }
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
