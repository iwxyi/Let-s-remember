package com.iwxyi.letsremember;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class RememberActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private TextView mContentTv;
    private TextView mPositiveTv;
    private TextView mReverseTv;
    private TextView mDescribeTv;

    private boolean positive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember);

        initView();
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("开始记忆吧");
        }
        mContentTv = (TextView) findViewById(R.id.tv_content);
        mContentTv.setOnClickListener(this);
        mPositiveTv = (TextView) findViewById(R.id.tv_positive);
        mPositiveTv.setOnClickListener(this);
        mReverseTv = (TextView) findViewById(R.id.tv_reverse);
        mReverseTv.setOnClickListener(this);
        mDescribeTv = (TextView) findViewById(R.id.tv_describe);
        mDescribeTv.setOnClickListener(this);

        //mContentTv.setFocusableInTouchMode(false);
        //mContentTv.setEnabled(false);
        mContentTv.setTextIsSelectable(true);
        mContentTv.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_content:
                // TODO 19/03/24
                break;
            case R.id.tv_positive:
                mPositiveTv.setTextColor(getResources().getColor(R.color.white));
                mPositiveTv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                mReverseTv.setTextColor(getResources().getColor(R.color.fontBlack));
                mReverseTv.setBackgroundColor(getResources().getColor(R.color.transparent));
                break;
            case R.id.tv_reverse:
                mReverseTv.setTextColor(getResources().getColor(R.color.white));
                mReverseTv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                mPositiveTv.setTextColor(getResources().getColor(R.color.fontBlack));
                mPositiveTv.setBackgroundColor(getResources().getColor(R.color.transparent));
                break;
            case R.id.tv_describe:
                // TODO 19/03/24
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {

        return false;
    }
}
