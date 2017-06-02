package com.yjtse.lamp;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;

public class Config {
    public static final String BAIDU_COM = "http://www.baidu.com";
    /**
     * 是否开启debug模式
     */
    public static final boolean DEBUG = false;
    public static final String DEBUG_FILE_NAME = "/LAMP_DEBUG.txt";
    public static final int CAMERA_REQUEST_CODE = 0;
    public static final String BmobSMSToken = "205241f79a98efbd59fafc6f6c5187fb";
    /**
     * 服务器的路径
     */
    public static final String SERVER_URL = "http://60.205.219.43/lamp";//服务器路径http://icloud.ticachina.com
//    public static final String SERVER_URL = "http://192.168.191.1";//服务器路径http://icloud.ticachina.com

    public static final String KEY_REMEMBER_PWD = "remember_password";
    //请求参数键
    public static final String KEY_USERNAME = "userId";
    public static final String KEY_PASSWORD = "userPass";
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
    /**
     * 发送短信到来的消息
     */
    public static final int MESSAGE_WHAT_SMS_OBSERVER_REGIST = 100;
    /**
     * 发送删除设备的消息
     */
    public static final int MESSAGE_WHAT_DEC_DEVICE = 101;


    //HTTP消息，用于发送系统消息的what值，从100开始
    /**
     * 添加设备消息
     */
    public static final int MESSAGE_WHAT_ADD_DEVICE = 102;
    /**
     * 更新设备状态信息
     */
    public static final int MESSAGE_WHAT_UPDATE_DEVICE_STATUS = 109;
    /**
     * 启用停用timer message
     */
    public static final int MESSAGE_WHAT_UPDATE_TIMER_AVAILABLE = 110;
    /**
     * 更新定时设置状态信息
     */
    public static final int MESSAGE_WHAT_UPDATE_TIMER = 120;
    /**
     * 定时添加状态信息
     */
    public static final int MESSAGE_WHAT_ADD_TIMER = 130;
    /**
     * 定时删除状态信息
     */
    public static final int MESSAGE_WHAT_DELETE_TIMER = 140;
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
     * 添加设备action
     * 携带参数socketId,ownerId,等
     */
    public static final String ACTION_ADD_USER = "user/register";

    /**
     * 查询用户action
     * 必须携带的参数
     * params: userId
     */
    public static final String ACTION_GET_USER = "user/query";
    /**
     * 登录请求的action
     * 必须携带的参数
     * *params: userId
     * params: userPass
     */
    public static final String ACTION_LOGIN = "user/login";
    /**
     * 修改用户信息
     * 必须携带的参数
     * params: userId
     * params: userPass
     */
    public static final String ACTION_USER_UPDATE = "user/update";
    /**
     * 改密码
     * params: userId
     * params: userPass
     */
    public static final String ACTION_USER_PASS_UPDATE = "user/updatePass";
    /**
     * 添加设备action
     * 携带参数socketId,ownerId,等
     */
    public static final String ACTION_ADD_DEVICE = "socket/register";
    /**
     * 查询设备action
     */
    public static final String ACTION_GET_DEVICE = "socket/query";
    /**
     * 查询所有设备action
     * 参数：ownerId
     */
    public static final String ACTION_GET_ALL_DEVICE = "socket/queryAll";
    /**
     * 修改设备信息
     * 携带参数socketId,ownerId，socketName,status等
     */
    public static final String ACTION_DEVICE_UPDATE = "socket/update";
    /**
     * 删除设备
     */
    public static final String ACTION_DEVICE_DELETE = "socket/delete";
    /**
     * 设置设备定时action
     * <p>
     * (value = "localDateTime") String localDateTime,
     * (value = "statusTobe") String statusTobe,
     * (value = "socketId") String socketId,
     * (value = "ownerId") String ownerId) {
     */
//    public static final String ACTION_DEVICE_TIMING = "socket/updateCron";
    public static final String ACTION_CRON_GET_ALL_BY_SOCKET_ID = "cron/queryBySocketId";

    public static final String ACTION_CRON_GET_ALL_BY_ID = "cron/query";

    public static final String ACTION_CRON_GET_ALL_BY_OWNER_ID = "cron/queryAllByOwnerId";

    /**
     * (value = "socketId") String socketId,
     * (value = "ownerId") String ownerId,
     * (value = "cron") String cron,
     * (value = "statusTobe") String statusTobe)
     */
    public static final String ACTION_CRON_ADD = "cron/addCron";
    public static final String ACTION_CRON_UPDATE = "cron/update";

    public static final String ACTION_CRON_DELETE_BY_ID = "cron/delete";
    public static final String ACTION_CRON_DELETE_BY_OWNER_ID = "cron/deleteByOwnerId";
    public static final String ACTION_CRON_DELETE_BY_SOCKET_ID = "cron/deleteBySocketId";


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

    /**
     * 得到相应action请求的URL路径
     *
     * @param action 需要请求的action
     * @return URL路径
     */
    public static String getRequestURL(String action) {
        return SERVER_URL + "/" + action;//得到服务器路径加上请求的动作
    }

}
