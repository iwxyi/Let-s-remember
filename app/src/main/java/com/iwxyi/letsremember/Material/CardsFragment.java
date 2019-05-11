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
import com.iwxyi.letsremember.Material.CardsContent.CardItem;

public class CardsFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnCardsFragmentInteractionListener mListener;
    private View global_view;

    public CardsFragment() {
    }

    @SuppressWarnings("unused")
    public static CardsFragment newInstance(int columnCount) {
        CardsFragment fragment = new CardsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public static CardsFragment newInstance() {
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
        View view = inflater.inflate(R.layout.fragment_cards_list, container, false);
        refreshCards();

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyCardsRecyclerViewAdapter(CardsContent.ITEMS, mListener));
        }
        global_view = view;
        return view;
    }

    public void refreshCards(String pack, String sect) {
        CardsContent.refreshCards(pack, sect);
        if (global_view != null && global_view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) global_view;
            recyclerView.setAdapter(new MyCardsRecyclerViewAdapter(CardsContent.ITEMS, mListener));
        }
    }

    public void refreshCards() {
        refreshCards(App.getVal("last_package"), App.getVal("last_section"));
    }

    public void refreshOneCard(int index) {
        refreshCards();
        CardsContent.setIndex(index);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCardsFragmentInteractionListener) {
            mListener = (OnCardsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCardsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCardsFragmentInteractionListener {
        void onCardClicked(CardItem item);
        void onCardClicked(int index);
    }
}
