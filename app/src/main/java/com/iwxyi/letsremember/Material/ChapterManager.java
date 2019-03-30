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
        String file_path = Paths.getLocalPath("material/"+pack+"/"+file+".txt");
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

    public String getContent() {
        return chapter.getRemeber().getContent();
    }

    public String getDescription() {
        return chapter.getRemeber().getDescription();
    }

    public ArrayList<RememberBean.PlaceBean> getPlaces() {
        return chapter.getRemeber().getPlaces();
    }

    public void switchPositive() {
        chapter.switchPositive();
    }

    public void switchPositive(boolean posi) {
        chapter.switchPositive(posi);
    }

    public void increaseHide(int start, int end) {
        chapter.getRemeber().increaseHide(start, end);
        chapter_list.set(index, chapter.toString());

        StringBuilder all = new StringBuilder();
        for (String c :
                chapter_list) {
            all.append(c);
        }

        FileUtil.writeTextVals("material/index/index.txt", all.toString());
    }
}
