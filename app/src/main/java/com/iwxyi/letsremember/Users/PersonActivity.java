package com.iwxyi.letsremember.Users;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.Globals.Paths;
import com.iwxyi.letsremember.Globals.User;
import com.iwxyi.letsremember.R;
import com.iwxyi.letsremember.Utils.ConnectUtil;
import com.iwxyi.letsremember.Utils.StringCallback;
import com.iwxyi.letsremember.Utils.StringUtil;

public class PersonActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mNicknameTv;
    private TextView mUsernameTv;
    private TextView mPasswordTv;
    private TextView mMobileTv;
    private TextView mEmailTv;
    private TextView mIntegralTv;
    private Button mLogoutBtn;
    private FloatingActionButton mFab;
    private TextView mTermsTv;
    private TextView mCountTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        initView();
        initData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initView() {
        mNicknameTv = (TextView) findViewById(R.id.tv_nickname);
        mNicknameTv.setOnClickListener(this);
        mUsernameTv = (TextView) findViewById(R.id.tv_username);
        mUsernameTv.setOnClickListener(this);
        mPasswordTv = (TextView) findViewById(R.id.tv_password);
        mPasswordTv.setOnClickListener(this);
        mMobileTv = (TextView) findViewById(R.id.tv_mobile);
        mMobileTv.setOnClickListener(this);
        mEmailTv = (TextView) findViewById(R.id.tv_email);
        mEmailTv.setOnClickListener(this);
        mIntegralTv = (TextView) findViewById(R.id.tv_integral);
        mIntegralTv.setOnClickListener(this);
        mLogoutBtn = (Button) findViewById(R.id.btn_logout);
        mLogoutBtn.setOnClickListener(this);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);
        mTermsTv = (TextView) findViewById(R.id.tv_terms);
        mCountTv = (TextView) findViewById(R.id.tv_count);
    }

    private void initData() {
        mNicknameTv.setText(User.nickname);
        mUsernameTv.setText(User.username);
        mPasswordTv.setText(User.password);
        mMobileTv.setText(User.mobile);
        mEmailTv.setText(User.email);
        mIntegralTv.setText("" + User.integral);

        int firstOpen = App.getInt("firstOpen");
        int now = App.getTimestamp();
        int delta = now - firstOpen; // 秒
        int day = delta / 3600 / 24;
        int hour = delta % (3600 * 24) / 3600;
        int minute = delta % 3600 / 60;
        String usedDay = String.format("%d天%d小时%d分钟", day, hour, minute);
        mTermsTv.setText(usedDay);

        int num = App.getInt("count");
        mCountTv.setText("共背诵" + num + "篇");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_nickname:
                inputDialog("nickname", "修改用户昵称", User.nickname);
                break;
            case R.id.tv_username:
                App.toast("不能修改用户名");
                break;
            case R.id.tv_password:
                inputDialog("password", "修改用户密码", User.password);
                break;
            case R.id.tv_mobile:
                inputDialog("mobile", "修改手机号", User.mobile);
                break;
            case R.id.tv_email:
                inputDialog("email", "修改邮箱", User.email);
                break;
            case R.id.btn_logout:
                AlertDialog dialog = new AlertDialog.Builder(PersonActivity.this)
                        .setTitle("提示")
                        .setMessage("是否确认退出登录？\n已有数据仍然存在，在下次登录时重新同步至云端")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                User.user_id = 0;
                                User.username = User.password = User.nickname = User.mobile = User.email = "";
                                App.setVal("user_id", 0);
                                App.setVal("password", "");
                                PersonActivity.this.finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;
            case R.id.fab:
                Snackbar.make(findViewById(R.id.fab), "用户反馈邮箱：482582886@qq.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            default:
                break;
        }
    }

    /**
     * 弹出输入框
     *
     * @param aim   目标
     * @param title 标题
     * @param def   默认值
     * @return 输入的字符串
     */
    private String inputDialog(final String aim, String title, String def) {
        final String[] result = new String[1];
        LayoutInflater factory = LayoutInflater.from(PersonActivity.this);//提示框
        final View view = factory.inflate(R.layout.edit_box, null);//这里必须是final的
        final EditText edit = (EditText) view.findViewById(R.id.editText);//获得输入框对象
        edit.setText(def);
        new AlertDialog.Builder(PersonActivity.this)
                .setTitle(title)//提示框标题
                .setView(view)
                .setPositiveButton("确定",//提示框的两个按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                result[0] = edit.getText().toString();
                                onInputDialog(aim, edit.getText().toString());
                            }
                        })
                .setNegativeButton("取消", null)
                .create().show();
        return result[0];
    }

    /**
     * 输入框结束事件
     *
     * @param aim 目标
     * @param s   字符串
     */
    private void onInputDialog(String aim, String s) {

        switch (aim) {
            case "nickname":
                if (!canMatch(s, "\\S+")) {
                    App.toast("用户名不能有空格");
                    return;
                }
                mNicknameTv.setText(User.nickname = s);
                break;
            case "username":
                if (!canMatch(s, "\\S+")) {
                    App.toast("用户名不能有空格");
                    return;
                }
                mUsernameTv.setText(User.username = s);
                break;
            case "password":
                if (!canMatch(s, "\\S+")) {
                    App.toast("密码不能有空格");
                    return;
                }
                mPasswordTv.setText(User.password = s);
                break;
            case "mobile":
                if (!canMatch(s, "\\+?\\d{5,15}")) {
                    App.toast("手机号格式不正确");
                    return;
                }
                mMobileTv.setText(User.mobile = s);
                break;
            case "email":
                if (!canMatch(s, "[\\w\\.]+@[\\.\\w]+")) {
                    App.toast("邮箱格式不正确");
                    return;
                }
                mEmailTv.setText(User.email = s);
                break;
        }

        updateContent(aim, s);
    }

    /**
     * 上传用户信息
     *
     * @param key 文件名
     * @param val 数值
     */
    private void updateContent(final String key, final String val) {
        String path = Paths.getNetpath("updateUserInfo");
        String[] params = new String[]{"user_id", User.id(), key, val};
        ConnectUtil.Get(path, params, new StringCallback() {
            @Override
            public void onFinish(String content) {
                content = StringUtil.getXml(content, "result");
                if (content.equals("OK") || content.equals("1")) {
                    Snackbar.make(findViewById(R.id.fab), "修改成功", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (!content.isEmpty()) {
                    Snackbar.make(findViewById(R.id.fab), "修改失败:" + StringUtil.getXml(content, "result"), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    boolean canMatch(String str, String pat) {
        return StringUtil.canMatch(str, pat);
    }
}
