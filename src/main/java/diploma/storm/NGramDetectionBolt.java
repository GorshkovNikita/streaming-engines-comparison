package diploma.storm;

import diploma.processors.Processor;
import org.apache.commons.lang3.StringEscapeUtils;
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
        // long start = System.nanoTime();
        List<String> ngrams = (List<String>) processor.process(StringEscapeUtils.unescapeJava((String) input.getValueByField("str")));
        for (String ngram : ngrams)
            collector.emit(new ArrayList<Object>() {{ add(ngram); }});
        // long elapsedTime = System.nanoTime() - start;
        //System.out.println("Поиск ngram занял " + elapsedTime / 1000 + "microseconds");
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("ngram"));
    }
}
