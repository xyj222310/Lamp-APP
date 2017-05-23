package com.yjtse.lamp.domain;


/**
 * 登陆方式的枚举
 * LOGIN 用户已登录
 * LOGOUT 用户未登录
 * LOGIN_REMOTE 当前登录方式为远程登陆
 * LOGIN_LOCAL  当前登录方式为家庭登陆
 */
public class Login {
    public enum State{
        LOGIN,LOGOUT
    }
    public enum Style{
        LOGIN_REMOTE,LOGIN_LOCAL
    }
}
