package diploma.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;

import java.io.Serializable;

/**
 * Created by Никита on 03.04.2016.
 */
public class PrinterStatusProcessor implements Processor<Status>, Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(PrinterStatusProcessor.class);

    @Override
    public void process(Status status) {
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        for (int i = 0; i < 250000; i++)
            LOG.info("Занимаю процессор " + i + "ый раз");
        String text = /* status.getUser().getName() + */ "posted " + status.getText();
        LOG.info(text);
    }
}
