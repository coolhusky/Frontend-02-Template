package com.github.coolhusky.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class HeaderHttpRequestFilter extends AbstractHttpRequestFilter {

    @Override
    protected void doFilter(FullHttpRequest request, ChannelHandlerContext ctx) {
        request.headers().set("mao", "soul");
    }
}
