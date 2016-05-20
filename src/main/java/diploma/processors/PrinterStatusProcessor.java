package diploma.processors;

import twitter4j.Status;

import java.io.Serializable;

/**
 * Created by Никита on 03.04.2016.
 */
public class PrinterStatusProcessor implements Processor<Status>, Serializable {
    @Override
    public void process(Status status) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(status.getUser().getName() + " posts " + status.getText());
    }
}
