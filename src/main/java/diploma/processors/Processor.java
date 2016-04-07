package diploma.processors;

import twitter4j.Status;

/**
 * Created by Никита on 03.04.2016.
 */
public interface Processor {
    void process(Status status);
}
