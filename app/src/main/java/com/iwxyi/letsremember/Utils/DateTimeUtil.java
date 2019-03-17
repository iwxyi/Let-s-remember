package com.iwxyi.letsremember.Utils;

/**
 * @Author: mrxy001
 * @Time: 2019
 * @Change: 2019.3.16
 */

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

    /**
     * 获取时间戳
     * @return 现行时间戳（10 位数，精确到秒）
     */
    public static int getTimestamp() {
        // 获取的是 13 位数，除以 1000 来获取 10位数的时间戳
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * 获取时间戳
     * @return 现行时间戳（13 位数，精确到毫秒）
     */
    public static long getMillisTimestamp() {
        return System.currentTimeMillis();
    }

    /********************************10位时间戳转具体数值**********************************/

    /**
     * 时间戳转换到年
     * @param timestamp 时间戳
     * @return 年
     */
    public static int getYearFromTimestamp(int timestamp) {
        if (timestamp == 0) return 0;
        long l = timestamp * 1000L;
        String s = null;
        s = timestampToString(l, "yyyy");
        return Integer.parseInt(s);
    }

    /**
     * 时间戳转换到月
     * @param timestamp 时间戳
     * @return 月
     */
    public static int getMonthFromTimestamp(int timestamp) {
        if (timestamp == 0) return 0;
        long l = timestamp * 1000L;
        String s = null;
        s = timestampToString(l, "MM"); // 大写M是月份，小写m是分钟
        return Integer.parseInt(s);
    }

    /**
     * 时间戳转换到日
     * @param timestamp 时间戳
     * @return 日
     */
    public static int getDateFromTimestamp(int timestamp) {
        if (timestamp == 0) return 0;
        long l = timestamp * 1000L;
        String s = null;
        s = timestampToString(l, "dd");
        return Integer.parseInt(s);
    }

    /**
     * 时间戳转换到小时
     * @param timestamp 时间戳
     * @return 小时
     */
    public static int getHourFromTimestamp(int timestamp) {
        if (timestamp == 0) return 0;
        long l = timestamp * 1000L;
        String s = null;
        s = timestampToString(l, "hh"); // 大写HH是24小时制
        return Integer.parseInt(s);
    }

    /**
     * 时间戳转换到分钟
     * @param timestamp 时间戳
     * @return 分钟
     */
    public static int getMinuteFromTimestamp(int timestamp) {
        if (timestamp == 0) return 0;
        long l = timestamp * 1000L;
        String s = null;
        s = timestampToString(l, "mm");
        return Integer.parseInt(s);
    }

    /**
     * 时间戳转换到秒
     * @param timestamp 时间戳
     * @return 秒
     */
    public static int getSecondFromTimestamp(int timestamp) {
        if (timestamp == 0) return 0;
        long l = timestamp * 1000L;
        String s = null;
        s = timestampToString(l, "ss");
        return Integer.parseInt(s);
    }

    /************************************13位时间戳转具体数值************************************/

    /**
     * 时间戳转换到年
     * @param timestamp 时间戳
     * @return 年
     */
    public static int getYearFromTimestamp(long timestamp) {
        if (timestamp == 0) return 0;
        String s = null;
        s = timestampToString(timestamp, "yyyy");
        return Integer.parseInt(s);
    }

    /**
     * 时间戳转换到月
     * @param timestamp 时间戳
     * @return 月
     */
    public static int getMonthFromTimestamp(long timestamp) {
        if (timestamp == 0) return 0;
        String s = null;
        s = timestampToString(timestamp, "MM"); // 大写M是月份，小写m是分钟
        return Integer.parseInt(s);
    }

    /**
     * 时间戳转换到日
     * @param timestamp 时间戳
     * @return 日
     */
    public static int getDateFromTimestamp(long timestamp) {
        if (timestamp == 0) return 0;
        String s = null;
        s = timestampToString(timestamp, "dd");
        return Integer.parseInt(s);
    }

    /**
     * 时间戳转换到小时
     * @param timestamp 时间戳
     * @return 小时
     */
    public static int getHourFromTimestamp(long timestamp) {
        if (timestamp == 0) return 0;
        String s = null;
        s = timestampToString(timestamp, "hh"); // 大写HH是24小时制？
        return Integer.parseInt(s);
    }

    /**
     * 时间戳转换到分钟
     * @param timestamp 时间戳
     * @return 分钟
     */
    public static int getMinuteFromTimestamp(long timestamp) {
        if (timestamp == 0) return 0;
        String s = null;
        s = timestampToString(timestamp, "mm");
        return Integer.parseInt(s);
    }

    /**
     * 时间戳转换到秒
     * @param timestamp 时间戳
     * @return 秒
     */
    public static int getSecondFromTimestamp(long timestamp) {
        if (timestamp == 0) return 0;
        String s = null;
        s = timestampToString(timestamp, "ss");
        return Integer.parseInt(s);
    }

    /**************************转换成字符串*******************************/

    /**
     * 散落的数字转换成文本（填充0）
     * @param year
     * @param month
     * @param date
     * @return
     */
    public static String dateToString(int year, int month, int date) {
        String rst = year + "/";
        if (month < 10)
            rst += "0";
        rst += month + "/";
        if (date < 10)
            rst += "0";
        rst += date;
        return rst;
    }

    public static String valuesToString(int year, int month, int date) {
        return dateToString(year, month, date);
    }

    /**
     * 散落的时间转换到字符串
     * @param hour
     * @param minute
     * @return
     */
    public static String timeToString(int hour, int minute) {
        String rst = "";
        if (hour < 10)
            rst += "0";
        rst += hour + ":";
        if (minute < 10)
            rst += "0";
        rst += minute;
        return rst;
    }

    public static String valuesToString(int hour, int minute) {
        return timeToString(hour, minute);
    }

    public static String valuesToString(int year, int month, int date, int hour, int minute) {
        return valuesToString(year, month, date) + " " + valuesToString(hour, minute);
    }

    /***************************************转换成时间戳、时间戳/Date转换***********************************/

    /**
     * 散落的日期时间转换到具体的时间戳
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static long datetimeToTimestamp(int year, int month, int date, int hour, int minute, int second) {
        String o = year + "-" + month+"-"+date + " " + hour + ":" + minute + ":" + second;
        if (year == 0)
            return 0;

        Date d = null;
        d = stringToDate(o, "yyyy-MM-dd HH:mm:ss");
        return d.getTime();
    }

    public static long valuesToTimestamp(int year, int month, int date, int hour, int minute, int second) {
        return datetimeToTimestamp(year, month, date, hour, minute, second);
    }

    public static int valuesToTimestamp(int year, int month, int date, int hour, int minute) {
        long timestamp = datetimeToTimestamp(year, month, date, hour, minute, 0);
        return (int)(timestamp/1000);
    }

    public static String timestampToString(int timestamp, String formatType) {
        return timestampToString(timestamp*1000L, formatType);
    }

    public static Date timestampToDate(int timestamp, String formatType) {
        return DateTimeUtil.timestampToDate(timestamp, formatType);
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String timestampToString(long timestamp, String formatType) {
        Date date = null; // long类型转成Date类型
        date = timestampToDate(timestamp, formatType);
        return dateToString(date, formatType);
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss //yyyy年MM月dd日 HH时mm分ss秒
    public static Date timestampToDate(long timestamp, String formatType) {
        Date dateOld = new Date(timestamp); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = null; // 把String类型转换为Date类型
        date = stringToDate(sDateTime, formatType);
        return date;
    }

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    @SuppressLint("SimpleDateFormat")
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss
    // yyyy年MM月dd日 HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /*********************************获取具体的数值**********************************/

    public static int getYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    public static int getDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    public static int getHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR);
    }

    public static int getMinute() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    public static int getSecond() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.SECOND);
    }
}
