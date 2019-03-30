package com.iwxyi.letsremember.Utils;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtil {

    private static final String FOLEDER_NAME = "letsremember";

    /**
     * 寻找程序数据存储的外部文件夹
     * @return 文件夹路径（带"/"）
     */
    public static String getFolder() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getPath() + "/" + FOLEDER_NAME;
        }
        else {
            return "/data/data/com.letsremember/files/"+FOLEDER_NAME;
        }
    }

    /**
     * 写入设置到对应的文件
     * @param fileName 文件名
     * @param text 文件内容
     * @return 写入文件是否成功
     */
    public static boolean writeTextVals(String fileName, String text) {
        return writeTextFile(getFolder() + "/" +fileName, text);
    }

    /**
     * 读取对应文件的设置
     * @param fileName 文件名
     * @return 读取到的文本内容
     */
    public static String readTextVals(String fileName) {
        try {
            return readTextFile(getFolder() + "/" +fileName);
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * 文件是否存在。用以做一些初始化工作
     * @param filename 文件名
     * @return
     */
    public static boolean exist(String filename) {
        File file = new File(getFolder() + "/" +filename);
        return file.exists();
    }

    /**
     * 确保文件夹存在（目标是数据存储的主文件夹）
     * @return 确认文件是否存在
     */
    public static boolean ensureFolder() {
        return createFolder(getFolder());
    }

    /**
     * 确保某个目录一定存在
     * @param folder_name
     * @return
     */
    public static boolean ensureFolder(String folder_name) {
        if (!folder_name.startsWith(getFolder())) {
            folder_name = getFolder() + folder_name;
        }
        return createFolder(folder_name);
    }

    private static boolean writeTextFile(String filePath, String text) {
        createFile(filePath);

        try{
            // 创建 File类 指定数据存储的位置
            File file = new File(filePath);
            // 创建一个文件输出流
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            fos.close();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String readTextFile(String filePath) throws IOException {
        createFile(filePath);

        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        BufferedReader bufr = new BufferedReader(new InputStreamReader(fis));
        StringBuilder sb = new StringBuilder();
        String line;
        while ( (line = bufr.readLine()) != null ) {
            sb.append(line+"\n");
        }
        return sb.toString();
    }

    private static boolean createFolder(String path) {
        File file;
        try {
            file = new File(path);
            Log.i("====file", path);
            return file.exists() || file.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean createFile(String filePath) {
        File file;
        try {
            file = new File(filePath);
            return file.exists() || file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
