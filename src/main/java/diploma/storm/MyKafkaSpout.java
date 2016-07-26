package diploma.storm;

import org.apache.storm.kafka.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by Никита on 16.06.2016.
 */
public class MyKafkaSpout extends KafkaSpout {
    private int counter = 0;
    private int currentSecond = 0;
    private final int tuplesPerSecond = 6000;

    public MyKafkaSpout(SpoutConfig spoutConf) {
        super(spoutConf);
    }

    @Override
    public void nextTuple() {
        if (counter == tuplesPerSecond) {
            int newSecond = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
            if (newSecond <= currentSecond) {
                return;
            }
            counter = 0;
            currentSecond = newSecond;
        }

        ++counter;
        super.nextTuple();
    }
}
