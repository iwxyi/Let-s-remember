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

    private static final int COUNT = 25;

    static {

    }

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

        String last_card = App.getVal("last_card");
        String path = Paths.getLocalPath("material/"+pack+"/"+sect+".txt");
        String text = FileUtil.readTextVals(path);
        ArrayList<String> cards_str = StringUtil.getXmls(text, "card");
        for (int i = 0; i < cards_str .size(); i++) {
            String name = StringUtil.getXml(cards_str.get(i), "name");
            String detail = "";
            if (i == index)
                detail = "    当前";
            addItem(createCardItem(i, detail, detail));
        }
    }

    /**
     * 刷新位置列表，cards的下标索引
     * @param x
     */
    public static void setIndex(int x) {
        if (index > 0 && index < ITEMS.size()) {
            CardItem item = ITEMS.get(index);
            item.details = "";
        }
        index = x;
        if (index > 0 && index < ITEMS.size()) {
            CardItem item = ITEMS.get(index);
            item.details = "    当前";
        }
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
