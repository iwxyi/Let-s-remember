package com.iwxyi.letsremember.Material.dummy;

import android.widget.Toast;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.Globals.Paths;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SectionsContent {

    public static final List<SectionItem> ITEMS = new ArrayList<SectionItem>();
    public static final Map<String, SectionItem> ITEM_MAP = new HashMap<String, SectionItem>();
    private static final int COUNT = 25;

    static {
        String package_name = App.getVal("selected_package");
        refreshSections(package_name);
        /*for (int i = 1; i <= COUNT; i++) {
            addItem(createSectionItem(i));
        }*/
    }

    private static void addItem(SectionItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static SectionItem createSectionItem(int position, String section_name, String detail) {
        return new SectionItem(String.valueOf(position), section_name, detail);
    }

    public static void refreshSections(String package_name) {
        if (package_name.equals("")) {
            return ;
        }

        ITEMS.clear();
        ITEM_MAP.clear();
        String package_path = Paths.getLocalPath("material/"+package_name+"/");
        File package_file = new File(package_path);
        if (!package_file.exists()) {
            App.toast("记忆包"+package_name+"不存在！");
            return ;
        }
        File[] sections = package_file.listFiles();
        for (int i = 0; i < sections .length; i++) {
            String section = sections[i].getName();
            if (section.endsWith(".txt")) {
                section = section.substring(0, section.length()-4);
            }
            addItem(createSectionItem(i, section, ""));
        }
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
