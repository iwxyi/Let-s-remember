package com.iwxyi.letsremember.Rank;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.Globals.User;
import com.iwxyi.letsremember.Utils.StringUtil;
import com.iwxyi.letsremember.Utils.XmlParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldRankContent {

    public static final List<WorldRankItem> ITEMS = new ArrayList<WorldRankItem>();
    public static final Map<String, WorldRankItem> ITEM_MAP = new HashMap<String, WorldRankItem>();

    private static final int COUNT = 25;

    static String[] rank_name = {
            "校长！借个火","甜心少女","倦容","我一直在等你","遇见",
            "世人皆醉我獨醒","小孤独","繁花入梦","多想留在你身边","哆啦只是个梦",
            "离挽i","别凶我","妄想徒手摘星","想把你藏起来","青鸾",
            "听闻爱情十有九悲","柠檬少女","情花罂粟","向暖","佐堓蓅哖",
            "孤影人","承蒙厚爱","清欢渡","清雾扰山河","傲性小仙女^ω^"
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
        /*for (int i = 0; i < COUNT; i++) {
            addItem(createItem(i+1, rank_name[i], recite_count[i],typein_count[i]));
        }
        addItem(createItem(182, "=="+User.getName()+"==", User.reciteShort+User.reciteMiddle+User.reciteLong, User.typeinCount));*/
    }

    private static void addItem(WorldRankItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static WorldRankItem createItem(int rank, String name, int recite, int typein) {
        return new WorldRankItem(""+rank, name, "记:"+recite+"  录:"+typein);
    }

    public static void initFromStr(String str) {
        ITEMS.clear();
        ITEM_MAP.clear();
        XmlParser xmlParser = new XmlParser(str);
        xmlParser.split("USER");
        for (int i = 0; i < xmlParser.size(); i++) {
//            xmlParser.setIndex(i);
//            addItem(createItem(i+1, xmlParser.get("NICKNAME"),
//                    xmlParser.getInt("TYPEIN"), xmlParser.getInt("RECITE")));
            addItem(new WorldRankItem(i+1, xmlParser.getItem(i)));
        }
    }

    public static class WorldRankItem {
        public String id = "";
        public int rank = 0;
        public int user_id = 0;
        public String username;
        public String nickname;
        public String details;
        public int integral;
        public int recite;
        public int typein;

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

        public WorldRankItem(int rank, String str) {
            this.rank = rank;
            this.user_id = StringUtil.getXmlInt(str, "USER_ID");
            this.username = StringUtil.getXml(str, "USERNAME");
            this.nickname = StringUtil.getXml(str, "NICKNAME");
            this.integral = StringUtil.getXmlInt(str, "INTEGRAL");
            this.recite = StringUtil.getXmlInt(str, "RECITE");
            this.typein = StringUtil.getXmlInt(str, "TYPEIN");

            makeDetail();
        }

        private void makeDetail() {
            this.details = "记:"+recite+"  录:"+typein;
        }

        public WorldRankItem(String str) {
            this.user_id = StringUtil.getXmlInt(str, "USER_ID");
            this.username = StringUtil.getXml(str, "USERNAME");
            this.nickname = StringUtil.getXml(str, "NICKNAME");
            this.integral = StringUtil.getXmlInt(str, "INTEGRAL");
            this.recite = StringUtil.getXmlInt(str, "RECITE");
            this.typein = StringUtil.getXmlInt(str, "TYPEIN");
        }

        @Override
        public String toString() {
            return username;
        }
    }
}
