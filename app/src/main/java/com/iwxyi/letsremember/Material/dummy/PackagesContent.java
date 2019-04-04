package com.iwxyi.letsremember.Material.dummy;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.Globals.Paths;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackagesContent {

    public static final List<PackageItem> ITEMS = new ArrayList<PackageItem>();
    public static final Map<String, PackageItem> ITEM_MAP = new HashMap<String, PackageItem>();

    private static final int COUNT = 25;

    static {
        String last_package = App.getVal("last_package");
        // 遍历文件
        File file = new File(Paths.getLocalPath(""));
        File[] packages = file.listFiles();
        for (int i = 0; i < packages .length; i++) {
            String package_name = packages[i].getName(); // 记忆包的文件夹名称
            String detail = "";
            if (package_name.equals(last_package))
                detail = "(上次记忆)";
            addItem(createPackageItem(i, package_name, detail));
        }
    }

    private static void addItem(PackageItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static PackageItem createPackageItem(int position, String package_name, String detail) {
        return new PackageItem(String.valueOf(position), package_name, detail);
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
