package com.iwxyi.letsremember.User;

import com.iwxyi.letsremember.Utils.SettingsUtil;

public class User {
    public static int    state;      // 0 未登录；1 用户；2管理员
    public static int    user_id;    // 用户ID
    public static String username;   // 账号
    public static String password;   // 密码
    public static String nickname;   // 昵称
    public static int    credit;     // 信用度
    public static String mobile;     // 手机号
    public static String email;      // 邮箱
    public static String company;    // 公司
    public static String post;       // 职位
    public static int    permission; // 管理员权限

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
