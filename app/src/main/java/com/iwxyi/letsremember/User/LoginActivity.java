package com.iwxyi.letsremember.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.iwxyi.letsremember.Global.Def;
import com.iwxyi.letsremember.R;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameTv;
    private EditText mPasswordTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    public void toLogin(View view) {
        String username = mUsernameTv.getText().toString();
        String password = mPasswordTv.getText().toString();
    }

    public void toRegister(View view) {
       startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), Def.code_register);
    }

    private void initView() {
        mUsernameTv = (EditText) findViewById(R.id.tv_username);
        mPasswordTv = (EditText) findViewById(R.id.tv_password);
    }
}
