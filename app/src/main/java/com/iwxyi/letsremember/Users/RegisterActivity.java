package com.iwxyi.letsremember.Users;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.Globals.Def;
import com.iwxyi.letsremember.Globals.Paths;
import com.iwxyi.letsremember.Globals.User;
import com.iwxyi.letsremember.R;
import com.iwxyi.letsremember.Utils.ConnectUtil;
import com.iwxyi.letsremember.Utils.StringCallback;
import com.iwxyi.letsremember.Utils.XmlParser;

public class RegisterActivity extends AppCompatActivity {

    private EditText mUsernameTv;
    private EditText mPasswordTv;
    private EditText mNicknameTv;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        ActionBar actionBar = this.getActionBar();
        if (actionBar != null) {
            actionBar.setTitle("用户注册");
        }

        mUsernameTv = (EditText) findViewById(R.id.tv_username);
        mPasswordTv = (EditText) findViewById(R.id.tv_password);
        mNicknameTv = (EditText) findViewById(R.id.tv_nickname);
    }

    public void toRegister(View view) {
        final String username = mUsernameTv.getText().toString();
        final String password = mPasswordTv.getText().toString();
        final String nickname = mNicknameTv.getText().toString();
        if (username.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
            Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
            return ;
        }

        progressDialog = ProgressDialog.show(this, "请稍等", "正在注册", true, false);
        String[] param = {"username", username, "password", password, "nickname", nickname};
        ConnectUtil.Post(Paths.getNetPath("register"), param, new StringCallback(){
            @Override
            public void onFinish(String content) {
                progressDialog.dismiss();
                XmlParser xmlParser = new XmlParser(content);
                if (xmlParser.judgeSuccess()) {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

                    App.setVal("username", User.username = username);
                    App.setVal("password", User.password = password);
                    App.setVal("nickname", User.nickname = nickname);
                    User.user_id = xmlParser.getInt("user_id");

                    setResult(Def.code_register);
                    finish();
                }
            }
        });

    }
}
