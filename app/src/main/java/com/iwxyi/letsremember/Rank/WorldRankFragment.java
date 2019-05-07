package com.iwxyi.letsremember.Rank;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iwxyi.letsremember.Globals.Paths;
import com.iwxyi.letsremember.R;
import com.iwxyi.letsremember.Rank.WorldRankContent.WorldRankItem;
import com.iwxyi.letsremember.Utils.ConnectUtil;
import com.iwxyi.letsremember.Utils.StringCallback;
import com.iwxyi.letsremember.Utils.XmlParser;

public class WorldRankFragment extends Fragment  {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnWorldRankInteractionListener mListener;
    private MyWorldRankRecyclerViewAdapter adapter;

    public WorldRankFragment() {
    }

    @SuppressWarnings("unused")
    public static WorldRankFragment newInstance(int columnCount) {
        WorldRankFragment fragment = new WorldRankFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worldrank_list, container, false);

        // 初始化内容
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyWorldRankRecyclerViewAdapter(WorldRankContent.ITEMS, mListener);
            recyclerView.setAdapter(adapter);
        }

        // 获取排行榜
        ConnectUtil.Get(Paths.getNetPath("getRank"), new StringCallback(){
            @Override
            public void onFinish(String content) {
                WorldRankContent.initFromStr(content);
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWorldRankInteractionListener) {
            mListener = (OnWorldRankInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnWorldRankInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnWorldRankInteractionListener {
        void onWorldRankInteraction(WorldRankContent.WorldRankItem item);
    }
}
