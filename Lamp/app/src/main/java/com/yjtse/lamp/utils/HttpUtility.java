//package com.yjtse.lamp.utils;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by XieYingjie on 2017/5/29/0029.
// */
//
//
//public class HttpUtility {
//
//
//    /**
//     * 图片资源缓存
//     */
//    private static Map<String, Bitmap> bitmapCache = new HashMap<>();
//
//    /**
//     * 获取网落图片资源
//     * @param url
//     * @return
//     */
//    public static Bitmap getHttpBitmap(String url) {
//        //先从缓存里找
//        Bitmap bitmap = bitmapCache.get(url);
//        if (bitmap != null) {
//            return bitmap;
//        }
//
//        //从网络上下载
//        URL myFileURL;
//        try {
//            myFileURL = new URL(url);
//            //获得连接
//            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
//            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
//            conn.setConnectTimeout(6000);
//            //连接设置获得数据流
//            conn.setDoInput(true);
//            //不使用缓存
//            conn.setUseCaches(false);
//            //这句可有可无，没有影响
//            //conn.connect();
//            //得到数据流
//            InputStream is = conn.getInputStream();
//            //解析得到图片
//            bitmap = BitmapFactory.decodeStream(is);
//            //关闭数据流
//            is.close();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (bitmap != null) {
//            bitmapCache.put(url, bitmap);
//        }
//
//        return bitmap;
//
//    }
//}