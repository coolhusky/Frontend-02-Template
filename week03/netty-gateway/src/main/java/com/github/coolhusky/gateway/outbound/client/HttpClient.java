package com.github.coolhusky.gateway.outbound.client;

import java.util.Map;

public interface HttpClient {

    byte[] doGet(final String url, final Map<String, String> headers) throws Throwable;
}
