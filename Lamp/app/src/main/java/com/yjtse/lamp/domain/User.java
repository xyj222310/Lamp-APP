package com.yjtse.lamp.domain;

/**
 * Created by yjtse on 2017/4/5.
 */

public class User {
//用户信息表sys_user.

    private Integer id;// 用户自增ID

    private String userId;// 用户名

    private String mail;

    private String phone;

    private String userName;

    private String userPass;

    private String sex;

    private String icon;

    private String role;

    public User() {

    }

    public User(String userId, String mail, String phone, String userName, String userPass, String sex, String icon, String role) {
        this.userId = userId;
        this.mail = mail;
        this.phone = phone;
        this.userName = userName;
        this.userPass = userPass;
        this.sex = sex;
        this.icon = icon;
        this.role = role;
    }

    public User(Integer id, String userId, String mail, String phone, String userName, String userPass, String sex, String icon, String role) {
        this.id = id;
        this.userId = userId;
        this.mail = mail;
        this.phone = phone;
        this.userName = userName;
        this.userPass = userPass;
        this.sex = sex;
        this.icon = icon;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", mail='" + mail + '\'' +
                ", phone='" + phone + '\'' +
                ", userName='" + userName + '\'' +
                ", userPass='" + userPass + '\'' +
                ", sex='" + sex + '\'' +
                ", icon='" + icon + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
