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
import com.iwxyi.letsremember.RememberActivity;
import com.iwxyi.letsremember.TypeinActivity;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private Button mStartRememberBtn;
    private Button mMyTypeinBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }

    private void initView(@NonNull final View itemView) {
        mStartRememberBtn = (Button) itemView.findViewById(R.id.btn_start_remember);
        mStartRememberBtn.setOnClickListener(this);
        mMyTypeinBtn = (Button) itemView.findViewById(R.id.btn_my_typein);
        mMyTypeinBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_remember:
                startActivity(new Intent(getContext(), RememberActivity.class));
                break;
            case R.id.btn_my_typein:
                startActivity(new Intent(getContext(), TypeinActivity.class));
                break;
            default:
                break;
        }
    }
}
