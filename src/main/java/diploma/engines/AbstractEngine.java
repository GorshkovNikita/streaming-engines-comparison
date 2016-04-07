package diploma.engines;

import diploma.processors.Processor;

/**
 * Created by Никита on 03.04.2016.
 */
public abstract class AbstractEngine implements Engine {
    protected Processor processor;

    public AbstractEngine() {
    }

    public AbstractEngine(Processor processor) {
        this.processor = processor;
    }

    public Processor getProcessor() {
        return processor;
    }
}
