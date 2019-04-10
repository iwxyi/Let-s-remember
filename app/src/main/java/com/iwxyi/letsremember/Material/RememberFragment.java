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
import android.widget.TextView;
import android.widget.Toast;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.R;

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
    }

    public void initData() {
        chapter_manager = new CardManager(
                App.getVal("last_package", "index"),
                App.getVal("last_section", "index"));
        if (mContentTv == null) {
            return ;
        }
        initShowed();
    }

    private void initShowed() {
        App.deb("initShowed():"+chapter_manager.getContent());
        if (positive) {
            mContentTv.setText(htmlToString(chapter_manager.getContent(), chapter_manager.getPlaces()));
        } else {
            mContentTv.setText(chapter_manager.getContent());
        }
//        mDescribeTv.setText(chapter_manager.getDescription());
    }

    private CharSequence htmlToString(String text, ArrayList<CardBean.PlaceBean> places) {
        String html = text, left_tag = "<font color='#FFFFFF'>", right_tag = "</font>";
        int offset = 0, left_length = left_tag.length(), right_length = right_tag.length();
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
        CharSequence charSequence = Html.fromHtml(html);
        return charSequence;
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
            case R.id.btn_prev:// TODO 19/03/31
                chapter_manager.moveChapter(-1);
                initShowed();
                break;
            case R.id.btn_next:// TODO 19/03/31
                chapter_manager.addRememberTimestamp();
                chapter_manager.moveChapter(1);
                initShowed();
                break;
            default:
                break;
        }
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
    }
}
