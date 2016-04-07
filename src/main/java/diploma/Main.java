package diploma;

import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.AuthorizationException;
import backtype.storm.generated.InvalidTopologyException;
import diploma.engines.*;
import diploma.processors.PrinterProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Никита on 14.12.2015.
 */
public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static void processStream(StreamEngineType streamEngineType) throws Exception {
        Engine engine;
        switch (streamEngineType) {
            case NONE:
                engine = new DefaultEngine(new PrinterProcessor());
                break;
            case STORM:
                engine = new StormEngine(new PrinterProcessor());
                break;
            case SPARK:
                engine = new SparkEngine(new PrinterProcessor());
                break;
            default:
                engine = new DefaultEngine(new PrinterProcessor());
                break;
        }
        engine.run();
    }

    public static void run(String consumerKey, String consumerSecret, String token, String secret, StreamEngineType streamEngineType) throws Exception, InterruptedException {
        TwitterStreamConnection.getInstance(consumerKey, consumerSecret, token, secret).getClient().connect();
        processStream(streamEngineType);
        TwitterStreamConnection.getInstance().getClient().stop();
        System.out.println("The client read " + TwitterStreamConnection.getInstance().getClient().getStatsTracker().getNumMessages() + " messages!\n");
    }

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException, AuthorizationException {
        try {
            StreamEngineType streamEngineType;
            if (args.length > 0)
                streamEngineType = StreamEngineType.valueOf(args[0].toUpperCase());
            else
                streamEngineType = StreamEngineType.NONE;
            Main.run("YOcgp2ovL8js849lx8hbnvxcf",
                    "IUxjGCksxWJiBlQ5PMsp5O8ksT7ZAsTDspOQafm46gSkYnII4u",
                    "4482173056-cZrtVBDKyRoeciGNs0JaDBtaNgGEl1IHKIckeSI",
                    "1nCVck1dtozb334vxlyca9Wb3Gq5ob7USXEX5sIqmIugs",
                    streamEngineType);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}