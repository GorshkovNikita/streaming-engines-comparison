package diploma.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Никита on 19.06.2016.
 */
public class TwitterQueueRestClient {
    public static String nextMessage() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://192.168.1.21:7070/queue/");
        CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            String responseBody = EntityUtils.toString(response.getEntity());
            return !responseBody.equals("empty") ? responseBody : null;
        } finally {
            response.close();
            httpclient.close();
        }
    }

    public static void main(String[] args) throws IOException {
        String str1 = nextMessage();
        String str2 = nextMessage();
        System.out.println(str1);
        System.out.println(str2);
    }
}
