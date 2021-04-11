package socket.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;

public class OkHttpTest {
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final String URL = "http://localhost:8802";

    public static void main(String[] args) {
        Request request = new Request.Builder()
                .url(URL)
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            ResponseBody body = response.body();
            System.out.println(body.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
