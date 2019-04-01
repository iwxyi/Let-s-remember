package com.iwxyi.letsremember.Globals;

public class User {
    public static int    user_id;    // 用户ID
    public static String username;   // 账号
    public static String password;   // 密码
    public static String nickname;   // 昵称
    public static String mobile;     // 手机号
    public static String email;      // 邮箱
    public static int integral;      // 积分
    public static int recite;        // 背诵数量

    public static boolean isLogin() {
        return user_id != 0;
    }

    public static String id() {
        return ""+user_id;
    }

    /**
     * 新背诵了一篇
     */
    public static void addRecite() {
        recite++;
        App.setVal("recite", recite);
    }
}
