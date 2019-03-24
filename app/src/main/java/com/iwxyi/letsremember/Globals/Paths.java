package com.iwxyi.letsremember.Globals;

import android.os.Environment;

import java.io.File;

public class Paths {

    public static final String NET_PATH = "http://iwxyi.com/letsremember/public/index.php/index/index/"; // 网络路径
    public static final String DATA_PATH = "/letsremember/";
    public static final String PC_IP = "192.168.43.135"; // 本机电脑IP，调试用 // 192.168.103.2 不知道为啥这个IP也可以诶？
    public static final String NET_PATHD = "http://"+PC_IP+"/letsremember/public/index.php/index/index/"; // 调试
    public static String SDDir = "";

    public static String getNetPath(String action) {
        return NET_PATHD + action;
    }

    public static String getLocalPath(String fileName) {
        if (SDDir.isEmpty()) {
            SDDir = getSDPath();
        }
        return SDDir+DATA_PATH+fileName;
    }

    public static String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if(sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            return sdDir.getPath();
        }
        return "/sdcard";
    }
}
