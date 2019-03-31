package com.iwxyi.letsremember.Material;


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
    boolean positive = true;

    ChapterBean(String str) {
        readStr(str);
    }

    public void readStr(String str) {
        String positive_str = StringUtil.getXml(str, "positive");
        positive_remember = new RememberBean(positive_str);

        String reverse_str = StringUtil.getXml(str, "reverse");
        reverse_remember = new RememberBean(reverse_str);
    }

    public void switchPositive() {
        switchPositive(!positive);
    }

    public void switchPositive(boolean posi) {
        positive = posi;
    }

    public RememberBean getRemeber() {
        if (positive) {
            return positive_remember;
        } else {
            return reverse_remember;
        }
    }

    public String toString() {
        String all = StringUtil.toXml(positive_remember.toString(), "positive");
        all += StringUtil.toXml(reverse_remember.toString(), "reverse");
        return StringUtil.toXml(all, "chapter");
    }
}
