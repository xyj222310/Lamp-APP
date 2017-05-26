package com.yjtse.lamp.parser;

import com.yjtse.lamp.Config;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

    /**
     * 解析登录服务器返回的JSON数据，方法返回服务器给出的token值
     *
     * @param jsonResult 服务器发出的JSON数据
     * @return token值
     */
    public static String parseLogin(String jsonResult) {

        try {
            JSONObject obj = new JSONObject(jsonResult);
//            String token = obj.getString(Config.KEY_TOKEN);
//            if (!TextUtils.isEmpty(token)) {
//                return token;
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解析注册用户服务器返回的数据格式，方法返回服务器是否注册成功
     *
     * @param jsonResult 服务器回传的Json字符串
     * @return 服务器是否注册成功
     */
    public static boolean parseRegist(String jsonResult) {
        try {
            JSONObject obj = new JSONObject(jsonResult);
            int status = Integer.valueOf(obj.getString(Config.KEY_STATUS));
            if (status == Config.RESULT_STATUS_SUCCESS) {
                return true;
            } else {
                return false;
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return false;
    }

    /**
     * 解析重置用户密码服务器返回的数据格式，方法返回服务器是否重置成功
     *
     * @param jsonResult 服务器回传的Json字符串
     * @return 服务器回传的Json字符串
     */
    public static boolean parseReplace(String jsonResult) {
        try {
            JSONObject obj = new JSONObject(jsonResult);
            int status = obj.getInt(Config.KEY_STATUS);
            if (status == 1) {
                return true;
            } else {
                return false;
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return false;
    }

    /**
     * 用户已经存在，返回true
     * 用户不存在，返回false
     *
     * @param result
     * @return
     */
    public static boolean parseUsernameIsExist(String result) {
        try {
            JSONObject object = new JSONObject(result);
            int res = Integer.valueOf(object.getString(Config.KEY_STATUS));
            if (res == Config.RESULT_STATUS_SUCCESS) { //存在
                return true;
            } else { //不存在
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }


    private static int checkRemoteInsideIsOffLine(JSONObject object) {
        try {
            int online = Integer.valueOf(object.getString("ONLINE"));
            int mode = object.getInt("S1");
            if (online == 0 || mode == 6) {
                return 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 2;
    }


}
