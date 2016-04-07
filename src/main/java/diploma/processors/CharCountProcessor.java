package diploma.processors;

import twitter4j.Status;

import java.io.Serializable;

/**
 * Created by Никита on 05.04.2016.
 */
public class CharCountProcessor implements Processor, Serializable {
    @Override
    public void process(Status status) {
        System.out.println("tweet " + status.getText() + " has " + status.getText().length() + " characters");
    }
}
