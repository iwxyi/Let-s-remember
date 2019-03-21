package com.iwxyi.letsremember.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.R;

/*
 * 输入一串文字，适合临时使用
 * 需要配合 StringCallback 来充当回调函数
 *
 * @author MRXY001
 * @date   2019/3/3
 * @Change 2019/3/21
 * @Update 新增全选默认文字
 * @email  wxy19980615@gmail.com
 */

public class InputDialog {

    public static String inputDialog(Context context, String title, String def,
                                     final StringCallback stringCallback) {
        return inputDialog(context, title, def, 0, stringCallback);
    }

    public static String inputDialog(final Context context, String title, String def,
                                     final String pat, final String error,
                                     final StringCallback stringCallback) {
        return inputDialog(context, title, def, 0, pat, error, stringCallback);
    }

    /**
     * 弹出输入框
     * @param title 标题
     * @param def 默认值
     * @return 输入的字符串
     */
    public static String inputDialog(Context context, String title, String def, int inputType,
                                     final StringCallback stringCallback) {
        final String[] result = new String[1];
        LayoutInflater factory = LayoutInflater.from(context);//提示框
        final View view = factory.inflate(R.layout.edit_box, null);//这里必须是final的
        final EditText edit = (EditText) view.findViewById(R.id.editText);//获得输入框对象
        edit.setText(def);
        if (inputType > 0)
            edit.setInputType(inputType);
        if (!def.isEmpty()) {
            edit.selectAll();
        }
        new AlertDialog.Builder(context)
                .setTitle(title)//提示框标题
                .setView(view)
                .setPositiveButton("确定",//提示框的两个按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                result[0] = edit.getText().toString();
                                stringCallback.onFinish(result[0]);
                            }
                        })
                .setNegativeButton("取消", null)
                .create().show();
        return result[0];
    }

    /**
     * 弹出输入框，带错误报告
     * @param context
     * @param title
     * @param def
     * @param inputType
     * @param pat
     * @param error
     * @param stringCallback
     * @return
     */
    public static String inputDialog(final Context context, String title, String def, int inputType,
                                     final String pat, final String error,
                                     final StringCallback stringCallback) {
        final String[] result = new String[1];
        LayoutInflater factory = LayoutInflater.from(context);//提示框
        final View view = factory.inflate(R.layout.edit_box, null);//这里必须是final的
        final EditText edit = (EditText) view.findViewById(R.id.editText);//获得输入框对象
        edit.setText(def);
        if (inputType > 0)
            edit.setInputType(inputType);
        if (!def.isEmpty()) {
            edit.selectAll();
        }
        new AlertDialog.Builder(context)
                .setTitle(title)//提示框标题
                .setView(view)
                .setPositiveButton("确定",//提示框的两个按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                result[0] = edit.getText().toString();
                                if (canMatch(result[0], pat))
                                    stringCallback.onFinish(result[0]);
                                else
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton("取消", null)
                .create().show();
        return result[0];
    }

    static boolean canMatch(String str, String pat) {
        return StringUtil.canMatch(str, pat);
    }

 }
