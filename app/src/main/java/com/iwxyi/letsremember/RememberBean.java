package com.iwxyi.letsremember;


import com.iwxyi.letsremember.Utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/*
 * File Description
 *
 * @author MRXY001
 * @date   2019/3/27 20 49
 * @email  wxy19980615@gmail.com
 */
public class RememberBean extends StringUtil {
    String positive_content;
    String positive_description;
    String reverse_content;
    String reverse_description;
    ArrayList<PlaceBean>positive_places = new ArrayList<>();
    ArrayList<PlaceBean>reverse_places = new ArrayList<>();

    public RememberBean(String str) {
        initFromStr(str);
    }

    /**
     * 从字符串中读取
     * @param str
     */
    private void initFromStr(String str) {
        positive_places.clear();
        reverse_places.clear();

        String positive_all = getXml(str, "positive");
        positive_content = getXml(positive_all, "content");
        positive_description = getXml(positive_all, "description");
        String place_str = getXml(positive_all, "hides");
        String[] places_str = place_str.split(",");
        int size = places_str.length;
        for (int i = 0; i < size-1; i+=2) {
            positive_places.add(new PlaceBean(places_str[i], places_str[i+1]));
        }

        String reverse_all = getXml(str, "reverse");
        reverse_content = getXml(reverse_all, "content");
        reverse_description = getXml(reverse_all, "description");
        place_str = getXml(reverse_all, "hides");
        places_str = place_str.split(",");
        size = places_str.length;
        for (int i = 0; i < size-1; i+=2) {
            reverse_places.add(new PlaceBean(places_str[i], places_str[i+1]));
        }
    }

    /**
     * 转化成一个带chapter标签的字符串
     * @return
     */
    public String toString() {
        String all = "<chapter>";
        String places = new String();
        int size = positive_places.size();
        for (int i = 0; i < size; i++) {
            places += positive_places.get(i).toString() + ",";
        }
        places = places .substring(0, places.length()-1);
        all += toXml(toXml(positive_content, "content")
                        + toXml(positive_description, "description")
                        + places
                , "positive");

        size = reverse_places.size();
        for (int i = 0; i < size; i++) {
            places += reverse_places.get(i).toString() + ",";
        }
        places = places .substring(0, places.length()-1);
        all += toXml(toXml(reverse_content, "content")
                        + toXml(reverse_description, "description")
                        + places
                ,"reverse");
        return all;
    }

    /**
     * 添加隐藏区域(可能重复)
     * @param start
     * @param end
     */
    public void increaseHide(int start, int end) {

    }

    /**
     * 添加显示的区域(可能重复)
     * @param start
     * @param end
     */
    public void increaseShow(int start, int end) {

    }

    /**
     * 存储隐藏位置的类
     */
    public class PlaceBean{
        int start, end;

        PlaceBean(int s, int e) {
            start = s;
            end = e;
        }

        PlaceBean(String s, String e) {
            start = Integer.parseInt(s);
            end = Integer.parseInt(e);
        }

        public String toString() {
            return String.format("%d,%d", start, end);
        }

    }
}
