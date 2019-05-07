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
import android.widget.Toast;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.Globals.User;
import com.iwxyi.letsremember.R;
import com.iwxyi.letsremember.Rank.RankActivity;
import com.iwxyi.letsremember.Users.PersonActivity;
import com.iwxyi.letsremember.Users.SettingsActivity;
import com.iwxyi.letsremember.Views.RoundImageView;

public class NavMineFragment extends Fragment implements View.OnClickListener {

    private Button mWorldRankBtn;
    private Button mSettingsBtn;
    private RoundImageView mMineHeadIm;
    private TextView mMineNameTv;
    private TextView mMineNameDescTv;
    private Button mFriendGroupBtn;
    private Button mGroupRankBtn;

    public static NavMineFragment newInstance(String param1, String param2) {
        NavMineFragment fragment = new NavMineFragment();
        return fragment;
    }

    private void initView(@NonNull final View itemView) {
        mWorldRankBtn = (Button) itemView.findViewById(R.id.btn_world_rank);
        mWorldRankBtn.setOnClickListener(this);
        mSettingsBtn = (Button) itemView.findViewById(R.id.btn_settings);
        mSettingsBtn.setOnClickListener(this);
        mMineHeadIm = (RoundImageView) itemView.findViewById(R.id.im_mine_head);
        mMineNameTv = (TextView) itemView.findViewById(R.id.tv_mine_name);
        mMineNameDescTv = (TextView) itemView.findViewById(R.id.tv_mine_name_desc);
        mFriendGroupBtn = (Button) itemView.findViewById(R.id.btn_friend_group);
        mFriendGroupBtn.setOnClickListener(this);
        mGroupRankBtn = (Button) itemView.findViewById(R.id.btn_group_rank);
        mGroupRankBtn.setOnClickListener(this);

        refreshInformation();

        mMineHeadIm.setOnClickListener(this);
        mMineNameTv.setOnClickListener(this);
        mMineNameDescTv.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        return view;
    }

    public void refreshInformation() {
        if (mMineNameTv == null) {
            return;
        }
        mMineNameTv.setText(User.getName());
    }

    public void showPersonInformation() {
        startActivity(new Intent(App.getContext(), PersonActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_world_rank:
                startActivity(new Intent(getContext(), RankActivity.class));
                break;
            case R.id.btn_group_rank:
                Toast.makeText(getContext(), "您目前还没有好友", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_friend_group:
                Toast.makeText(getContext(), "您目前还没有好友", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_settings:
                startActivity(new Intent(getContext(), SettingsActivity.class));
                break;
            case R.id.im_mine_head:
            case R.id.tv_mine_name:
            case R.id.tv_mine_name_desc:
                showPersonInformation();
                break;
            default:
                break;
        }
    }
}
