package diploma.storm;

import org.apache.storm.metric.api.IMetricsConsumer;
import org.apache.storm.task.IErrorReporter;
import org.apache.storm.task.TopologyContext;

import java.util.Collection;
import java.util.Map;

/**
 * @author Никита
 */
public class MyMetricConsumer implements IMetricsConsumer {
    @Override
    public void prepare(Map stormConf, Object registrationArgument, TopologyContext context, IErrorReporter errorReporter) {

    }

    @Override
    public void handleDataPoints(TaskInfo taskInfo, Collection<DataPoint> dataPoints) {
         for (DataPoint dataPoint : dataPoints) {
             System.out.println(dataPoint.value);
         }
    }

    @Override
    public void cleanup() {

    }
}
