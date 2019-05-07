package com.iwxyi.letsremember.Utils;

import android.util.Log;

import com.iwxyi.letsremember.Globals.App;

/*
 * 自制XML解析器
 * 仅适用于只有一级列表的情况，网络传输
 * @author MRXY001
 * @date   2019/3/17 15 13
 * @email  wxy19980615@gmail.com
 * @Update: 2019.5.7
 * - 优化列表解析器，单方向从字符串转成列表，并且保留字符串方法
 */
public class XmlParser {
     private String full;
     private String[] list;
     private int size = 0;
     private int current_index = -1;

    public XmlParser(String text) {
        full = text;
        list = new String[]{};
    }

    public XmlParser(String[] list) {
        this.list = list;
        full = "";
    }

    /****************** text *********************/

    public String get(String tag) {
        return StringUtil.getXml(full, tag);
    }

    public int getInt(String tag) {
        return StringUtil.getXmlInt(full, tag);
    }

    public long getLong(String tag) {
        return StringUtil.getXmlLong(full, tag);
    }

    public double getDouble(String tag) {
        return StringUtil.getXmlDouble(full, tag);
    }

    public boolean getBoolean(String tag) {
        return StringUtil.getXmlBoolean(full, tag);
    }

    /***************** list **************/

    public void split(String tag) {
        list = StringUtil.getXmls(full, tag).toArray(new String[0]);
    }

    public int size() {
        if (size == 0) {
            if (list.length == 0)
                return 0;
            return size = list.length;
        }
        return size;
    }

    public void setIndex(int x) {
        if (x < 0 || x >= list.length) {
            Log.e("====XmlParser列表错误", "数组下标溢出");
            return ;
        }
        full = list[x];
        current_index = x;
    }

    public String getItem(int index) {
        if (index < 0 || index >= list.length) {
            return "";
        }
        return list[index];
    }

    public int getItemInt(int index) {
        String i = getItem(index);
        if (i.isEmpty())
            return 0;
        int ans = Integer.parseInt(i);
        return ans;
    }

    /******************* tools *******************/

    /**
     * 网络传输是否成功，很有必要被重写
     * 判断条件：<result>1</result> 或者 <result>OK</result>
     * @return
     */
    public boolean isSuccess() {
        String result = get("result");
        return (result.equals("1") || result.equals("OK") || result.equals("OK") || result.equals("Ok"));
    }

    public boolean judgeSuccess() {
        if (isSuccess()) {
            return true;
        }
        String result = get("result");
        if (result.isEmpty()) {
            App.toast("网络连接失败");
        } else {
            App.toast(result);
        }
        return false;
    }
}
