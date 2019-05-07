package com.iwxyi.letsremember.Rank;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iwxyi.letsremember.R;
import com.iwxyi.letsremember.Rank.WorldRankContent.WorldRankItem;

import java.util.List;

public class MyWorldRankRecyclerViewAdapter extends RecyclerView.Adapter<MyWorldRankRecyclerViewAdapter.ViewHolder> {

    private final List<WorldRankContent.WorldRankItem> mValues;
    private final WorldRankFragment.OnWorldRankInteractionListener mListener;

    public MyWorldRankRecyclerViewAdapter(List<WorldRankContent.WorldRankItem> items, WorldRankFragment.OnWorldRankInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_worldrank, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(""+mValues.get(position).rank);
        holder.mContentView.setText(mValues.get(position).nickname);
        holder.mDetailView.setText(mValues.get(position).details);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onWorldRankInteraction(holder.mItem);
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
        public WorldRankContent.WorldRankItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            mDetailView = view.findViewById(R.id.detail);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
