package com.iwxyi.letsremember.Material;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.Globals.Paths;
import com.iwxyi.letsremember.Utils.FileUtil;
import com.iwxyi.letsremember.Utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardsContent {

    public static final List<CardItem> ITEMS = new ArrayList<CardItem>();
    public static final Map<String, CardItem> ITEM_MAP = new HashMap<String, CardItem>();
    public static int index = 0; // 当前卡片的位置索引

    public static void refreshCards() {
        String pack = App.getVal("last_package");
        String sect = App.getVal("last_section");
        if (pack.isEmpty() || sect.isEmpty()) {
            return ;
        }
        refreshCards(pack, sect);
    }

    public static void refreshCards(String pack, String sect) {
        if (pack.isEmpty()) {
            pack = App.getVal("last_pack");
            if (pack.isEmpty()) {
                return ;
            }
        }
        if (sect.isEmpty()) {
            sect = App.getVal("last_sect");
            if (sect.isEmpty()) {
                return ;
            }
        }

        ITEMS.clear();
        ITEM_MAP.clear();

        String path = Paths.getLocalPath("material/"+pack+"/"+sect+".txt");
        String text = FileUtil.readTextVals(path);
        ArrayList<String> cards_str = StringUtil.getXmls(text, "card");
        for (int i = 0; i < cards_str .size(); i++) {
            String content = StringUtil.getXml(cards_str.get(i), "content").trim();
            content = getContentTitle(content);
            String detail = getRecentRemember(StringUtil.getXml(cards_str.get(i), "reads").trim());
            addItem(createCardItem(i+1, content, detail));
        }
    }

    private static String getContentTitle(String c) {
        String ans = c;
        int pos = c.indexOf("\n");
        if (pos > -1) {
            ans = c.substring(0, pos);
        }
        if (ans.length() > 20)
            ans = ans.substring(0, 20);
        return ans;
    }

    private static String getRecentRemember(String str) {
        String[] places_slist = str .split(",");
        if (places_slist.length > 0 && !places_slist[0].isEmpty()) {
            int timestamp = Integer.parseInt(places_slist[places_slist.length-1]);
            if (timestamp > 0) {
                int now = App.getTimestamp();
                int delta = now - timestamp;

                int second = delta;
                if (second < 120)
                    return ""+second+"秒前";

                int minute = second / 60;
                if (minute < 120)
                    return ""+minute+"分钟前";

                int hour = minute / 60;
                if (hour < 48)
                    return ""+hour+"小时前";

                int day = hour / 24;
                if (day < 60)
                    return ""+day+"天前";

                int month = day / 30;
                if (month < 24)
                    return ""+month+"月前";

                int year = month / 12;
                if (year < 100)
                    return ""+year+"年前";

                return "很久很久以前";
            }
        }
        return "";
    }

    /**
     * 刷新位置列表，cards的下标索引
     * @param x
     */
    public static void setIndex(int x) {
        /*if (index > 0 && index < ITEMS.size()) {
            CardItem item = ITEMS.get(index);
            item.details = "";
        }*/
        index = x;
        /*if (index > 0 && index < ITEMS.size()) {
            CardItem item = ITEMS.get(index);
            item.details = "    当前";
        }*/
    }

    private static void addItem(CardItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static CardItem createCardItem(int position, String card_name, String detail) {
        return new CardItem(String.valueOf(position), card_name, detail);
    }

    public static class CardItem {
        public final String id;
        public final String content;
        public String details;

        public CardItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
