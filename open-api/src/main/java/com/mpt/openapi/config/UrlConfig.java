package com.mpt.openapi.config;

public enum UrlConfig {
//    API_SERVER("mpt-api-server.com"),
//    AUTH_SERVER("mpt-auth-server.com"),
//    GOODS_SERVER("mpt-goods-server.com"),
//    ALARM_SERVER("mpt-alarm-server.com");
    API_SERVER("localhost:8080"),
    AUTH_SERVER("mpt-auth-server.com"),
    GOODS_SERVER("localhost:8082"),
    ALARM_SERVER("localhost:8081");

    private String ip;

    UrlConfig(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return "http://" + ip;
    }
}
