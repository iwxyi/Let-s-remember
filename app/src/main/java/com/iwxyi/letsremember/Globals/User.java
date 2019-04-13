package com.iwxyi.letsremember.Globals;

import com.iwxyi.letsremember.Utils.StringUtil;

public class User {
    public static int    user_id;    // 用户ID
    public static String username;   // 账号
    public static String password;   // 密码
    public static String nickname;   // 昵称
    public static String mobile;     // 手机号
    public static String email;      // 邮箱
    public static int integral;      // 积分
    public static int reciteShort;   // 背诵单词类
    public static int reciteMiddle;  // 背诵诗文类
    public static int reciteLong;    // 背诵长篇类
    public static int typeinCount;   // 录入

    static {
        integral = App.getInt("integral");
        reciteShort = App.getInt("reciteShort");
        reciteMiddle = App.getInt("reciteMiddle");
        reciteLong = App.getInt("reciteLong");
        typeinCount = App.getInt("typeinCount");
    }

    public static boolean isLogin() {
        return user_id != 0;
    }

    public static String id() {
        return ""+user_id;
    }

    /**
     * 新背诵了一篇
     */
    public static void addRecite(String str) {
        int eng = 0, chi = 0;
        for (int i = 0; i < str .length()-1; i++) {
            String c = str.substring(i, i+1);
            if (StringUtil.canMatch(c, "\\d"))
                eng++;
            if (StringUtil.canMatch(c, "[^\\u00-\\uff]"))
                chi++;
        }
        if (eng == 0 && str.length() > 40 && chi > str.length() * 0.8) {
            reciteMiddle++;
            App.setVal("reciteMiddle", reciteMiddle);
        } else if (str.length() > 200) {
            reciteLong++;
            App.setVal("reciteLong", reciteLong);
        } else {
            reciteShort++;
            App.setVal("reciteShort", reciteShort);
        }
    }

    public static void addTypeinCount() {
        typeinCount++;
    }
}
