package diploma.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;

import java.io.Serializable;

/**
 * Created by Никита on 03.04.2016.
 */
public class PrinterStatusProcessor implements Processor<Void, Status>, Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(PrinterStatusProcessor.class);

    @Override
    public Void process(Status status) {
        String text = status.getUser().getName() + "posted " + status.getText();
        System.out.println(text);
        return null;
    }
}
