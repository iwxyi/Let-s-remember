package com.iwxyi.letsremember.Material;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.Globals.Def;
import com.iwxyi.letsremember.Globals.User;
import com.iwxyi.letsremember.R;
import com.iwxyi.letsremember.Utils.Recent;

import java.util.ArrayList;

public class RememberFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String package_name;
    private String section_name;

    private OnRememberFragmentInteractionListener mListener;

    private TextView mContentTv;
    private TextView mPositiveTv;
    private TextView mReverseTv;
//    private TextView mDescribeTv;

    CardManager chapter_manager;
    boolean positive = true;
    private Button mPrevBtn;
    private Button mNextBtn;
    private ImageView mCopperIv;
    private ImageView mWoodIv;
    private ImageView mIceIv;

    public RememberFragment() {

    }

    public static RememberFragment newInstance(String param1, String param2) {
        RememberFragment fragment = new RememberFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            package_name = getArguments().getString(ARG_PARAM1);
            section_name = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remember, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mContentTv = (TextView) view.findViewById(R.id.tv_content);
        mContentTv.setOnClickListener(this);
        mPositiveTv = (TextView) view.findViewById(R.id.tv_positive);
        mPositiveTv.setOnClickListener(this);
        mReverseTv = (TextView) view.findViewById(R.id.tv_reverse);
        mReverseTv.setOnClickListener(this);
//        mDescribeTv = (TextView) view.findViewById(R.id.tv_describe);
//        mDescribeTv.setOnClickListener(this);
        mPrevBtn = (Button) view.findViewById(R.id.btn_prev);
        mPrevBtn.setOnClickListener(this);
        mNextBtn = (Button) view.findViewById(R.id.btn_next);
        mNextBtn.setOnClickListener(this);

        mContentTv.setTextIsSelectable(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Toast.makeText(getContext(), "安卓版本过低（<23），将无法使用文字隐藏的功能", Toast.LENGTH_SHORT).show();
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
                    chapter_manager.increaseHide(start, end);
                    initShowed();
                    mContentTv.setSelected(false);
                } else if (item.getItemId() == R.id.show) {
                    final int selectStart = mContentTv.getSelectionStart();
                    final int selectEnd = mContentTv.getSelectionEnd();
                    int start = Math.max(0, Math.min(selectStart, selectEnd));
                    int end = Math.max(0, Math.max(selectStart, selectEnd));
                    chapter_manager.decreaseHide(start, end);
                    initShowed();
                    mContentTv.setSelected(false);
                } else if (item.getItemId() == R.id.search) {

                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        mCopperIv = (ImageView) view.findViewById(R.id.iv_copper);
        mCopperIv.setOnClickListener(this);
        mWoodIv = (ImageView) view.findViewById(R.id.iv_wood);
        mWoodIv.setOnClickListener(this);
        mIceIv = (ImageView) view.findViewById(R.id.iv_ice);
        mIceIv.setOnClickListener(this);
    }

    public void initData() {
        chapter_manager = new CardManager(
                App.getVal("last_package", "index"),
                App.getVal("last_section", "index"));

        if (mContentTv == null) {
            return;
        }
        initShowed();
    }

    private void initShowed() {
        if (positive) {
            mContentTv.setText(htmlToString(chapter_manager.getContent(), chapter_manager.getPlaces()));
        } else {
            mContentTv.setText(chapter_manager.getContent());
        }
        setBoxShowed(chapter_manager.getChapterBox());
    }

    private CharSequence htmlToString(String text, ArrayList<CardBean.PlaceBean> places) {
        String html = text, left_tag = "<font color='#FFFFFF'>", right_tag = "</font>";
        int offset = 0, left_length = left_tag.length(), right_length = right_tag.length();
        Log.i("====len", ""+text.length());
        Log.i("====text", text);
        html = html.replace(" ", "\t"); // 空格替换成制表符
        for (int i = 0; i < places.size(); i++) {
            CardBean.PlaceBean place = places.get(i);
            int start = place.start, end = place.end;
            if (start >= 0 && start <= html.length() - offset) {
                html = html.substring(0, start + offset) + left_tag + html.substring(start + offset, html.length());
                offset += left_length;
            }
            if (end > 0 && end <= html.length() - offset) {
                html = html.substring(0, end + offset) + right_tag + html.substring(end + offset, html.length());
                offset += right_length;
            }
        }
        Log.i("====章节富文本", html);
        html = html.replace("\n", "<br />");
        html = html.replace("\t", "&nbsp;"); // 把原来的空格替换成HTML实体
        CharSequence charSequence = Html.fromHtml(html);
        return charSequence;
    }

    private void setBoxShowed(int x) {
        mCopperIv.setImageResource(R.drawable.box_copper);
        mWoodIv.setImageResource(R.drawable.box_wood);
        mIceIv.setImageResource(R.drawable.box_ice);

        if (x == Def.ICE_BOX)
            mIceIv.setImageResource(R.drawable.box_ice_selected);
        if (x == Def.WOOD_BOX)
            mWoodIv.setImageResource(R.drawable.box_wood_selected);
        if (x == Def.COPPER_BOX)
            mCopperIv.setImageResource(R.drawable.box_copper_selected);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_content:
                break;
            case R.id.tv_positive:
                mPositiveTv.setTextColor(getResources().getColor(R.color.white));
                mPositiveTv.setBackground(getResources().getDrawable(R.drawable.button_border_selected));
                mReverseTv.setTextColor(getResources().getColor(R.color.fontBlack));
                mReverseTv.setBackground(getResources().getDrawable(R.drawable.button_border));
                positive = true;
                initShowed();
                break;
            case R.id.tv_reverse:
                mReverseTv.setTextColor(getResources().getColor(R.color.white));
                mReverseTv.setBackground(getResources().getDrawable(R.drawable.button_border_selected));
                mPositiveTv.setTextColor(getResources().getColor(R.color.fontBlack));
                mPositiveTv.setBackground(getResources().getDrawable(R.drawable.button_border));
                positive = false;
                initShowed();
                break;
            case R.id.btn_prev: // 返回上一个记忆卡片
                chapter_manager.moveChapter(-1);
                initShowed();
                break;
            case R.id.btn_next: // 下一个记忆卡片
                chapter_manager.addRememberTimestamp();
                chapter_manager.moveChapter(1);
                initShowed();
                Recent.addRecent(chapter_manager.pack_name, chapter_manager.sect_name, chapter_manager.index);
                break;
            case R.id.iv_copper:
                if (chapter_manager.getChapterBox() == Def.COPPER_BOX) {
                    Toast.makeText(getContext(), "取消放入铁盒子", Toast.LENGTH_SHORT).show();
                    chapter_manager.setChapterBox(Def.NONE_BOX);
                    setBoxShowed(Def.NONE_BOX);
                    App.setVal("boxCopperCount", --User.boxCopperCount);
                } else {
                    Toast.makeText(getContext(), "已放入铁盒子中，将长期记忆", Toast.LENGTH_SHORT).show();
                    chapter_manager.setChapterBox(Def.COPPER_BOX);
                    setBoxShowed(Def.COPPER_BOX);
                    App.setVal("boxCopperCount", ++User.boxCopperCount);
                }
                break;
            case R.id.iv_wood:
                if (chapter_manager.getChapterBox() == Def.WOOD_BOX) {
                    Toast.makeText(getContext(), "取消放入木盒子", Toast.LENGTH_SHORT).show();
                    chapter_manager.setChapterBox(Def.NONE_BOX);
                    setBoxShowed(Def.NONE_BOX);
                    App.setVal("boxWoodCount", --User.boxCopperCount);
                } else {
                    Toast.makeText(getContext(), "已放入木盒子中，建议一段时间后复习", Toast.LENGTH_SHORT).show();
                    chapter_manager.setChapterBox(Def.WOOD_BOX);
                    setBoxShowed(Def.WOOD_BOX);
                    App.setVal("boxWoodCount", ++User.boxCopperCount);
                }
                break;
            case R.id.iv_ice:
                if (chapter_manager.getChapterBox() == Def.ICE_BOX) {
                    Toast.makeText(getContext(), "取消放入冰盒子", Toast.LENGTH_SHORT).show();
                    chapter_manager.setChapterBox(Def.NONE_BOX);
                    setBoxShowed(Def.NONE_BOX);
                    App.setVal("boxIceCount", --User.boxCopperCount);
                } else {
                    Toast.makeText(getContext(), "已放入冰盒子中，建议稍后复习", Toast.LENGTH_SHORT).show();
                    chapter_manager.setChapterBox(Def.ICE_BOX);
                    setBoxShowed(Def.ICE_BOX);
                    App.setVal("boxIceCount", ++User.boxIceCount);
                }
                break;
            default:
                break;
        }
    }

    public void setChapter(int x) {
        chapter_manager.jumpChapter(x);
        initShowed();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onRememberFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRememberFragmentInteractionListener) {
            mListener = (OnRememberFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRememberFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnRememberFragmentInteractionListener {
        void onRememberFragmentInteraction(Uri uri);

        void onCardRemember(int index);
    }
}
