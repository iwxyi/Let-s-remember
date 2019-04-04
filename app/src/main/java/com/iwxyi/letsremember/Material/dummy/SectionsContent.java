package com.iwxyi.letsremember.Material.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SectionsContent {

    public static final List<SectionItem> ITEMS = new ArrayList<SectionItem>();
    public static final Map<String, SectionItem> ITEM_MAP = new HashMap<String, SectionItem>();
    private static final int COUNT = 25;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(createSectionItem(i));
        }
    }

    private static void addItem(SectionItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static SectionItem createSectionItem(int position) {
        return new SectionItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static class SectionItem {
        public final String id;
        public final String content;
        public final String details;

        public SectionItem(String id, String content, String details) {
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
