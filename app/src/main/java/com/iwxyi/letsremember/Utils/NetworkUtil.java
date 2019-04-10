package com.iwxyi.letsremember.Utils;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtil {
    /**
     * 获取网页源码 GET 方式
     * @param path 地址
     * @return 网页
     */
    public static String get(String path) {
        return getWebPagSource(path, "GET", "");
    }

    public static String get(String path, String param) {
        if (param != "")
            path += "?" + param;
        return getWebPagSource(path, "GET", "");
    }

    public static String post(String path, String param) {
        return getWebPagSource(path, "POST", param);
    }

    public static String getWebPagSource(String path, String method, String param) {
        Log.i("====getWebSource", "msg");
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.setConnectTimeout(5000);

            // 如果是 post 的话，还需要额外设置其他的
            if ("POST".equals(method)) {
                // TODO POST 不知道要怎么改啊！
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches(false);
                PrintWriter printWriter = new PrintWriter(urlConnection.getOutputStream());
                // 发送请求参数
                printWriter.write(param); //post的参数 xx=xx&yy=yy
                // flush输出流的缓冲
                printWriter.flush();
            }
            Log.i("====param", param);
            int code = urlConnection.getResponseCode();
            if (code == 200) {
                InputStream in = urlConnection.getInputStream();
                String content = StreamUtil.readStream(in);
                Log.i("====username", content);
                return content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
