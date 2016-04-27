package diploma.processors;

import java.io.Serializable;

/**
 * Created by Никита on 21.04.2016.
 */
public class PrinterStringProcessor implements Processor<String>, Serializable {
    @Override
    public void process(String string) {
        System.out.println(string);
    }
}
