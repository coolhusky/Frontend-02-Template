package com.github.coolhusky.gateway.inbound;

import com.github.coolhusky.gateway.filter.AuthHttpRequestFilter;
import com.github.coolhusky.gateway.filter.HeaderHttpRequestFilter;
import com.github.coolhusky.gateway.filter.HttpRequestFilterChain;
import com.github.coolhusky.gateway.outbound.proxy.HttpProxyService;
import com.github.coolhusky.gateway.support.LoggingUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {
    private final List<String> proxyServer;
//    private final HttpRequestFilter requestFilter = new HeaderHttpRequestFilter();
    private final HttpRequestFilterChain requestFilterChain;
    private final HttpProxyService httpProxyService;

    public HttpInboundHandler(List<String> proxyServer) {
        this.proxyServer = proxyServer;
        this.httpProxyService = new HttpProxyService(this.proxyServer);
        this.requestFilterChain = new HttpRequestFilterChain();
        this.requestFilterChain.addFilter(new HeaderHttpRequestFilter());
        this.requestFilterChain.addFilter(new AuthHttpRequestFilter());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            FullHttpRequest request = (FullHttpRequest) msg;
            requestFilterChain.filter(request, ctx);
            httpProxyService.handle(request, ctx);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LoggingUtils.current().error("Server error", cause);
        ctx.close();
    }
}
