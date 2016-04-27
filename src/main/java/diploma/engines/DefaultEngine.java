package diploma.engines;

import diploma.TwitterStreamConnection;
import diploma.processors.Processor;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.util.concurrent.*;

/**
 * Created by Никита on 03.04.2016.
 */
public class DefaultEngine extends AbstractEngine {
    public DefaultEngine(Processor processor) {
        super(processor);
    }

    @Override
    public void run() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> {
                process();
                return "Ready!";
            }
        );
        try {
            System.out.println(future.get(10000, TimeUnit.MILLISECONDS));
        } catch (TimeoutException e) {
            future.cancel(true);
        }
        executor.shutdownNow();
    }

    public void process() {
        while (true) {
            if (TwitterStreamConnection.getInstance().getClient().isDone()) {
                System.out.println("Client connection closed unexpectedly: " + TwitterStreamConnection.getInstance().getClient().getExitEvent().getMessage());
                break;
            }

            String msg = TwitterStreamConnection.getNextMessage();
            if (msg == null) {
                System.out.println("Did not receive a message in 1 second");
            } else {
                try {
                    this.processor.process(TwitterObjectFactory.createStatus(msg));
                }
                catch (TwitterException ex) {
                    // cannot parse json, just ignore it
                }
            }
        }
    }
}
