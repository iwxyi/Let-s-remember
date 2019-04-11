package com.iwxyi.letsremember.Material;

import com.iwxyi.letsremember.Globals.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardsContent {

    public static final List<CardItem> ITEMS = new ArrayList<CardItem>();
    public static final Map<String, CardItem> ITEM_MAP = new HashMap<String, CardItem>();

    private static final int COUNT = 25;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
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

    }

    private static void addItem(CardItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static CardItem createDummyItem(int position) {
        return new CardItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static class CardItem {
        public final String id;
        public final String content;
        public final String details;

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
