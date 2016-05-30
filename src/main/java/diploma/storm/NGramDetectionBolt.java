package diploma.storm;

import diploma.processors.Processor;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Никита on 30.05.2016.
 */
public class NGramDetectionBolt extends AbstractBasicBolt {
    public NGramDetectionBolt(Processor processor) {
        super(processor);
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        List<String> ngrams = (List<String>) processor.process(input.getValueByField("status"));
        for (String ngram : ngrams)
            collector.emit(new ArrayList<Object>() {{ add(ngram); add(input.getMessageId()); }});
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("ngram", "msgid"));
    }
}
