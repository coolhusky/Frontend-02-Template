package com.github.coolhusky.gateway.config;

import lombok.Data;

@Data
public class AppConfig {
    private String name = "NIOGateway";
    private String version = "1.0.0";
    private String host = "http://localhost";
    private int port = 8808;

    private static final AppConfig INSTANCE = new AppConfig();

    private AppConfig() {
    }

    public static AppConfig getInstance() {
        return INSTANCE;
    }
}
