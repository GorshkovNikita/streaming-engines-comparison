package diploma.processors;

import twitter4j.Status;

import java.io.Serializable;

/**
 * Created by Никита on 03.04.2016.
 */
public class PrinterProcessor implements Processor, Serializable {
    @Override
    public void process(Status status) {
        System.out.println(status.getUser().getName() + " posts " + status.getText());
    }
}
