package com.iwxyi.letsremember.Utils;

import com.iwxyi.letsremember.Globals.App;

/*
 * 自制XML解析器
 * 仅适用于只有一级列表的情况，网络传输
 * @author MRXY001
 * @date   2019/3/17 15 13
 * @email  wxy19980615@gmail.com
 */
public class XmlParser {
     private String full;
     private String[] list;

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
        list = (String[])StringUtil.getXmls(full, tag).toArray();
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
