package diploma;

import diploma.engines.*;
import diploma.processors.PrinterStatusProcessor;
import diploma.processors.PrinterStringProcessor;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Никита on 14.12.2015.
 */
public class Main {
    //private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static void processStream(StreamEngineType streamEngineType) throws Exception {
        Engine engine;
        switch (streamEngineType) {
            case NONE:
                engine = new DefaultEngine(new PrinterStatusProcessor());
                break;
            case STORM:
//                engine = new StormEngine(new PrinterStringProcessor());
                engine = new StormEngine(new PrinterStatusProcessor());
                break;
            case SPARK:
                engine = new SparkEngine(new PrinterStatusProcessor());
                break;
            default:
                engine = new DefaultEngine(new PrinterStatusProcessor());
                break;
        }
        engine.run();
    }

    public static void run(String consumerKey, String consumerSecret, String token, String secret, StreamEngineType streamEngineType) throws Exception, InterruptedException {
        processStream(streamEngineType);
    }

    public static void main(String[] args) throws Exception {
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
