package com.iwxyi.letsremember.Material;

import android.util.Log;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.Globals.Paths;
import com.iwxyi.letsremember.Utils.FileUtil;
import com.iwxyi.letsremember.Utils.StringUtil;

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
        return chapter.getContent();
    }

    public String getDescription() {
        return chapter.getDescription();
    }

    public ArrayList<ChapterBean.PlaceBean> getPlaces() {
        return chapter.getPlaces();
    }

    public void increaseHide(int start, int end) {
        chapter.increaseHide(start, end);
        chapter_list.set(index, chapter.toString());

        StringBuilder all = new StringBuilder();
        for (String c :
                chapter_list) {
            all.append(c);
        }

        FileUtil.writeTextVals("material/index/index.txt", all.toString());
    }

    public void decreaseHide(int start, int end) {
        chapter.decreaseHide(start, end);
        chapter_list.set(index, chapter.toString());

        StringBuilder all = new StringBuilder();
        for (String c :
                chapter_list) {
            all.append(c);
        }

        FileUtil.writeTextVals("material/index/index.txt", all.toString());
    }

    public void addRememberTimestamp() {
        int timestamp = App.getTimestamp();

    }

    /**
     * 移动章节，相对于当前位置的差异
     * @param x
     */
    public void moveChapter(int x) {
        if (chapter_list.size() == 0 || x == 0) {
            return ;
        }
        int index = this.index + x;
        if (index < 0) {
            index = 0;
        } else if (index >= chapter_list.size()) {
            index = chapter_list.size()-1;
        }
        jumpChapter(index);
    }

    /**
     * 切换章节
     * @param x 和当前的距离
     */
    public void jumpChapter(int x) {
        index = x;
        if (index < 0) {
            index = 0;
        } else if (index >= chapter_list.size()) {
            index = chapter_list.size()-1;
        }
        Log.i("====章节位置", ""+index+"/"+chapter_list.size());

        chapter = new ChapterBean(chapter_list.get(index));
        App.setVal("chapter:"+pack_name+"/"+file_name, index);
    }
}
