package com.iwxyi.letsremember.Material;

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

    static {
        String last_package = App.getVal("last_package");
        // 遍历文件
        File file = new File(Paths.getLocalPath("material"));
        File[] packages = file.listFiles();
        for (int i = 0; i < packages .length; i++) {
            String package_name = packages[i].getName(); // 记忆包的文件夹名称
            String detail = "("+getSectinoCount(package_name)+")";
            if (package_name.equals(last_package))
                detail += "    上次记忆";
            addItem(createPackageItem(i+1, package_name, detail));
        }
    }

    private static void addItem(PackageItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static PackageItem createPackageItem(int position, String package_name, String detail) {
        return new PackageItem(String.valueOf(position), package_name, detail);
    }

    /**
     * 获取一个记忆包中章节的数量
     * @param package_name 记忆包名称
     */
    public static int getSectinoCount(String package_name) {
        String path = Paths.getLocalPath("material/"+package_name);
        File file = new File(path);
        File[] sections = file.listFiles();
        return sections.length;
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
