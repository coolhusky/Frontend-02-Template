package com.github.coolhusky.gateway.filter;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthHttpRequestFilter extends AbstractHttpRequestFilter {
    private static final String NO_PERMISSION = "no_permission";

    @Override
    protected void doFilter(FullHttpRequest request, ChannelHandlerContext ctx) {
        String cookie = request.headers().get(HttpHeaderNames.COOKIE);
        if (!checkPermission(cookie)) {
            noPermission(request, ctx);
        }
    }

    private boolean checkPermission(String cookie) {
        if (StringUtils.isBlank(cookie)) {
            return false;
        }
        Set<Cookie> cookies = ServerCookieDecoder.LAX.decode(cookie);
        if (cookies.isEmpty()) {
            return false;
        }
        return cookies.stream().anyMatch(c ->
                "token".equals(c.name()) && "abc123".equals(c.value()));

    }

    private void noPermission(FullHttpRequest request, ChannelHandlerContext ctx) {
        FullHttpResponse response = null;
        byte[] bytes = NO_PERMISSION.getBytes(StandardCharsets.UTF_8);
        try {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer(bytes));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, bytes.length);
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
}
