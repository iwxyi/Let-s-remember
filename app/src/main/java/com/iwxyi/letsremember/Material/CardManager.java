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
    private String pack_name = "";
    private String file_name = "";
    private String full_text;
    public ArrayList<String> card_list;
    private int index;
    private CardBean card;

    public CardManager(String pack, String file) {
        initList(pack, file);
    }

    public void initList(String pack, String file) {
        pack_name = pack;
        file_name = file;
        String file_path = Paths.getLocalPath("material/"+pack+"/"+file+".txt");
        full_text = FileUtil.readTextVals(file_path);
        card_list = StringUtil.getXmls(full_text, "card");
        index = App.getInt("card:"+pack_name+"/"+file_name);
        if (card_list.size() == 0) {
            App.toast("找不到内容："+pack_name+"/"+ file_name);
            return ;
        }
        if (index >= card_list.size()) {
            index = 0;
        }
        card = new CardBean(card_list.get(index));
    }

    public String getContent() {
        return card.getContent();
    }

    public String getDescription() {
        return card.getDescription();
    }

    public ArrayList<CardBean.PlaceBean> getPlaces() {
        return card.getPlaces();
    }

    private void saveModify() {
        card_list.set(index, card.toString());

        StringBuilder all = new StringBuilder();
        for (String c :
                card_list) {
            all.append("<card>").append(c).append("</card>");
        }

        FileUtil.writeTextVals("material/index/index.txt", all.toString());
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
        int timestamp = App.getTimestamp();
        card.addRememberTimestamp(timestamp);
        saveModify();
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
        if (index < 0) {
            index = 0;
        } else if (index >= card_list.size()) {
            index = card_list.size()-1;
        }
        Log.i("====章节位置", ""+index+"/"+card_list.size());

        card = new CardBean(card_list.get(index));
        App.setVal("card:"+pack_name+"/"+file_name, index);
    }
}
