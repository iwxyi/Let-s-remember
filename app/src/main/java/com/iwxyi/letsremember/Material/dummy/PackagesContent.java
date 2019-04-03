package com.iwxyi.letsremember.Material.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PackagesContent {

    public static final List<PackageItem> ITEMS = new ArrayList<PackageItem>();
    public static final Map<String, PackageItem> ITEM_MAP = new HashMap<String, PackageItem>();

    private static final int COUNT = 25;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(PackageItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static PackageItem createDummyItem(int position) {
        return new PackageItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static class PackageItem {
        public final String id;
        public final String content;
        public final String details;

        public PackageItem(String id, String content, String details) {
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
