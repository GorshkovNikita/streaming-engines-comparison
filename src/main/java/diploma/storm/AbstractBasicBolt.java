package diploma.storm;

import diploma.processors.Processor;
import org.apache.storm.topology.base.BaseBasicBolt;

/**
 * Created by Никита on 30.05.2016.
 */
public abstract class AbstractBasicBolt extends BaseBasicBolt {
    protected Processor processor;

    public AbstractBasicBolt(Processor processor) {
        this.processor = processor;
    }
}
