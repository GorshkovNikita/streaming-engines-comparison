package diploma.processors;

/**
 * Created by Никита on 30.05.2016.
 */
public class PrinterStringProcessor implements Processor<Void, String> {
    @Override
    public Void process(String string) {
        System.out.println(string);
        return null;
    }
}
