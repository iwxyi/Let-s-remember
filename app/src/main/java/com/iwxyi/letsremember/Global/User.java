package com.iwxyi.letsremember.Global;

public class User {
    public static int    state;      // 0 未登录；1 用户；2管理员
    public static int    user_id;    // 用户ID
    public static String username;   // 账号
    public static String password;   // 密码
    public static String nickname;   // 昵称
    public static String mobile;     // 手机号
    public static String email;      // 邮箱

    public static boolean isLogin() {
        return state != 0;
    }

    public static boolean isUser() {
        return state == 1;
    }

    public static boolean isAdmin() {
        return state == 2;
    }

    public static String id() {
        return ""+user_id;
    }

}
