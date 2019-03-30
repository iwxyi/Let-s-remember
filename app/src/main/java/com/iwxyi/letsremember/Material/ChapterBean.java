package com.iwxyi.letsremember.Material;


import com.iwxyi.letsremember.Globals.Paths;
import com.iwxyi.letsremember.Utils.StringUtil;

/*
 * File Description
 *
 * @author MRXY001
 * @date   2019/3/30 18 39
 * @email  wxy19980615@gmail.com
 */
public class ChapterBean {
    RememberBean positive_remember;
    RememberBean reverse_remember;
    boolean model = true;

    ChapterBean(String str) {

    }

    public void readStr(String str) {
        String positive_str = StringUtil.getXml(str, "positive");
        positive_remember = new RememberBean(positive_str);

        String reverse_str = StringUtil.getXml(str, "positive");
        reverse_remember = new RememberBean(reverse_str);
    }

    public void switchPositive(boolean posi) {
        if (posi) {
            model = true;
        } else {
            model = false;
        }
    }

}
