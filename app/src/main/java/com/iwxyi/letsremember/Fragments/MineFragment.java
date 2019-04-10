package com.iwxyi.letsremember.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.iwxyi.letsremember.R;
import com.iwxyi.letsremember.Rank.RankActivity;

public class MineFragment extends Fragment implements View.OnClickListener {

    private Button mWorldRankBtn;

    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    private void initView(@NonNull final View itemView) {
        mWorldRankBtn = (Button) itemView.findViewById(R.id.btn_world_rank);
        mWorldRankBtn.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_world_rank:
                startActivity(new Intent(getContext(), RankActivity.class));
                break;
            default:
                break;
        }
    }
}
