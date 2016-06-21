package diploma.spark;

import diploma.client.TwitterQueueRestClient;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.receiver.Receiver;

import java.io.IOException;

/**
 * Created by Никита on 07.04.2016.
 */
public class CustomReceiver extends Receiver<String> {
    public CustomReceiver(StorageLevel storageLevel) {
        super(storageLevel);
    }

    @Override
    public StorageLevel storageLevel() {
        return StorageLevel.MEMORY_ONLY();
    }

    @Override
    public void onStart() {
        new Thread() {
            @Override
            public void run() {
                receive();
            }
        }.start();
    }

    @Override
    public void onStop() {
    }

    public void receive() {
        while (!isStopped()) {
            try {
                String msg = TwitterQueueRestClient.nextMessage();
                if (msg != null)
                    store(msg);
            }
            catch (IOException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
            //store(TwitterStreamConnection.getNextMessage());
        }
    }
}
