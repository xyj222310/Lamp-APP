package com.yjtse.lamp.domain;


/**
 * Created by yjtse on 2017/4/5.
 */

public class Socket {

    private Integer id;// 自增ID

    private String socketId;// 插座id

    private String socketName;

    private String ownerId; //所属用户id

    private String status; //插座状态

    private String cron; //插座定时参数

    private String statusTobe; //定时设定的状态

    public Socket(String socketId, String socketName, String ownerId, String status, String cron, String statusTobe) {
        this.socketId = socketId;
        this.socketName = socketName;
        this.ownerId = ownerId;
        this.status = status;
        this.cron = cron;
        this.statusTobe = statusTobe;
    }

    public Socket() {

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

    public String getSocketName() {
        return socketName;
    }

    public void setSocketName(String socketName) {
        this.socketName = socketName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
