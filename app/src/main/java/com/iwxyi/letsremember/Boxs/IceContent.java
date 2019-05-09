package com.iwxyi.letsremember.Boxs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IceContent {

    public static final List<IceItem> ITEMS = new ArrayList<IceItem>();

    public static final Map<String, IceItem> ITEM_MAP = new HashMap<String, IceItem>();

    private static final int COUNT = 25;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(IceItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(""+item.id, item);
    }

    private static IceItem createDummyItem(int position) {
        return new IceItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static class IceItem {

        public int id;
        public String pack;
        public String sect;
        public int index;
        public int time;

        public final String content;
        public final String details;

        public IceItem(String id, String content, String details) {
            this.id = Integer.parseInt(id);
            this.content = content;
            this.details = details;
        }

        public IceItem(int id, String p, String s, int i, int t) {
            this.id = id;
            this.pack = p;
            this.sect = s;
            this.index = i;
            this.time = t;

            this.content = ""+id;
            this.details = p+s;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
