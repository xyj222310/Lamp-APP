package com.yjtse.lamp.utils;

/**
 * Created by XieYingjie on 2017/5/29/0029.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CronDateUtils {

    /**
     * 日期转化为cron表达式
     *
     * @param date
     * @return
     */
    public static String getCron(java.util.Calendar date) {
        String dateFormat = "ss mm HH dd MM ? yyyy";
        return CronDateUtils.fmtDateToStr(date, dateFormat);
    }

    /**
     * cron表达式转为日期
     *
     * @param cron
     * @return
     */
    public static Date getCronToDate(String cron) {
        String dateFormat = "ss mm HH dd MM ? yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = sdf.parse(cron);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }

    /**
     * Description:格式化日期,Date转化为String字符串
     *
     * @param date
     * @param dtFormat 例如:yyyy-MM-dd HH:mm:ss yyyyMMdd
     * @return
     */
    public static String fmtDateToStr(Calendar date, String dtFormat) {
        if (date == null)
            return "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(dtFormat);
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}