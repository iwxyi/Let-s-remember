package com.iwxyi.letsremember.Rank.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample username for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class WorldRankContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<WorldRankItem> ITEMS = new ArrayList<WorldRankItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, WorldRankItem> ITEM_MAP = new HashMap<String, WorldRankItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(WorldRankItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static WorldRankItem createDummyItem(int position) {
        return new WorldRankItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of username.
     */
    public static class WorldRankItem {
        public String id = "";
        public int rank = 0;
        public int user_id = 0;
        public String username;
        public String details;

        public WorldRankItem(String id, String content, String details) {
            this.id = id;
            this.username = content;
            this.details = details;
        }

        public WorldRankItem(int rank, int user_id, String username, String detail) {
            this.rank = rank;
            this.user_id = user_id;
            this.username = username;
            this.details = detail;
        }

        @Override
        public String toString() {
            return username;
        }
    }
}
