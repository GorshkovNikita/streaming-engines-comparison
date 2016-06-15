package diploma.processors;

import diploma.nlp.NGrams;
import twitter4j.Status;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Никита on 30.05.2016.
 */
public class NGramsProcessor implements Processor<List<String>, String>, Serializable {
    @Override
    public List<String> process(String statusText) {
        return NGrams.allNGrams(statusText);
    }
}
