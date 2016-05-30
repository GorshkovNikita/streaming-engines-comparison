package diploma.nlp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Никита on 30.05.2016.
 */
public class NGrams {
    public static List<String> ngrams(int n, String str) {
        List<String> ngrams = new ArrayList<String>();
        String[] words = str.split(" ");
        for (int i = 0; i < words.length - n + 1; i++)
            ngrams.add(concat(words, i, i+n));
        return ngrams;
    }

    private static String concat(String[] words, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++)
            sb.append(words[i] + " ");
        return sb.toString();
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        for (int n = 1; n <= 4; n++) {
            ngrams(n, "Журналист Павел Лобков уходит с \"Дождя\" из-за несогласия с новой политикой канала").forEach(System.out::println);
            System.out.println();
        }
        System.out.println("\n" + (System.nanoTime() - start) / 1000000);
    }
}
