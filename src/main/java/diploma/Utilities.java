package diploma;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

/**
 * Created by Никита on 16.05.2016.
 */
public class Utilities {
    private static final String CHAR_LIST =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int RANDOM_STRING_LENGTH = 10;

    /**
     * This method generates random string
     * @return
     */
    public static String generateRandomString() {

        StringBuffer randStr = new StringBuffer();
        for(int i=0; i<RANDOM_STRING_LENGTH; i++){
            int number = getRandomNumber();
            char ch = CHAR_LIST.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }

    /**
     * This method generates random numbers
     * @return int
     */
    private static int getRandomNumber() {
        int randomInt = 0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }

    public static void testWait(long interval) {
        final long INTERVAL = interval;
        long start = System.nanoTime();
        long end=0;
        do{
            end = System.nanoTime();
        } while(start + INTERVAL >= end);
        // System.out.println(end - start);
    }
}
