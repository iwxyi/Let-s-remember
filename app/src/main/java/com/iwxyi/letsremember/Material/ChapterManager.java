package com.iwxyi.letsremember.Material;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.Globals.Paths;
import com.iwxyi.letsremember.Utils.FileUtil;
import com.iwxyi.letsremember.Utils.StringUtil;

import java.net.FileNameMap;
import java.util.ArrayList;

/*
 * File Description
 *
 * @author MRXY001
 * @date   2019/3/30 18 43
 * @email  wxy19980615@gmail.com
 */
public class ChapterManager {
    private String pack_name = "";
    private String file_name = "";
    private String full_text;
    public ArrayList<String> chapter_list;
    private int index;
    private ChapterBean chapter;

    public ChapterManager(String pack, String file) {
        initList(pack, file);
    }

    public void initList(String pack, String file) {
        pack_name = pack;
        file_name = file;
        String file_path = Paths.getLocalPath(pack+"/"+file+".txt");
        full_text = FileUtil.readTextVals(file_path);
        chapter_list = StringUtil.getXmls(full_text, "chapter");
        index = App.getInt("chapter:"+pack_name+"/"+file_name);
        if (chapter_list.size() == 0) {
            App.toast("找不到内容："+pack_name+"/"+ file_name);
            return ;
        }
        if (index >= chapter_list.size()) {
            index = 0;
        }
        chapter = new ChapterBean(chapter_list.get(index));
    }
}
