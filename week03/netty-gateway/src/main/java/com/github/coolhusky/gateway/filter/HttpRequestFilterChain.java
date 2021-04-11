package com.github.coolhusky.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class HttpRequestFilterChain {
    private HttpRequestFilter head = null;
    private HttpRequestFilter tail = null;

    public void addFilter(HttpRequestFilter filter) {
        filter.setSuccessor(null);

        if (head == null) {
            head = filter;
            tail = filter;
            return;
        }

        tail.setSuccessor(filter);
        tail = filter;
    }

    public void filter(FullHttpRequest request, ChannelHandlerContext ctx) {
        if (head != null) {
            head.filter(request, ctx);
        }
    }

}
