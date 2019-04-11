package com.iwxyi.letsremember.Material;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.R;
import com.iwxyi.letsremember.Material.SectionsContent.SectionItem;

public class SectionsFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnSectionsFragmentInteractionListener mListener;
    private View global_view;

    public SectionsFragment() {
    }

    @SuppressWarnings("unused")
    public static SectionsFragment newInstance(int columnCount) {
        SectionsFragment fragment = new SectionsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public static SectionsFragment newInstance() {
        return newInstance(1);
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
        View view = inflater.inflate(R.layout.fragment_sections_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MySectionsRecyclerViewAdapter(SectionsContent.ITEMS, mListener));
        }

        global_view = view;
        return view;
    }

    public void refreshSections(String package_name) {
        SectionsContent.refreshSections(package_name);
        if (global_view != null && global_view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) global_view;
            recyclerView.setAdapter(new MySectionsRecyclerViewAdapter(SectionsContent.ITEMS, mListener));
        }
    }

    public void refreshSections() {
        refreshSections(App.getVal("selected_package"));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSectionsFragmentInteractionListener) {
            mListener = (OnSectionsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSectionsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSectionsFragmentInteractionListener {
        void onSectionClicked(SectionItem item);
    }
}
