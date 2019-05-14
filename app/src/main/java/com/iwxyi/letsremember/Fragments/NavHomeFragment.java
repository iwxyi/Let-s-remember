package com.iwxyi.letsremember.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.Globals.Def;
import com.iwxyi.letsremember.Globals.User;
import com.iwxyi.letsremember.Material.CardManager;
import com.iwxyi.letsremember.Material.MaterialSelectActivity;
import com.iwxyi.letsremember.R;
import com.iwxyi.letsremember.TypeinActivity;
import com.iwxyi.letsremember.Users.LoginActivity;
import com.iwxyi.letsremember.Users.PersonActivity;
import com.iwxyi.letsremember.Utils.InputDialog;
import com.iwxyi.letsremember.Utils.StringCallback;
import com.iwxyi.letsremember.Views.RoundImageView;

public class NavHomeFragment extends Fragment implements View.OnClickListener {

    private static final int CODE_REMEMBER = 10;
    private Button mStartRememberBtn;
    private RoundImageView mHeadIm;
    private TextView mInspirationTv;
    private TextView mMemoryPlanTv;
    private TextView mRemainDaysTv;
    private Button mChangePlanBtn;
    private CardView mRecent1Cd;
    private TextView mRecentTitle2Tv;
    private TextView mRecentDetail2Tv;
    private CardView mRecent2Cd;
    private TextView mRecentTitle3Tv;
    private TextView mRecentDetail3Tv;
    private CardView mRecent3Cd;
    private TextView mRecentTitle1Tv;
    private TextView mRecentDetail1Tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(@NonNull final View itemView) {
        mStartRememberBtn = (Button) itemView.findViewById(R.id.btn_start_remember);
        mStartRememberBtn.setOnClickListener(this);
        mHeadIm = (RoundImageView) itemView.findViewById(R.id.im_head);
        mHeadIm.setOnClickListener(this);
        mInspirationTv = (TextView) itemView.findViewById(R.id.tv_inspiration);
        mMemoryPlanTv = (TextView) itemView.findViewById(R.id.tv_memory_plan);
        mRemainDaysTv = (TextView) itemView.findViewById(R.id.tv_remain_days);
        mChangePlanBtn = (Button) itemView.findViewById(R.id.btn_change_plan);
        mChangePlanBtn.setOnClickListener(this);

        mInspirationTv.setOnClickListener(this);
        mRecentTitle1Tv = (TextView) itemView.findViewById(R.id.tv_recent_title1);
        mRecentDetail1Tv = (TextView) itemView.findViewById(R.id.tv_recent_detail1);
        mRecent1Cd = (CardView) itemView.findViewById(R.id.cd_recent1);
        mRecent1Cd.setOnClickListener(this);
        mRecentTitle2Tv = (TextView) itemView.findViewById(R.id.tv_recent_title2);
        mRecentDetail2Tv = (TextView) itemView.findViewById(R.id.tv_recent_detail2);
        mRecent2Cd = (CardView) itemView.findViewById(R.id.cd_recent2);
        mRecent2Cd.setOnClickListener(this);
        mRecentTitle3Tv = (TextView) itemView.findViewById(R.id.tv_recent_title3);
        mRecentDetail3Tv = (TextView) itemView.findViewById(R.id.tv_recent_detail3);
        mRecent3Cd = (CardView) itemView.findViewById(R.id.cd_recent3);
        mRecent3Cd.setOnClickListener(this);
    }

    public void initData() {
        if (mInspirationTv == null) { // 没有初始化的话就不进行修改数据
            return ;
        }

        if (!App.getVal("inspiration").isEmpty()) {
            mInspirationTv.setText(App.getVal("inspiration"));
        }

        // 读取最新的记录
        if (!App.getVal("recent_package_1").equals("") && !App.getVal("recent_package_1").equals("0")) {
            CardManager manager = new CardManager(App.getVal("recent_package_1"), App.getVal("recent_section_1"));
            if (App.getInt("recent_card_1") > 0)
                manager.jumpChapterOnly(App.getInt("recent_card_1"));
            String title = manager.pack_name + " / " + manager.sect_name + " / "
                    + (manager.index + 1) + "  " + (int) (manager.index * 100 / manager.getCount()) + "%";
            mRecentTitle1Tv.setText(title);
            mRecentDetail1Tv.setText(manager.getContent());
        } else {
            mRecent1Cd.setVisibility(View.GONE);
        }

        // 读取最新的记录
        if (!App.getVal("recent_package_2").equals("")) {
            CardManager manager = new CardManager(App.getVal("recent_package_2"), App.getVal("recent_section_2"));

            if (manager.jumpChapterOnly(App.getInt("recent_card_2"))) {
                String title = manager.pack_name + " / " + manager.sect_name + " / "
                        + (manager.index + 1) + "  " + (int) (manager.index * 100 / manager.getCount()) + "%";
                mRecentTitle2Tv.setText(title);
                mRecentDetail2Tv.setText(manager.getContent());
            } else {
                mRecent2Cd.setVisibility(View.GONE);
            }
        } else {
            mRecent2Cd.setVisibility(View.GONE);
        }

        // 读取最新的记录
        if (!App.getVal("recent_package_3").equals("") && !App.getVal("recent_package_3").equals("0")) {
            CardManager manager = new CardManager(App.getVal("recent_package_3"), App.getVal("recent_section_3"));
            if (manager.jumpChapterOnly(App.getInt("recent_card_3"))) {
                String title = manager.pack_name + " / " + manager.sect_name + " / "
                        + (manager.index + 1) + "  " + (int) (manager.index * 100 / manager.getCount()) + "%";
                mRecentTitle3Tv.setText(title);
                mRecentDetail3Tv.setText(manager.getContent());
            } else {
                mRecent3Cd.setVisibility(View.GONE);
            }
        } else {
            mRecent3Cd.setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CODE_REMEMBER:
                initData();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_head:
                if (!User.isLogin()) {
                    startActivityForResult(new Intent(getContext(), LoginActivity.class), Def.code_login);
                } else {
                    startActivityForResult(new Intent(getContext(), PersonActivity.class), Def.code_person);
                }
                break;
            case R.id.btn_start_remember:
                startActivityForResult(new Intent(getContext(), MaterialSelectActivity.class), CODE_REMEMBER);
                break;
            case R.id.btn_typein_history:
                startActivityForResult(new Intent(getContext(), TypeinActivity.class), CODE_REMEMBER);
                break;
            case R.id.btn_change_plan:// TODO 19/03/17
                break;
            case R.id.tv_inspiration:
                InputDialog.inputDialog(getContext(), "请输入励志语", App.getVal("inspiration"),
                        new StringCallback() {
                            @Override
                            public void onFinish(String content) {
                                mInspirationTv.setText(content);
                                App.setVal("inspiration", content);
                            }
                        });
                break;
            case R.id.tv_memory_plan:// TODO 19/03/17
                break;
            case R.id.tv_remain_days:// TODO 19/03/17
                break;
            case R.id.cd_recent1:// TODO 19/05/14
            {
                App.setVal("last_package", App.getVal("recent_package_1"));
                App.setVal("last_section", App.getVal("recent_section_1"));
                App.setVal("card:" + App.getVal("recent_package_1") + "/" + App.getVal("recent_section_1"), App.getInt("recent_card_1"));
                Intent activity_change = new Intent(getContext(), MaterialSelectActivity.class);
                Bundle bundle = new Bundle();// 创建Bundle对象
                bundle.putBoolean("open", true);//  放入data值为int型
                activity_change.putExtras(bundle);// 将Bundle对象放入到Intent上
                startActivityForResult(activity_change, CODE_REMEMBER);//  开始跳转
            }
            break;
            case R.id.cd_recent2:// TODO 19/05/14
            {
                App.setVal("last_package", App.getVal("recent_package_2"));
                App.setVal("last_section", App.getVal("recent_section_2"));
                App.setVal("card:" + App.getVal("recent_package_2") + "/" + App.getVal("recent_section_2"), App.getInt("recent_card_2"));
                Intent activity_change = new Intent(getContext(), MaterialSelectActivity.class);
                Bundle bundle = new Bundle();// 创建Bundle对象
                bundle.putBoolean("open", true);//  放入data值为int型
                activity_change.putExtras(bundle);// 将Bundle对象放入到Intent上
                startActivityForResult(activity_change, CODE_REMEMBER);//  开始跳转
            }
            break;
            case R.id.cd_recent3:// TODO 19/05/14
            {
                App.setVal("last_package", App.getVal("recent_package_3"));
                App.setVal("last_section", App.getVal("recent_section_3"));
                App.setVal("card:" + App.getVal("recent_package_3") + "/" + App.getVal("recent_section_3"), App.getInt("recent_card_3"));
                Intent activity_change = new Intent(getContext(), MaterialSelectActivity.class);
                Bundle bundle = new Bundle();// 创建Bundle对象
                bundle.putBoolean("open", true);//  放入data值为int型
                activity_change.putExtras(bundle);// 将Bundle对象放入到Intent上
                startActivityForResult(activity_change, CODE_REMEMBER);//  开始跳转
            }
            break;
            default:
                break;
        }
    }
}
