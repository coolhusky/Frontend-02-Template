package com.github.coolhusky.gateway.outbound.client;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class OkHttpClientImpl implements HttpClient {
    private final OkHttpClient CLIENT = initClient();

    private static OkHttpClient initClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public byte[] doGet(String url, Map<String, String> headers) throws Throwable {
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .build();
        Response response = null;
        try {
            response = CLIENT.newCall(request).execute();
            return Optional.ofNullable(response.body())
                    .map(body -> {
                        try {
                            return body.bytes();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }).orElse(new byte[0]);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
