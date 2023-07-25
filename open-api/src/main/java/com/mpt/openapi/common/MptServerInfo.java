package com.mpt.openapi.common;

public enum MptServerInfo {
    API_SERVER("mpt-api-server.com"),
    AUTH_SERVER("mpt-auth-server.com"),
    GOODS_SERVER("mpt-goods-server.com");

    private String ip;

    MptServerInfo(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return "http://" + ip;
    }
}
