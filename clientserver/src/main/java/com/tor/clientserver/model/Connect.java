package com.tor.clientserver.model;

public class Connect {

    private boolean letsConnect;
    private String port;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public boolean getLetsConnect() {
        return letsConnect;
    }

    public void setLetsConnect(boolean letsConnect) {
        this.letsConnect = letsConnect;
    }
}