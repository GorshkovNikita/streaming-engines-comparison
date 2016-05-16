package diploma;

/**
 * Created by Никита on 16.05.2016.
 */
public class TwitterThread implements Runnable {
    TwitterStreamConnection twitterStreamConnection;

    public TwitterThread(String consumerKey, String consumerSecret, String token, String secret) {
        this.twitterStreamConnection = TwitterStreamConnection.getInstance(consumerKey, consumerSecret, token, secret);
    }

    @Override
    public void run() {
        twitterStreamConnection.getClient().connect();
    }
}
