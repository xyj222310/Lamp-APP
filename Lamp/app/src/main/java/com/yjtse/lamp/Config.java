package com.yjtse.lamp;

import android.content.Context;
import android.os.Environment;

import com.yjtse.lamp.domain.Login;
import com.yjtse.lamp.utils.SharedPreferencesUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;

public class Config {

    /**
     * 登录状态
     */
    public static Login.State loginState;

    /**
     * 登录方式
     */
    public static Login.Style loginStyle;
    //上面两个静态变量不科学，但是暂时没有更好的解决方式

    /**
     * 是否开启debug模式
     */
    public static final boolean DEBUG = false;
    public static final String DEBUG_FILE_NAME = "/TICA_DEBUG.txt";
    public static File DEBUG_FILE = null;

    public static void writeToDebug(String msg) {
        if (DEBUG) {
            try {
                if (DEBUG_FILE == null) {
                    Config.DEBUG_FILE = new File(Environment.getExternalStorageDirectory(), Config.DEBUG_FILE_NAME);
                }
                //第二个参数意义是说是否以append方式添加内容
                BufferedWriter bw = new BufferedWriter(new FileWriter(Config.DEBUG_FILE, true));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(System.currentTimeMillis());
                bw.write(dateString);
                bw.write(" " + msg);
                bw.write("\r\n");
                bw.flush();
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Config.DEBUG_FILE = new File(Environment.getExternalStorageDirectory(), Config.DEBUG_FILE_NAME);
            if (Config.DEBUG_FILE.exists()) {
                Config.DEBUG_FILE.delete();
            }
        }
    }

    public static final String BmobSMSToken = "e1ece4992023d2886aae2920f0648162";
    /**
     * 服务器的路径
     */
    public static final String SERVER_URL = "http://60.205.219.43";//服务器路径http://icloud.ticachina.com

    /**
     * TCP通信端口
     */
    public static final int TCP_PORT = 9999;
    /**
     * UDP通信端口
     */
    public static final int UDP_PORT = 6500;

    //请求参数键
    public static final String KEY_TOKEN = "sessionId";
    public static final String KEY_USERNAME = "userId";
    public static final String KEY_PASSWORD = "userPass";
    public static final String KEY_REMEMBER_PWD = "remember_password";
    public static final String KEY_MOBILE_LOGIN = "mobileLogin";

    /**
     * 本地存储xml文件名
     */
    public static final String APP_ID = "com.example.frame";
    public static final String APP_DATABASE_NAME = "com_example_frame";

    //JSON返回字段
    public static final String KEY_STATUS = "state";
    public static final String KEY_MESSAGE = "message";

    public static final String KEY_APK_VERSION = "version";

    /**
     * 系统错误
     */
    public static final int RESULT_STATUS_SYSTEM_ERROR = 0;
    /**
     * 访问成功
     */
    public static final int RESULT_STATUS_SUCCESS = 1;
    /**
     * 访问失败
     */
    public static final int RESULT_STATUS_FAIL = 2;
    /**
     * Token失效
     */
    public static final int RESULT_STATUS_INVALID_TOKEN = 3;


    //HTTP消息，用于发送系统消息的what值，从100开始
    /**
     * 发送短信到来的消息
     */
    public static final int MESSAGE_WHAT_SMS_OBSERVER_REGIST = 100;
    /**
     * 发送删除设备的消息
     */
    public static final int MESSAGE_WHAT_DEC_DEVICE = 101;
    /**
     * 添加设备消息
     */
    public static final int MESSAGE_WHAT_ADD_DEVICE = 102;
    /**
     * 查找用户已经存在
     */
    public static final int MESSAGE_WHAT_HTTP_FIND_USERNAME_EXIST = 103;
    /**
     * 查找用户不存在
     */
    public static final int MESSAGE_WHAT_HTTP_FIND_USERNAME_NOT_EXIST = 104;
    /**
     * 注册成功
     */
    public static final int MESSAGE_WHAT_HTTP_REGISTER_SUCCESS = 105;
    /**
     * 注册失败
     */
    public static final int MESSAGE_WHAT_HTTP_REGISTER_FAIL = 106;
    /**
     * 登录成功
     */
    public static final int MESSAGE_WHAT_HTTP_LOGIN_SUCCESS = 107;
    /**
     * 登录失败
     */
    public static final int MESSAGE_WHAT_HTTP_LOGIN_FAIL = 108;
    /**
     * 查询网关数量成功
     */
    public static final int MESSAGE_WHAT_HTTP_SEARCH_GATEWAY_SUCCESS = 109;
    /**
     * 查询网关数量失败
     */
    public static final int MESSAGE_WHAT_HTTP_SEARCH_GATEWAY_FAIL = 110;
    /**
     * 查询IP地址和端口号成功
     */
    public static final int MESSAGE_WHAT_HTTP_SEARCH_IPPORT_SUCCESS = 111;
    /**
     * 查询IP地址和端口号成功
     */
    public static final int MESSAGE_WHAT_HTTP_SEARCH_IPPORT_FAIL = 112;
    /**
     * 修改密码成功
     */
    public static final int MESSAGE_WHAT_HTTP_REPLACE_SUCCESS = 119;
    /**
     * 修改密码失败
     */
    public static final int MESSAGE_WHAT_HTTP_REPLACE_FAIL = 120;


    //UDP消息，用于发送系统消息的what值，从200开始
    /**
     * 设置SSID和PWD成功消息
     */
    public static final int MESSAGE_WHAT_UDP_SET_SSID_SUCCESS = 200;
    /**
     * 设置SSID和PWD失败消息
     */
    public static final int MESSAGE_WHAT_UDP_SET_SSID_FAIL = 201;
    /**
     * 查询个数成功消息
     */
    public static final int MESSAGE_WHAT_UDP_SEARCH_SUCCESS = 202;
    /**
     * 查询个数失败消息
     */
    public static final int MESSAGE_WHAT_UDP_SEARCH_FAIL = 203;


    /**
     * 查询用户action
     */
    public static final String ACTION_GET_USER = "user";

    /**
     * 登录请求的action
     */
    public static final String ACTION_LOGIN = "user/login";

    /**
     * 注册请求的action
     */
    public static final String ACTION_USER_REGISTER = "user/register";

    /**
     * 修改用户信息
     */
    public static final String ACTION_USER_UPDATE = "user/update";


    /**
     * 添加设备action
     */
    public static final String ACTION_ADD_DEVICE = "socket/register";


    /**
     * 查询设备action
     */
    public static final String ACTION_GET_DEVICE = "socket";

    /**
     * 修改设备信息
     */
    public static final String ACTION_DEVICE_UPDATE = "socket/update";

    /**
     * 设置设备定时action
     */
    public static final String ACTION_DEVICE_TIMING = "socket/updateCron";

    /**
     * 得到相应action请求的URL路径
     *
     * @param action 需要请求的action
     * @return URL路径
     */
    public static String getRequestURL(String action) {
        return SERVER_URL + "/" + action;//得到服务器路径加上请求的动作
    }

    public static String getAfterLoginRequestURL(Context context, String action) {
        String token = (String) SharedPreferencesUtil.query(context, KEY_TOKEN, "String");
        String url = SERVER_URL + "/" + action + ";" + "JSESSIONID=" + token + "?__ajax=true";
        return url;
    }

}
