package com.iwxyi.letsremember.Rank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldRankContent {

    public static final List<WorldRankItem> ITEMS = new ArrayList<WorldRankItem>();
    public static final Map<String, WorldRankItem> ITEM_MAP = new HashMap<String, WorldRankItem>();

    private static final int COUNT = 25;

    static String[] rank_name = {
            "","","","","",
            "","","","","",
            "","","","","",
            "","","","","",
            "","","","",""
    };
    static int[] recite_count = {
            7823,6323,6209,6188,6043,
            5873,5623,5321,4903,4003,
            3765,3715,3612,3121,2941,
            2927,2923,2812,2604,2588,
            2445,2312,2298,2275,2265,
    };
    static int[] typein_count = {
            519,310,410,79,1,
            0,178,0,43,8,
            0,0,0,238,98,
            19,84,0,0,3,
            32,0,572,0,8
    };

    static {
        for (int i = 1; i < COUNT; i++) {
            addItem(createItem(i, rank_name[i], recite_count[i],typein_count[i]));
//            addItem(createItem(i, rank_name[i], 0,0));
//            addItem(createDummyItem(i));
        }
    }

    private static void addItem(WorldRankItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static WorldRankItem createItem(int rank, String name, int recite, int typein) {
        return new WorldRankItem(""+rank, name, "背诵："+recite+"  录入："+typein);
    }

    private static WorldRankItem createDummyItem(int position) {
        return new WorldRankItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        builder.append("\nMore details information here.");
        return builder.toString();
    }

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
