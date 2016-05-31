package diploma;

import diploma.engines.*;
import diploma.processors.PrinterStatusProcessor;
import diploma.processors.PrinterStringProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Никита on 14.12.2015.
 */
public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static void processStream(String[] args) throws Exception {
        StreamEngineType streamEngineType;
        int numWorkers;
        Engine engine = null;
        if (args.length > 0)
            streamEngineType = StreamEngineType.valueOf(args[0].toUpperCase());
        else
            streamEngineType = StreamEngineType.NONE;
        switch (streamEngineType) {
            case NONE:
                engine = new DefaultEngine(new PrinterStatusProcessor());
                break;
            case STORM:
//                engine = new StormEngine(new PrinterStringProcessor());
                try { numWorkers = Integer.valueOf(args[1]); }
                catch (NumberFormatException ex) {
                    LOG.info("Неверный формат ввода числа");
                    break;
                }
                catch (Exception ex) {
                    LOG.info("Параметр количества воркеров отсутствует");
                    break;
                }
                engine = new StormEngine(new PrinterStatusProcessor(), numWorkers);
                break;
            case SPARK:
                engine = new SparkEngine(new PrinterStringProcessor());
                break;
            default:
                engine = new DefaultEngine(new PrinterStatusProcessor());
                break;
        }
        if (engine != null)
            engine.run();
    }

    public static void main(String[] args) throws Exception {
        try {
            processStream(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
