package com.iwxyi.letsremember.Boxs;

import android.util.Log;

import com.iwxyi.letsremember.Globals.Def;
import com.iwxyi.letsremember.Globals.Paths;
import com.iwxyi.letsremember.Utils.FileUtil;
import com.iwxyi.letsremember.Utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IceContent {

    public static final List<IceItem> ITEMS = new ArrayList<IceItem>();

    public static final Map<String, IceItem> ITEM_MAP = new HashMap<String, IceItem>();

    private static final int COUNT = 25;

    static {
        refreshContent();
    }

    public static void refreshContent() {
        ITEM_MAP.clear();
        ITEM_MAP.clear();

        int all_index = 1;
        // 遍历文件夹
        File pack_dir = new File(Paths.getLocalPath("material"));
        File[] packages = pack_dir.listFiles();
        for (int i = 0; i < packages.length; i++) {
            // 获取包名和包路径
            String pack_name = packages[i].getName();
            String pack_path = packages[i].getPath();
            Log.i("===pack", pack_path);
            // 遍历文件
            File sect_dir = new File(pack_path);
            File[] sections = sect_dir.listFiles();
            for (int j = 0; j < sections.length; j++) {
                // 获取章节名字和路径
                String sect_name = sections[j].getName();
                String sect_path = sections[j].getPath();
                // 遍历章节内所有的文件
                String file_content = FileUtil.readTextVals(sect_path);
                String[] card_list = StringUtil.getXmls(file_content, "card").toArray(new String[0]);
                for (int k = 0; k < card_list.length; k++) {
                    String card = card_list[k];
                    // 判断记忆卡片的盒子类型，如果是当前的盒子则开始判断
                    int box = StringUtil.getXmlInt(card, "box");
                    if (box == Def.ICE_BOX) {
                        String content = StringUtil.getXml(card, "content").trim();
                        addItem(new IceItem(all_index++, pack_name, sect_name, k, content, 0));
                    }
                }
            }
        }
    }

    private static void addItem(IceItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(""+item.id, item);
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
        public String card;
        public int time;

        public final String content;
        public final String details;

        public IceItem(String id, String content, String details) {
            this.id = Integer.parseInt(id);
            this.content = content;
            this.details = details;
        }

        public IceItem(int id, String p, String s, int i, String c, int t) {
            this.id = id;
            this.pack = p;
            this.sect = s;
            this.index = i;
            this.card = c;
            this.time = t;

            this.content = pack + " / " + sect + " / " + i;
            this.details = card;

        }

        @Override
        public String toString() {
            return content;
        }
    }
}
