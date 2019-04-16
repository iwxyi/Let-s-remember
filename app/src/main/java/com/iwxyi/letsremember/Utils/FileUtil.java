package com.iwxyi.letsremember.Utils;

/**
 * FileUtil:文件操作工具类
 * @Author:命燃芯乂
 * @Time:2019
 *
 * @Update:2019.4.16
 *  - 增加 readAssert() 方法
 *
 * @Update:2019.4.13
 *  - 增加 delete 方法
 */

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.iwxyi.letsremember.Globals.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtil {

    private static final String FOLEDER_NAME = "letsremember";
    private static String LOCAL_FOLDER = "";

    /**
     * 寻找程序数据存储的外部文件夹
     * @return 文件夹路径（带"/"）
     */
    public static String getFolder() {
        if (!LOCAL_FOLDER.isEmpty()) {
            return LOCAL_FOLDER;
        }
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return LOCAL_FOLDER = Environment.getExternalStorageDirectory().getPath() + "/" + FOLEDER_NAME;
        }
        else {
            return LOCAL_FOLDER = "/data/data/com.letsremember/files/"+FOLEDER_NAME;
        }
    }

    /**
     * 写入设置到对应的文件
     * @param fileName 文件名
     * @param text 文件内容
     * @return 写入文件是否成功
     */
    public static boolean writeTextVals(String fileName, String text) {
        if (!fileName.startsWith(getFolder()) && !fileName.startsWith("/"))
            fileName = getFolder()+"/"+fileName;
        return writeTextFile(fileName, text);
    }

    /**
     * 读取对应文件的设置
     * @param fileName 文件名
     * @return 读取到的文本内容
     */
    public static String readTextVals(String fileName) {
        if (!fileName.startsWith(getFolder()) && !fileName.startsWith("/"))
            fileName = getFolder()+"/"+fileName;
        try {
            return readTextFile(fileName);
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
        if (!filename.startsWith("/") && !filename.startsWith(getFolder()))
            filename = getFolder() + "/" + filename;
        File file = new File(filename);
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
        if (!folder_name.startsWith(getFolder()) && !folder_name.startsWith("/"))
            folder_name = getFolder()+"/"+folder_name;
        return createFolder(folder_name);
    }

    public static void delete(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return ;
        }
        if (file.isDirectory()) {
            deleteDirectory(path);
        } else {
            deleteFile(path);
        }
    }

    private static void deleteDirectory(String path) {
        File file = new File(path);
        App.log("====delete dir", file.getPath());
        File[] files = file.listFiles();
        for (File f :
                files) {
            if (f.isDirectory()) {
                deleteDirectory(f.getPath());
            } else {
                deleteFile(f.getPath());
            }
        }
        file.delete();
    }

    private static void deleteFile(String path) {
        File file = new File(path);
        App.log("====delete file", file.getPath());
        file.delete();
    }

    /**
     * 确保文件存在
     * @param fileName
     * @return
     */
    public static boolean ensureFile(String fileName) {
        if (!fileName.startsWith(getFolder()) && !fileName.startsWith("/"))
            fileName = getFolder()+"/"+fileName;
        return createFile(fileName);
    }

    /**
     * 从 Assert 中读取文本文件，可能只有txt文件可以用
     * @param fileName
     * @return
     */
    public static String readAssert(String fileName) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(App.getContext().getResources().getAssets().open(fileName));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "", result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line+"\n";
            }
            inputStreamReader.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
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
