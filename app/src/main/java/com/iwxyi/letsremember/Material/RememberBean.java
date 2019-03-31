package com.iwxyi.letsremember.Material;


import android.util.Log;

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
    String content;
    String description;
    ArrayList<PlaceBean>places = new ArrayList<>();

    public RememberBean(String str) {
        initFromStr(str);
    }

    /**
     * 从字符串中读取
     * @param str
     */
    private void initFromStr(String str) {
        places.clear();

        content = getXml(str, "content").trim();
        description = getXml(str, "description").trim();
        String place_str = getXml(str, "hides").trim();
        String[] places_str = place_str.split(",");
        int size = places_str.length;
        for (int i = 0; i < size-1; i+=2) {
            places.add(new PlaceBean(places_str[i], places_str[i+1]));
        }
    }

    /**
     * 转化成一个带chapter标签的字符串
     * @return
     */
    public String toString() {
        String all = "";
        StringBuilder places_build = new StringBuilder("");
        int size = places.size();
        for (int i = 0; i < size; i++) {
            places_build.append(places.get(i).toString()).append(",");
        }
        if (places_build.length() > 0)
            places_build = new StringBuilder(places_build.substring(0, places_build.length() - 1));
        all += toXml(content, "content")
                        + toXml(description, "description")
                        + toXml(places_build.toString(), "hides");
        return all;
    }

    /**
     * 添加隐藏区域(可能重复)
     * @param start
     * @param end
     */
    public void increaseHide(int start, int end) {
        Log.i("====increaseHide", "start:"+start+", end:"+end);
        int size = places.size();
        int i;
        for (i = 0; i < size; i++) {
            PlaceBean place = places.get(i);
            if (place.start <= start && place.end >= end) { // 已经存在了
                break;
            } else if (place.start <= start && place.end < end) { // 扩大右边
                place.end = end;
                while (i < size - 1 && places.get(i + 1).start <= end) { // 循环删除右边重复的
                    end = Math.max(end, places.get(i+1).end);
                    place.end = end;
                    places.remove(i+1);
                    size--;
                }
                places.set(i, place);
                break;
            } else if (place.start > start && place.end >= end) { // 扩大左边
                place.start = start;
                places.set(i, place);
                break;
            } else if (place.start >= start && place.end <= end) { // 扩大两边
                place.start = start;
                place.end = end;
                while (i < size - 1 && places.get(i + 1).start <= end) { // 循环删除右边重复的
                    end = Math.max(end, places.get(i+1).end);
                    place.end = end;
                    places.remove(i+1);
                    size--;
                }
                places.set(i, place);
                break;
            }
        }
        if (i == size) { // 没有变化，则按顺序插入到对应的位置
            Log.i("====increaseHide", "Add");
            if (size == 0) {
                places.add(new PlaceBean(start, end));
            } else {
                for (i = 0; i < size; i++) {
                    if (places.get(i).start < start && (i == size-1 || places.get(i + 1).start > start)) {
                        if (i == size-1) {
                            places.add(new PlaceBean(start, end));
                        } else {
                            places.add(i+1, new PlaceBean(start, end));
                        }
                    }
                }
            }

        }
    }

    /**
     * 减少隐藏的区域(很可能覆盖)
     * @param start
     * @param end
     */
    public void decreaseHide(int start, int end) {
        Log.i("====decreaseHide", "start:"+start+", end:"+end);
        int size = places.size();
        int i;
        for (i = 0; i < size; i++) {
            PlaceBean place = places.get(i);
            if (place.start == start && place.end == end) { // 刚好删除
                places.remove(i);
                break;
            } else if (place.start > start && place.end < end) { // 删除整个
                start = place.start; // 调整删除范围，继续下去
                places.remove(i);
            } else if (place.start < start && place.end == end) { // 刚好删除右边
                place.end = start;
                places.set(i, place);
                break;
            } else if (place.start == start && place.end > end) { // 刚好删除左边
                place.start = end;
                places.set(i, place);
                break;
            } else if (place.start < start && place.end > end) { // 删除中间
                PlaceBean place2 = new PlaceBean(end, place.end);
                place.end = start;
                places.set(i, place);
                if (i == size - 1) {
                    places.add(place2);
                } else {
                    places.add(i + 1, place2);
                }
                break;
            } else if (place.start <= start && place.end > start && place.end < end) { // 删除右边
                int temp = place.end;
                place.end = start;
                start = temp;
                places.set(i, place);
            } else if (place.start > start && place.start < end && place.end >= end) { // 删除左边
                int temp = place.start;
                place.start = end;
                end = temp;
                places.set(i, place);
                break;
            }
        }
        if (i == size) {
            Log.i("====decreaseHide", "None");
        }
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<PlaceBean> getPlaces() {
        return places;
    }

    /**
     * 存储隐藏位置的类
     */
    public class PlaceBean{
        public int start, end;

        PlaceBean(int s, int e) {
            start = s;
            end = e;
        }

        PlaceBean(String s, String e) {
            start = Integer.parseInt(s);
            end = Integer.parseInt(e);
        }

        public String toString() {
            return ""+start+","+end;
        }

    }
}
