package com.iwxyi.letsremember.User;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.iwxyi.letsremember.Global.App;
import com.iwxyi.letsremember.Global.Def;
import com.iwxyi.letsremember.Global.Paths;
import com.iwxyi.letsremember.Global.User;
import com.iwxyi.letsremember.R;
import com.iwxyi.letsremember.Utils.ConnectUtil;
import com.iwxyi.letsremember.Utils.StringCallback;
import com.iwxyi.letsremember.Utils.XmlParser;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameTv;
    private EditText mPasswordTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        ActionBar actionBar = this.getActionBar();
        if (actionBar != null)
            actionBar.setTitle("用户登录");

        mUsernameTv = (EditText) findViewById(R.id.tv_username);
        mPasswordTv = (EditText) findViewById(R.id.tv_password);

        mUsernameTv.setText(App.getVal("username"));
        mPasswordTv.setText(App.getVal("password"));
    }

    public void toLogin(View view) {
        final String username = mUsernameTv.getText().toString();
        final String password = mPasswordTv.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入数据", Toast.LENGTH_SHORT).show();
            return ;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(this, "请稍等", "正在登录", true, false);
        String[] params = {"username", username, "password", password};
        ConnectUtil.Post(Paths.getNetpath("login"), params, new StringCallback(){
            @Override
            public void onFinish(String result) {
                progressDialog.dismiss();
                XmlParser xmlParser = new XmlParser(result);
                if (xmlParser.judgeSuccess()) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                    User.user_id = xmlParser.getInt("user_id");
                    User.nickname = xmlParser.get("nickname");
                    App.setVal("username", User.username = username);
                    App.setVal("password", User.password = password);

                    setResult(Def.code_login);
                    finish();
                }
            }
        });
    }

    public void toRegister(View view) {
       startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), Def.code_register);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Def.code_register) {
            setResult(Def.code_login);
            finish();
        }
    }
}
