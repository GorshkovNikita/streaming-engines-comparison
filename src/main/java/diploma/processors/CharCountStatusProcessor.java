package diploma.processors;

import twitter4j.Status;

import java.io.Serializable;

/**
 * Created by Никита on 05.04.2016.
 */
public class CharCountStatusProcessor implements Processor<Integer, Status>, Serializable {
    @Override
    public Integer process(Status status) {
        Integer numChars = status.getText().length();
        System.out.println("tweet " + status.getText() + " has " + numChars + " characters");
        return numChars;
    }
}
