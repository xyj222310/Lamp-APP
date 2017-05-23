package com.yjtse.lamp.domain;


public class IPort {

    private String gateWayNumber;
    private String ip;
    private int port;

    public IPort() {
    }

    public IPort(String gateWayNumber, String ip, int port) {
        this.gateWayNumber = gateWayNumber;
        this.ip = ip;
        this.port = port;
    }

    public String getGateWayNumber() {
        return gateWayNumber;
    }

    public void setGateWayNumber(String gateWayNumber) {
        this.gateWayNumber = gateWayNumber;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
