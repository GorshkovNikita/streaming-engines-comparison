package diploma;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * @author Никита
 */
public class Config {
    public static final String ZOOKEEPER_ID;
    public static final String ZOOKEEPER_PORT;
    public static final String KAFKA_BROKER_LIST;
    public static final String KAFKA_BROKER_PORT;
    public static final String KAFKA_TOPIC;

    static {
        String zookeeperId;
        String zookeeperPort;
        String kafkaBrokerList;
        String kafkaTopic;
        String kafkaBrokerPort;
        try {
            InputStream in = Config.class.getResourceAsStream("/network-settings.properties");
            Properties prop = new Properties();
            prop.load(in);
            zookeeperId = prop.getProperty("zookeeper.ip");
            zookeeperPort = prop.getProperty("zookeeper.port");
            kafkaBrokerList = prop.getProperty("kafka.broker.list");
            kafkaBrokerPort = prop.getProperty("kafka.broker.port");
            kafkaTopic = prop.getProperty("kafka.topic");
        } catch (Exception e) {
            zookeeperId = "";
            zookeeperPort = "";
            kafkaBrokerList = "";
            kafkaTopic = "";
            kafkaBrokerPort = "";
            e.printStackTrace();
        }
        ZOOKEEPER_ID = zookeeperId;
        ZOOKEEPER_PORT = zookeeperPort;
        KAFKA_BROKER_LIST = kafkaBrokerList;
        KAFKA_TOPIC = kafkaTopic;
        KAFKA_BROKER_PORT = kafkaBrokerPort;
    }
}
