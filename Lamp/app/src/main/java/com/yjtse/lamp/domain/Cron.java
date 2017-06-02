package com.yjtse.lamp.domain;


/**
 * Created by yjtse on 2017/4/5.
 */

public class Cron {

    private Integer id;// 自增ID

    private String socketId;// 插座id

    private String ownerId; //所属用户id

    private String cron; //插座定时参数

    private String statusTobe; //定时设定的状态

    private String available;

    public Cron(Integer id, String socketId, String ownerId, String cron, String statusTobe, String available) {
        this.id = id;
        this.socketId = socketId;
        this.ownerId = ownerId;
        this.cron = cron;
        this.statusTobe = statusTobe;
        this.available = available;
    }

    public Cron() {
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getStatusTobe() {
        return statusTobe;
    }

    public void setStatusTobe(String statusTobe) {
        this.statusTobe = statusTobe;
    }
}
