package com.github.coolhusky.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public interface HttpRequestFilter {
    void setSuccessor(HttpRequestFilter filter);

    void filter(FullHttpRequest request, ChannelHandlerContext ctx);
}
