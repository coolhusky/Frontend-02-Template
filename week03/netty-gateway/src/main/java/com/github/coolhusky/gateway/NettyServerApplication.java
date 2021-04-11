package com.github.coolhusky.gateway;

import com.github.coolhusky.gateway.config.AppConfig;
import com.github.coolhusky.gateway.server.NettyHttpServer;
import com.github.coolhusky.gateway.support.LoggingUtils;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NettyServerApplication {

    public static void main(String[] args) {
        AppConfig appConfig = AppConfig.getInstance();
        String proxyServers = System.getProperty("proxyServers", "http://localhost:8801,http://localhost:8802");

        LoggingUtils.current().info("{}{} starting...", appConfig.getName(), appConfig.getVersion());
        NettyHttpServer server = new NettyHttpServer(appConfig.getHost(), appConfig.getPort(), Arrays.stream(proxyServers.split(",")).collect(Collectors.toList()));
        LoggingUtils.current().info("{}{} started at {}:{} for server: {}",
                appConfig.getName(), appConfig.getVersion(), appConfig.getHost(), appConfig.getPort(), server.toString());

        try {
            server.run();
        } catch (Exception e) {
            LoggingUtils.current().error("Server error", e);
        }
    }

}
