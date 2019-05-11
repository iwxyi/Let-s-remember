package com.iwxyi.letsremember.Boxs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.Globals.Def;
import com.iwxyi.letsremember.Globals.Paths;
import com.iwxyi.letsremember.Globals.User;
import com.iwxyi.letsremember.R;
import com.iwxyi.letsremember.Utils.FileUtil;
import com.iwxyi.letsremember.Utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoxItemsFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private static final String ARG_BOX_TYPE = "box-type";
    private int boxType = Def.NONE_BOX;
    private OnBoxFragmentInteractionListener mListener;

    BoxContent boxContent;

    public BoxItemsFragment() {
    }

    @SuppressWarnings("unused")
    public static BoxItemsFragment newInstance(int columnCount) {
        BoxItemsFragment fragment = new BoxItemsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public static BoxItemsFragment newInstance(int columnCount, int type) {
        BoxItemsFragment fragment = new BoxItemsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putInt(ARG_BOX_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            boxType = getArguments().getInt(ARG_BOX_TYPE);
        }

        boxContent = new BoxContent();
        boxContent.refreshContent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boxitem_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyIceItemRecyclerViewAdapter(boxContent.ITEMS, mListener));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBoxFragmentInteractionListener) {
            mListener = (OnBoxFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBoxFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnBoxFragmentInteractionListener {
        void onBoxItemClicked(BoxContent.BoxItem item);
    }

    public static class MyIceItemRecyclerViewAdapter extends RecyclerView.Adapter<MyIceItemRecyclerViewAdapter.ViewHolder> {

        private final List<BoxContent.BoxItem> mValues;
        private final OnBoxFragmentInteractionListener mListener;

        public MyIceItemRecyclerViewAdapter(List<BoxContent.BoxItem> items, OnBoxFragmentInteractionListener listener) {
            mValues = items;
            mListener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_iceitem, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(""+mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);
            holder.mDetailView.setText(mValues.get(position).details);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.onBoxItemClicked(holder.mItem);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public final TextView mDetailView;
            public BoxContent.BoxItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.item_number);
                mContentView = (TextView) view.findViewById(R.id.content);
                mDetailView = (TextView) view.findViewById(R.id.detail);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    public class BoxContent {

        public final List<BoxItem> ITEMS = new ArrayList<BoxItem>();

        public final Map<String, BoxItem> ITEM_MAP = new HashMap<String, BoxItem>();

        public void refreshContent() {
            ITEMS.clear();
            ITEM_MAP.clear();

            int all_index = 1;
            // 遍历文件夹
            File pack_dir = new File(Paths.getLocalPath("material"));
            File[] packages = pack_dir.listFiles();
            for (int i = 0; i < packages.length; i++) {
                // 获取包名和包路径
                String pack_name = packages[i].getName();
                String pack_path = packages[i].getPath();
                Log.i("===pack", pack_path);
                // 遍历文件
                File sect_dir = new File(pack_path);
                File[] sections = sect_dir.listFiles();
                for (int j = 0; j < sections.length; j++) {
                    // 获取章节名字和路径
                    String sect_name = sections[j].getName();
                    String sect_path = sections[j].getPath();
                    sect_name = sect_name.substring(0, sect_name.length()-4);
                    // 遍历章节内所有的文件
                    String file_content = FileUtil.readTextVals(sect_path);
                    String[] card_list = StringUtil.getXmls(file_content, "card").toArray(new String[0]);
                    for (int k = 0; k < card_list.length; k++) {
                        String card = card_list[k];
                        // 判断记忆卡片的盒子类型，如果是当前的盒子则开始判断
                        int box = StringUtil.getXmlInt(card, "box");
                        if (box == boxType) {
                            String content = StringUtil.getXml(card, "content").trim();
                            addItem(new BoxItem(all_index++, pack_name, sect_name, k+1, content, 0));
                        }
                    }
                }
            }

            if (boxType == Def.ICE_BOX){
                App.setVal("boxIceCount", User.boxIceCount = all_index-1);
            } else if (boxType == Def.WOOD_BOX){
                App.setVal("boxWoodCount", User.boxWoodCount = all_index-1);
            } else if (boxType == Def.COPPER_BOX){
                App.setVal("boxCopperCount", User.boxCopperCount = all_index-1);
            }
        }

        private void addItem(BoxItem item) {
            ITEMS.add(item);
            ITEM_MAP.put(""+item.id, item);
        }


        public class BoxItem {

            public int id;
            public String pack;
            public String sect;
            public int index;
            public String card;
            public int time;

            public final String content;
            public final String details;

            public BoxItem(String id, String content, String details) {
                this.id = Integer.parseInt(id);
                this.content = content;
                this.details = details;
            }

            public BoxItem(int id, String p, String s, int i, String c, int t) {
                this.id = id;
                this.pack = p;
                this.sect = s;
                this.index = i;
                this.card = c;
                this.time = t;

                this.content = pack + " / " + sect + " / " + i;
                this.details = card;

            }

            @Override
            public String toString() {
                return content;
            }
        }
    }
}
