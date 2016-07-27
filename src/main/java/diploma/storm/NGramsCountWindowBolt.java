package diploma.storm;

import diploma.Utilities;
import org.apache.storm.metric.api.CountMetric;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.windowing.TupleWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Никита on 31.05.2016.
 */
public class NGramsCountWindowBolt extends BaseWindowedBolt {
    private OutputCollector collector;
//    private CountMetric countMetric;
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
//        countMetric = new CountMetric();
        this.collector = collector;
//        context.registerMetric("count-of-windows", countMetric, 60);
    }

    @Override
    public void execute(TupleWindow inputWindow) {
        System.out.println("---------------------------------НОВОЕ ОКНО---------------------------------------------------------");
        //countMetric.incr();
        Map<String, Integer> ngrams = new HashMap<>();
        for (Tuple tuple : inputWindow.get()) {
            String ngram = tuple.getStringByField("ngram");
            if (ngrams.containsKey(ngram))
                ngrams.put(ngram, ngrams.get(ngram) + 1);
            else
                ngrams.put(ngram, 1);
        }
        List<Map.Entry<String, Integer>> entries = Utilities.entriesSortedByValues(ngrams);
        for (int i = 0; i < 50; i++) {
            System.out.println(entries.get(i).getKey() + " " + entries.get(i).getValue() + " раз");
        }
        //System.out.println("----------------------------НОВОЕ ОКНО-----------------------------------");
        //for (Map.Entry<String, Integer> ngram : ngrams.entrySet())
            //System.out.println(ngram.getKey() + " = " + ngram.getValue());
            //collector.emit(new ArrayList<Object>() {{ add(ngram.getKey()); add(ngram.getValue()); }});
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //declarer.declare(new Fields("ngram", "count"));
    }
}
