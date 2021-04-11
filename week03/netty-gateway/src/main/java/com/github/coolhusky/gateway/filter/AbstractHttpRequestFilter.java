package com.github.coolhusky.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public abstract class AbstractHttpRequestFilter implements HttpRequestFilter {

    protected HttpRequestFilter successor = null;

    public void setSuccessor(HttpRequestFilter successor) {
        this.successor = successor;
    }

    @Override
    public void filter(FullHttpRequest request, ChannelHandlerContext ctx) {
        doFilter(request, ctx);
        if (successor != null) {
            successor.filter(request, ctx);
        }
    }

    protected abstract void doFilter(FullHttpRequest request, ChannelHandlerContext ctx);
}
