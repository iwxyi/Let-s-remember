package com.iwxyi.letsremember.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.Globals.Def;
import com.iwxyi.letsremember.Globals.User;
import com.iwxyi.letsremember.Material.MaterialSelectActivity;
import com.iwxyi.letsremember.R;
import com.iwxyi.letsremember.TypeinActivity;
import com.iwxyi.letsremember.Users.LoginActivity;
import com.iwxyi.letsremember.Users.PersonActivity;
import com.iwxyi.letsremember.Utils.InputDialog;
import com.iwxyi.letsremember.Utils.StringCallback;
import com.iwxyi.letsremember.Views.RoundImageView;

public class NavHomeFragment extends Fragment implements View.OnClickListener {

    private Button mStartRememberBtn;
    private Button mMyTypeinBtn;
    private RoundImageView mHeadIm;
    private TextView mInspirationTv;
    private TextView mMemoryPlanTv;
    private TextView mRemainDaysTv;
    private Button mChangePlanBtn;

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
        mMyTypeinBtn = (Button) itemView.findViewById(R.id.btn_my_typein);
        mMyTypeinBtn.setOnClickListener(this);
        mHeadIm = (RoundImageView) itemView.findViewById(R.id.im_head);
        mHeadIm.setOnClickListener(this);
        mInspirationTv = (TextView) itemView.findViewById(R.id.tv_inspiration);
        mMemoryPlanTv = (TextView) itemView.findViewById(R.id.tv_memory_plan);
        mRemainDaysTv = (TextView) itemView.findViewById(R.id.tv_remain_days);
        mChangePlanBtn = (Button) itemView.findViewById(R.id.btn_change_plan);
        mChangePlanBtn.setOnClickListener(this);

        mInspirationTv.setOnClickListener(this);
    }

    private void initData() {
        if (!App.getVal("inspiration").isEmpty()) {
            mInspirationTv.setText(App.getVal("inspiration"));
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
                startActivity(new Intent(getContext(), MaterialSelectActivity.class));
                break;
            case R.id.btn_my_typein:
                startActivity(new Intent(getContext(), TypeinActivity.class));
                break;
            case R.id.btn_change_plan:// TODO 19/03/17
                break;
            case R.id.tv_inspiration:
                InputDialog.inputDialog(getContext(), "请输入励志语", App.getVal("inspiration"),
                        new StringCallback(){
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
            default:
                break;
        }
    }
}
