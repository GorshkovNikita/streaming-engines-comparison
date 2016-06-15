package diploma.processors;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.util.ArrayList;

/**
 * Created by Никита on 16.06.2016.
 */
public class StatusFilterProcessor implements Processor<Status, String> {
    @Override
    public Status process(String statusJson) {
        try {
            return TwitterObjectFactory.createStatus(statusJson);
        }
        catch (TwitterException e) {
            return null;
        }
    }
}
