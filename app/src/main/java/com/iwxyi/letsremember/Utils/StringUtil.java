package com.iwxyi.letsremember.Utils;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 能否匹配正则表达式，用来判断输入的结构是否正确
     * @param string  字符串
     * @param pattern 正则表达式格式
     * @return 能否匹配
     */
    public static boolean canMatch(String string, String pattern) {
        if ("".equals(string)) return false;
        return Pattern.matches(pattern, string);
    }

    /**
     * 从XML中读取文本内容列表
     * @param str 字符串
     * @param tag 标签
     * @return 文本列表
     */
    public static ArrayList<String> getXmls(String str, String tag) {
        ArrayList<String> result = new ArrayList<>();
        String startTag = "<" + tag + ">";
        String endTag = "</" + tag + ">";
        int left = 0, right = 0;
        while (right != -1) {
            left = str.indexOf(startTag, right);
            if (left == -1) break;
            left += startTag.length();
            right = str.indexOf(endTag, left);
            if (right == -1) break;
            String mid = str.substring(left, right);
            result.add(mid);
        }
        return result;
    }

    /**
     * 从字符串中获取一个字符串
     * @param str 字符串
     * @param tag 标签
     * @return 一个字符串
     */
    public static String getXml(String str, String tag) {
        String result = "";
        String startTag = "<" + tag + ">";
        String endTag = "</" + tag + ">";
        int left = str.indexOf(startTag);
        if (left == -1) return result;
        left += startTag.length();
        int right = str.indexOf(endTag, left);
        if (right == -1) return result;
        result = str.substring(left, right);
        return result;
    }

    /**
     * 在XML文本中读取一个整型的数值
     * @param str 字符串
     * @param tag 标签
     * @return 整数
     */
    public static int getXmlInt(String str, String tag) {
        String s = getXml(str, tag);
        if ("".equals(s)) return 0;
        return Integer.parseInt(s);
    }

    /**
     * 从XML文本中读取一个长整数型的字符串
     * @param str XML文本
     * @param tag 标签
     * @return 长整数型
     */
    public static long getXmlLong(String str, String tag) {
        String s = getXml(str, tag);
        if ("".equals(s)) return 0;
        return Long.parseLong(s);
    }

    /**
     * 从XML格式的文本中读取一个布尔型的数值，1是true，其余都是false
     * @param str 字符串
     * @param tag 标签
     * @return 布尔型数值
     */
    public static boolean getXmlBoolean(String str, String tag) {
        String s = getXml(str, tag);
        if ("".equals(s) || "0".equals(s) || "false".equals(s) || "False".equals(s) || "FALSE".equals(s)) return false;
        return "1".equals(s);
    }

    /**
     * 从XML文本从读取一个double类型的数值
     * @param str 字符串
     * @param tag 标签
     * @return double
     */
    public static double getXmlDouble(String str, String tag) {
        String s = getXml(str, tag);
        if ("".equals(s)) return 0.0;
        return Double.parseDouble(s);
    }

    /**
     * 将数据转换成XML
     * @param str 字符串
     * @param tag 标签
     * @return 结果XML
     */
    public static String toXml(String str, String tag) {
        return "<" + tag + ">" + str + "</" + tag + ">";
    }

    /**
     * 将数据转换成XML
     * @param val 数值
     * @param tag 标签
     * @return 结果XML
     */
    public static String toXml(int val, String tag) {
        return "<" + tag + ">" + val + "</" + tag + ">";
    }

    /**
     * 将数据转换成XML
     * @param val 数值
     * @param tag 标签
     * @return 结果XML
     */
    public static String toXml(long val, String tag) {
        return "<" + tag + ">" + val + "</" + tag + ">";
    }

    /**
     * 将数据转换成XML
     * @param val 数值
     * @param tag 标签
     * @return 结果XML
     */
    public static String toXml(boolean val, String tag) {
        return "<" + tag + ">" + (val?1:0) + "</" + tag + ">";
    }

    /**
     * 将数据转换成XML
     * @param val 数值
     * @param tag 标签
     * @return 结果XML
     */
    public static String toXml(double val, String tag) {
        return "<" + tag + ">" + val + "</" + tag + ">";
    }
}

