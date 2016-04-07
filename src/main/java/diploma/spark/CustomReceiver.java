package diploma.spark;

import diploma.TwitterStreamConnection;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.receiver.Receiver;

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
            store(TwitterStreamConnection.getNextMessage());
        }
    }
}
