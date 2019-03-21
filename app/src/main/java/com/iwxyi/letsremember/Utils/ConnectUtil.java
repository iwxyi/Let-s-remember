package com.iwxyi.letsremember.Utils;

/**
 * @author: mrxy001
 * @time: 2019.2.20
 * @Change: 2019.3.17
 * 宇宙超级无敌联网类
 * 一行搞定取网页源码问题
 * 注意需要联网权限(否则本类最后一个方法会报错，但是能通过编译)
 * <uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * Android P 需要 android:usesCleartextTraffic="true"
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ConnectUtil implements Runnable {

    /******************************** Runnable+Lambda(JDK1.8) ************************************/

    static String temp_result = "";

    static public void Get(String path, String param[], final Runnable runnable) {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                temp_result = (String) msg.obj;
                runnable.run();
            }
        };
        Get(path, param, 0, handler);
    }

    static public void Get(String path, String param, final Runnable runnable) {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                temp_result = (String) msg.obj;
                runnable.run();
            }
        };
        Get(path, param, 0, handler);
    }

    static public void Get(String path, final Runnable runnable) {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                temp_result = (String) msg.obj;
                runnable.run();
            }
        };
        Get(path, "", 0, handler);
    }

    /*******************post*****************/

    static public void Post(String path, String param[], final Runnable runnable) {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                temp_result = (String) msg.obj;
                runnable.run();
            }
        };
        Post(path, param, 0, handler);
    }

    static public void Post(String path, String param, final Runnable runnable) {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                temp_result = (String) msg.obj;
                runnable.run();
            }
        };
        Post(path, param, 0, handler);
    }

    static public void Post(String path, final Runnable runnable) {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                temp_result = (String) msg.obj;
                runnable.run();
            }
        };
        Post(path, "", 0, handler);
    }


    static public String getResult() {
        return temp_result;
    }


    /******************************** Callback ************************************/

    /**
     * 一行工具联网并直接运行的工具类
     * @param path              网络路径
     * @param param             参数
     * @param stringCallback   回调函数
     */
    static public void Get(String path, String param[], final StringCallback stringCallback) {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String str = (String) msg.obj;
                stringCallback.onFinish(str);
                if (str.isEmpty()) {
                    stringCallback.onFail(str);
                } else {
                    stringCallback.onSuccess(str);
                }
            }
        };
        Get(path, param, 0, handler);
    }

    static public void Get(String path, String param, final StringCallback stringCallback) {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String str = (String) msg.obj;
                stringCallback.onFinish(str);
                if (str.isEmpty()) {
                    stringCallback.onFail(str);
                } else {
                    stringCallback.onSuccess(str);
                }
            }
        };
        Get(path, param, 0, handler);
    }

    static public void Get(String path, final StringCallback stringCallback) {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String str = (String) msg.obj;
                stringCallback.onFinish(str);
                if (str.isEmpty()) {
                    stringCallback.onFail(str);
                } else {
                    stringCallback.onSuccess(str);
                }
            }
        };
        Get(path, "", 0, handler);
    }

     /********************post********************/

     static public void Post(String path, String param[], final StringCallback stringCallback) {
         @SuppressLint("HandlerLeak")
         Handler handler = new Handler(){
             @Override
             public void handleMessage(Message msg) {
                 String str = (String) msg.obj;
                 stringCallback.onFinish(str);
                 if (str.isEmpty()) {
                     stringCallback.onFail(str);
                 } else {
                     stringCallback.onSuccess(str);
                 }
             }
         };
         Post(path, param, 0, handler);
     }

    static public void Post(String path, String param, final StringCallback stringCallback) {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String str = (String) msg.obj;
                stringCallback.onFinish(str);
                if (str.isEmpty()) {
                    stringCallback.onFail(str);
                } else {
                    stringCallback.onSuccess(str);
                }
            }
        };
        Post(path, param, 0, handler);
    }

    static public void Post(String path, final StringCallback stringCallback) {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String str = (String) msg.obj;
                stringCallback.onFinish(str);
                if (str.isEmpty()) {
                    stringCallback.onFail(str);
                } else {
                    stringCallback.onSuccess(str);
                }
            }
        };
        Post(path, "", 0, handler);
    }


    /******************************** Handler ************************************/

    /**
     * 静态类工具，一行代码才可联网工具
     * @param path    网址
     * @param param   参数
     * @param what    返回的 what，由使用的对象来决定
     * @param handler 要返回的 Handler，进行处理返回的代码
     */
    static public void Get(String path, String param, int what, Handler handler) {
        Thread thread = new Thread(new ConnectUtil(path, param, what, handler));
        thread.start();
    }

    static public void Get(String path, String[] param, int what, Handler handler) {
        Thread thread = new Thread(new ConnectUtil(path, param, what, handler));
        thread.start();
    }

    static public void Get(String path, String param, Handler handler) {
        Thread thread = new Thread(new ConnectUtil(path, param, 0, handler));
        thread.start();
    }

    static public void Get(String path, String param[], Handler handler) {
        Thread thread = new Thread(new ConnectUtil(path, param, 0, handler));
        thread.start();
    }

    static public void Get(String path, Handler handler) {
        Thread thread = new Thread(new ConnectUtil(path, "", 0, handler));
        thread.start();
    }

    /*******************post********************/
    static public void Post(String path, String param, int what, Handler handler) {
        Thread thread = new Thread(new ConnectUtil(path, param, what, handler).post());
        thread.start();
    }

    static public void Post(String path, String[] param, int what, Handler handler) {
        Thread thread = new Thread(new ConnectUtil(path, param, what, handler).post());
        thread.start();
    }

    static public void Post(String path, String param, Handler handler) {
        Thread thread = new Thread(new ConnectUtil(path, param, 0, handler).post());
        thread.start();
    }

    static public void Post(String path, String param[], Handler handler) {
        Thread thread = new Thread(new ConnectUtil(path, param, 0, handler).post());
        thread.start();
    }

    static public void Post(String path, Handler handler) {
        Thread thread = new Thread(new ConnectUtil(path, "", 0, handler).post());
        thread.start();
    }


    /******************************** inner Class ************************************/

    String path, param;
    String method = "GET";
    int what;
    Handler handler;

    /**
     * 连接的构造函数
     * @param path      网址
     * @param param     参数（文本）
     * @param what      Message.what
     * @param handler   Handler
     */
    public ConnectUtil(String path, String param, int what, Handler handler) {
        this.handler = handler;
        this.what = what;
        this.path = path;
        this.param = param;
    }

    public ConnectUtil(String path, String param, Handler handler) {
        this(path, param, 0, handler);
    }

    public ConnectUtil(String path, Handler handler) {
        this(path, "", 0, handler);
    }

    public ConnectUtil(String path, String[] params, int what, Handler handler) {

        this(path, "", what, handler);

        StringBuilder url = new StringBuilder();
        int count = params.length;
        for (int i = 0; i < count; i++) {
            String str = params[i];
            try {
                str = URLEncoder.encode(str, "UTF-8"); // 进行网络编码
            } catch (UnsupportedEncodingException e) {
                str = URLEncoder.encode(str);
            }
            if (i % 2 == 0) {
                if (i > 0) {
                    url.append("&");
                }
                url.append(str).append("=");
            }
            else {
                url.append(str);
            }
        }
        param = url.toString();
    }

    public ConnectUtil(Handler handler, String path, String[] params) {
        this(path, params, 0, handler);
    }

    public ConnectUtil post() {
        this.method = "POST";
        return this;
    }

    @Override
    public void run() {
        String result;
        if ("POST".equals(method))
            result = NetworkUtil.post(path, param);
        else
            result = NetworkUtil.get(path, param);
        Message msg = new Message();
        msg.obj = result;
        msg.what = what;
        handler.sendMessage(msg);
        Log.i("====connect", path + "?" + param + ">>>>" + result);
    }

    /**
     * 判断有没有连接网络
     * @param activity
     * @return
     */
    public static boolean isConnect(Activity activity) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) ((Activity) activity)
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            Log.v("====isConnect", e.toString());
        }
        return false;
    }
}