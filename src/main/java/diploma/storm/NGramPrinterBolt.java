package diploma.storm;

import diploma.processors.Processor;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

/**
 * Created by Никита on 30.05.2016.
 */
public class NGramPrinterBolt extends AbstractBasicBolt {
    public NGramPrinterBolt(Processor processor) {
        super(processor);
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        processor.process(input.getStringByField("ngram"));
        collector.emit(input.getValues());
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("ngram", "msgid"));
    }
}
