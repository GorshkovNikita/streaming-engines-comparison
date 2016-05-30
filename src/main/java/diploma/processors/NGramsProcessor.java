package diploma.processors;

import diploma.nlp.NGrams;
import twitter4j.Status;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Никита on 30.05.2016.
 */
public class NGramsProcessor implements Processor<List<String>, Status>, Serializable {
    @Override
    public List<String> process(Status status) {
        List<String> ngrams = new ArrayList<>();
        for (int i = 1; i <= 4; i++)
            ngrams.addAll(NGrams.ngrams(i, status.getText()));
        return ngrams;
    }
}