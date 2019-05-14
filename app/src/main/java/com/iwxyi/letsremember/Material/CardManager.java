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
public class CardManager {
    public String pack_name = "";
    public String sect_name = "";
    private String full_text;
    public ArrayList<String> card_list;
    public int index;
    private CardBean card;

    public CardManager(String pack, String file) {
        initList(pack, file);
    }

    public void initList(String pack, String sect) {
        pack_name = pack;
        sect_name = sect;
        String file_path = Paths.getLocalPath("material/"+pack+"/"+sect+".txt");
        full_text = FileUtil.readTextVals(file_path);
        card_list = StringUtil.getXmls(full_text, "card");
        index = App.getInt("card:"+pack_name+"/"+ sect_name);
        if (card_list.size() == 0) {
            App.err("找不到内容："+pack_name+"/"+ sect_name);
            card = new CardBean("");
            return ;
        }
        if (index < 0 || index >= card_list.size()) {
            App.err("记忆卡片索引出错" + index);
            index = 0;
        }
        card = new CardBean(card_list.get(index));
    }

    public String getContent() {
        if (card == null) {
            return "";
        }
        return card.getContent();
    }

    public String getDescription() {
        if (card == null) {
            return "";
        }
        return card.getDescription();
    }

    public int getCount() {
        return card_list.size();
    }

    public ArrayList<CardBean.PlaceBean> getPlaces() {
        if (card == null) {
            return new ArrayList<CardBean.PlaceBean>();
        }
        return card.getPlaces();
    }

    private void saveModify() {
        card_list.set(index, card.toString());

        StringBuilder all = new StringBuilder();
        for (String c :
                card_list) {
            all.append("<card>").append(c).append("</card>");
        }

//        Log.i("====saveModify", "material/"+pack_name+"/"+sect_name+".txt\n" + all.toString());

        FileUtil.writeTextVals("material/"+pack_name+"/"+sect_name+".txt", all.toString());
    }

    public void increaseHide(int start, int end) {
        card.increaseHide(start, end);
        saveModify();
    }

    public void decreaseHide(int start, int end) {
        card.decreaseHide(start, end);
        saveModify();
    }

    /**
     * 记住这个词了，修改记住时间
     * 如果和上次背诵时间差距小于1分钟（避免来回切换），则保存
     */
    public void addRememberTimestamp() {
        if (card_list.size() == 0) {
            return ;
        }
        int timestamp = App.getTimestamp();
        card.addRememberTimestamp(timestamp);
        saveModify();
    }

    public void setChapterBox(int x) {
        if (card == null) {
            return ;
        }
        card.setBox(x);
        saveModify();
    }

    public int getChapterBox() {
        if (card == null) {
            return 0;
        }
        return card.box;
    }

    /**
     * 移动章节，相对于当前位置的差异
     * @param x
     */
    public void moveChapter(int x) {
        if (card_list.size() == 0 || x == 0) {
            return ;
        }
        int index = this.index + x;
        if (index < 0) {
            index = 0;
        } else if (index >= card_list.size()) {
            index = card_list.size()-1;
        }
        jumpChapter(index);
    }

    /**
     * 切换章节
     * @param x 和当前的距离
     */
    public void jumpChapter(int x) {
        index = x;
        if (index >= card_list.size()) {
            index = card_list.size()-1;
        }
        if (index < 0) {
            index = 0;
        }
        Log.i("====章节位置", ""+index+"/"+card_list.size());

        card = new CardBean(card_list.get(index));
        App.setVal("card:"+pack_name+"/"+ sect_name, index);
    }

    public boolean jumpChapterOnly(int x) {
        index = x;
        if (index >= card_list.size()) {
            return false;
        }
        if (index < 0) {
            return false;
        }
        Log.i("====章节位置", ""+index+"/"+card_list.size());

        card = new CardBean(card_list.get(index));
        return true;
    }
}
