package com.github.coolhusky.gateway.outbound.proxy;

import com.github.coolhusky.gateway.filter.HeaderHttpResponseFilter;
import com.github.coolhusky.gateway.filter.HttpRequestFilter;
import com.github.coolhusky.gateway.filter.HttpResponseFilter;
import com.github.coolhusky.gateway.outbound.client.HttpClient;
import com.github.coolhusky.gateway.outbound.client.OkHttpClientImpl;
import com.github.coolhusky.gateway.router.HttpEndpointRouter;
import com.github.coolhusky.gateway.router.RandomHttpEndpointRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class HttpProxyService {
    private List<String> svcUrls;
    private HttpClient httpClient;
    private ExecutorService pool;

    private HttpEndpointRouter router = new RandomHttpEndpointRouter();
    private HttpResponseFilter responseFilter = new HeaderHttpResponseFilter();

    public HttpProxyService(List<String> svcUrls) {
        this.svcUrls = svcUrls.stream().map(this::formatUrl).collect(Collectors.toList());

        httpClient = new OkHttpClientImpl();

        // thread pool
        int cores = Runtime.getRuntime().availableProcessors();
        long keepAliveTime = 1000;
        int queueSize = 2048;
        RejectedExecutionHandler rejectHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        pool = new ThreadPoolExecutor(cores, cores, keepAliveTime, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueSize), new NamedThreadFactory("httpProxyPool"), rejectHandler);
    }


    public void handle(final FullHttpRequest request, final ChannelHandlerContext ctx) {
        String backendUrl = router.route(svcUrls);
        final String url = backendUrl + request.uri();

        pool.submit(() -> fetchGet(request, ctx, url));
//        fetchGet(request, ctx, url);
    }

    private void fetchGet(final FullHttpRequest request, final ChannelHandlerContext ctx, final String url) {
        FullHttpResponse response = null;
        try {
            byte[] respBytes = httpClient.doGet(url, headersAsMap(request.headers()));
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(respBytes));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, respBytes.length);
            responseFilter.filter(response);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NO_CONTENT);
            exceptionCaught(ctx, throwable);
        } finally {
            if (request != null) {
                if (!HttpUtil.isKeepAlive(request)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(response);
                }
            }
            ctx.flush();
        }

    }


    // utils

    private String formatUrl(String backend) {
        return backend.endsWith("/") ? backend.substring(0, backend.length() - 1) : backend;
    }

    private Map<String, String> headersAsMap(HttpHeaders headers) {
        Map<String, String> headersMap = new HashMap<>(16);
        Iterator<Map.Entry<String, String>> it = headers.iteratorAsString();
        while (it.hasNext()) {
            Map.Entry<String, String> header = it.next();
            headersMap.put(header.getKey(), header.getValue());
        }
        return headersMap;
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
