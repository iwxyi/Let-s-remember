package com.iwxyi.letsremember.Material;


import android.util.Log;

import com.iwxyi.letsremember.Globals.User;
import com.iwxyi.letsremember.Utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/*
 * 记忆卡片 JavaBean
 *
 * @author MRXY001
 * @date   2019/3/27 20 49
 * @email  wxy19980615@gmail.com
 */
public class CardBean extends StringUtil {
    String content;
    String description;
    String label;
    int box = 0;
    ArrayList<PlaceBean>places = new ArrayList<>();
    ArrayList<Integer>reads = new ArrayList<>();

    public CardBean(String str) {
        initFromStr(str);
    }

    /**
     * 从字符串中读取
     * @param str
     */
    private void initFromStr(String str) {
        places.clear();
        reads.clear();

        content = getXml(str, "content").trim();
        description = getXml(str, "description").trim();
        label = getXml(str, "label").trim();
        box = getXmlInt(str, "box");

        String places_str = getXml(str, "hides").trim();
        String[] places_list_str = places_str.split(",");
        int size = places_list_str.length;
        for (int i = 0; i < size-1; i+=2) {
            places.add(new PlaceBean(places_list_str[i], places_list_str[i+1]));
        }

        String reads_str = getXml(str, "reads").trim();
        String[] reads_list_str = reads_str.split(",");
        size = reads_list_str.length;
        for (int i = 0; i < size; i++) {
            if (!reads_list_str[i].isEmpty())
                reads.add(Integer.parseInt(reads_list_str[i]));
        }
    }

    /**
     * 转化成一个带card标签的字符串
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

        StringBuilder reads_build = new StringBuilder();
        size = reads.size();
        for (int i = 0; i < size; i++) {
            reads_build.append(reads.get(i)).append(",");
        }
        if (reads_build.length() > 0)
            reads_build = new StringBuilder(reads_build.substring(0, reads_build.length()-1));

        all += toXml(content, "content")
                        + toXml(description, "description")
                        + toXml(label, "label")
                        + toXml(places_build.toString(), "hides")
                        + toXml(reads_build.toString(), "reads")
                        + toXml(box, "box");

        return toXml(all, "card");
    }

    public void setContent(String c) {
        this.content = c;
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
            } else if (place.start <= start && place.end >= start && place.end < end) { // 扩大右边
                place.end = end;
                while (i < size - 1 && places.get(i + 1).start <= end) { // 循环删除右边重复的
                    end = Math.max(end, places.get(i+1).end);
                    place.end = end;
                    places.remove(i+1);
                    size--;
                }
                places.set(i, place);
                break;
            } else if (place.start > start && place.start <= end && place.end >= end) { // 扩大左边
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
                    if (i == 0 && places.get(i).start > start) { // 插入到开头
                        Log.i("====increaseHide", "Add to first");
                        places.add(0, new PlaceBean(start, end));

                        ++size;
                        while (++i < size && places.get(i).start <= end) {
                            int end2 = places.get(i).end;
                            end = end > end2 ? end : end2;
                            places.get(0).end = end;
                            Log.i("====increaseHide", "合并：" + i + " : " + places.get(i).start + ", " + places.get(i).end);
                            places.remove(i);
                            i--;
                            size--;
                        }
                        break;
                    } else if (i == size - 1) {
                        Log.i("====increaseHide", "Add to last");
                        places.add(new PlaceBean(start, end));
                        break;
                    } else if (places.get(i).start < start && places.get(i + 1).start > start) {
                        Log.i("====increaseHide", "Add to "+ (++i));
                        places.add(i, new PlaceBean(start, end));
                        int index = i;

                        ++size;
                        while (++i < size && places.get(i).start <= end) {
                            int end2 = places.get(i).end;
                            end = end > end2 ? end : end2;
                            places.get(index).end = end;
                            Log.i("====increaseHide", "合并：" + i + " : " + places.get(i).start + ", " + places.get(i).end);
                            places.remove(i);
                            i--;
                            size--;
                        }
                        break;
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
                places.remove(i--);
                size--;
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

    /**
     * 添加阅读时间，10位时间戳格式
     * 默认应该都是从小到大排序的吧
     * @param timestamp
     */
    public void addRememberTimestamp(int timestamp) {
        int size = reads.size();
        if (size == 0) {
            reads.add(timestamp);
            User.addRecite(getContent()); // 添加用户背诵的数量
            return ;
        }
        int prev = reads.get(size-1);
        if (prev <= timestamp && prev + 60 > timestamp) { // 一分钟以内，删除上一次的
            reads.remove(size-1);
        }
        reads.add(timestamp);
    }

    public void setBox(int box) {
        Log.i("====设置盒子", getContent()+box);
        this.box = box;
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
