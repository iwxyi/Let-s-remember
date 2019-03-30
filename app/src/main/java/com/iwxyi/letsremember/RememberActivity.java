package com.iwxyi.letsremember;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.iwxyi.letsremember.Material.ChapterManager;

public class RememberActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mContentTv;
    private TextView mPositiveTv;
    private TextView mReverseTv;
    private TextView mDescribeTv;

    private boolean positive = false;
    ChapterManager chapter_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember);

        initView();
        initData();
    }

    @SuppressLint("ObsoleteSdkInt")
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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Toast.makeText(this, "安卓版本过低（<23），将无法使用文字隐藏的功能", Toast.LENGTH_SHORT).show();
        }
        mContentTv.setCustomSelectionActionModeCallback(new ActionMode.Callback2() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                if (inflater != null) {
                    inflater.inflate(R.menu.menu_material, menu);
                }
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.hide) {
                    final int selectStart = mContentTv.getSelectionStart();
                    final int selectEnd = mContentTv.getSelectionEnd();
                    int start = Math.max(0, Math.min(selectStart, selectEnd));
                    int end = Math.max(0, Math.max(selectStart, selectEnd));

                } else if (item.getItemId() == R.id.show) {

                } else if (item.getItemId() == R.id.search) {

                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });


    }

    private void initData() {
        chapter_manager = new ChapterManager("index", "index");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_content:
                // TODO 19/03/24
                break;
            case R.id.tv_positive:
                mPositiveTv.setTextColor(getResources().getColor(R.color.white));
                mPositiveTv.setBackground(getResources().getDrawable(R.drawable.button_border_selected));
                mReverseTv.setTextColor(getResources().getColor(R.color.fontBlack));
                mReverseTv.setBackground(getResources().getDrawable(R.drawable.button_border));
                break;
            case R.id.tv_reverse:
                mReverseTv.setTextColor(getResources().getColor(R.color.white));
                mReverseTv.setBackground(getResources().getDrawable(R.drawable.button_border_selected));
                mPositiveTv.setTextColor(getResources().getColor(R.color.fontBlack));
                mPositiveTv.setBackground(getResources().getDrawable(R.drawable.button_border));
                break;
            case R.id.tv_describe:
                // TODO 19/03/24
                break;
            default:
                break;
        }
    }
}
